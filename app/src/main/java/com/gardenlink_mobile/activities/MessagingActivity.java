package com.gardenlink_mobile.activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.fragment.app.FragmentTransaction;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Message;
import com.gardenlink_mobile.entities.Photo;
import com.gardenlink_mobile.entities.Talk;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.fragments.ChatFragment;
import com.gardenlink_mobile.fragments.ContactsFragment;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.Escaper;
import com.gardenlink_mobile.utils.ImageMaster;
import com.gardenlink_mobile.wsconnecting.operations.GET_PHOTO;
import com.gardenlink_mobile.wsconnecting.operations.GET_TALKS;
import com.gardenlink_mobile.wsconnecting.operations.GET_USER;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.gardenlink_mobile.wsconnecting.operations.POST_MESSAGE;
import com.gardenlink_mobile.wsconnecting.operations.POST_PHOTO;
import com.gardenlink_mobile.wsconnecting.operations.POST_TALK;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MessagingActivity extends NavigableActivity implements IWebConnectable {

    private static final String TAG = "MessagingActivity";

    public static final String NEW_MESSAGE_USER_ID = "newMessageUserId";

    private static final int TIME_OF_REFRESH = 5000;

    public static final Integer ORIGIN_ID = 0;

    private ChatFragment mChat = null;

    private ContactsFragment mContacts;

    private Menu mContactMenu;

    private List<Talk> mTalks;

    private boolean mIsOnContact = false;

    private int mGetUserOrigin = 0;

    private int mGetPhotosOrigin = 0;

    private boolean mIsInit = false;

    private Runnable mUpdater;

    private Handler mHandler;

    private HashMap<String, Message> mMapForPhotos;

    private Talk mTalkToDisplay;

    private Integer mNbOfPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging_activity);

        initMenu();
        loadTalk();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.contactmenu, menu);
        mContactMenu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.action_contact:
                toggleContact();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void endInit() {

        mContacts = new ContactsFragment(mTalks);

        Bundle lBundle = getIntent().getExtras();

        if (lBundle != null && lBundle.getString(this.NEW_MESSAGE_USER_ID) != null) {
            getUser(lBundle.getString(this.NEW_MESSAGE_USER_ID), this.ORIGIN_ID);
        } else {
            displayContacts();

        }

        mHandler = new Handler();

        mUpdater = new Runnable() {
            @Override
            public void run() {

                loadTalk();
                mHandler.postDelayed(mUpdater, TIME_OF_REFRESH);
            }
        };


        mHandler.postDelayed(mUpdater, TIME_OF_REFRESH);

        mIsInit = true;

    }

    private void updateTalks() {
        mContacts.updateTalks(mTalks, mIsOnContact);

        if (mChat != null) {
            mChat.updateTalk(findTalk(mChat.getTalkId()), mIsOnContact);
        }

    }

    private Talk findTalk(String pId) {
        for (Talk lTalk : mTalks) {
            if (lTalk.getId().equals(pId)) {
                return lTalk;
            }
        }

        return null;
    }

    private void toggleContact() {
        if (mIsOnContact && mChat != null) {
            displayChat();
            mIsOnContact = false;
        } else if (!mIsOnContact) {
            displayContacts();
            mIsOnContact = true;
        }

    }

    private void displayChat() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.chatContainer, mChat).show(mChat).commit();
    }

    private void displayContacts() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.chatContainer, mContacts).show(mContacts).commit();
    }

    public void loadTalk() {
        new GET_TALKS().perform(new WeakReference<>(this));
    }


    private Integer findNumbersOfPhotos(Talk pTalk) {
        int nb = 0;
        for (Message lMessage : mTalkToDisplay.getMessages()) {
            if (lMessage.getPhotos() != null && !lMessage.getPhotos().isEmpty()) {
                nb += lMessage.getPhotos().size();
            }
        }
        return nb;
    }

    public void loadConversation(Talk pTalk) {
        mIsOnContact = false;
        mMapForPhotos = new HashMap<>();
        mTalkToDisplay = pTalk;
        mNbOfPhotos = findNumbersOfPhotos(pTalk);
        mGetPhotosOrigin = this.ORIGIN_ID;

        for (Message lMessage : mTalkToDisplay.getMessages()) {
            if (lMessage.getPhotos() != null && !lMessage.getPhotos().isEmpty()) {

                lMessage.setMessagesPhotos(new ArrayList<Drawable>());
                for (Photo lPhoto : lMessage.getPhotos()) {

                    mMapForPhotos.put(lPhoto.getFileName(), lMessage);
                    new GET_PHOTO(lPhoto.getFileName()).perform(new WeakReference<>(this));
                }
            }
        }

        displayConversation();
    }


    public void displayConversation() {
        mChat = new ChatFragment(mTalkToDisplay);
        displayChat();

    }


    public void createNewConversation(User pInterlocutor) {
        Talk lNewTalk = new Talk();
        lNewTalk.setSender(Session.getInstance().getCurrentUser().getId());
        lNewTalk.setReceiver(pInterlocutor.getId());
        lNewTalk.setMessages(new ArrayList<Message>());
        lNewTalk.setSubject("");
        lNewTalk.setArchived(false);

        postNewTalk(lNewTalk);

    }

    public void getUser(String pId, int pOrigin) {
        this.mGetUserOrigin = pOrigin;
        new GET_USER(pId).perform(new WeakReference<>(this));
    }


    public void getPhotos(String filename, int pOrigin) {
        mGetPhotosOrigin = pOrigin;
        new GET_PHOTO(filename).perform(new WeakReference<>(this));
    }


    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {

        switch (operation.getName()) {
            case "GET_TALKS":
                if (responseCode == 200) {
                    List<Talk> lTalk = (List<Talk>) results;
                    mTalks = new ArrayList<Talk>();
                    if (lTalk != null) {
                        mTalks.addAll(lTalk);
                    }
                    if (!mIsInit) {
                        endInit();
                    } else {
                        updateTalks();
                    }
                } else {
                    Log.e(TAG, "Error getting talks");
                }
                break;


            case "POST_TALK":
                if (responseCode == 201) {
                    Talk lTalkUpdated = (Talk) results.get(0);
                    Log.i(TAG, "Post of new talk Successfull");
                    if (lTalkUpdated != null) {
                        receiveNewTalk(lTalkUpdated);
                    }
                } else {
                    Log.e(TAG, "Error while posting new Talk");
                }
                break;


            case "GET_USER":
                if (results != null && !results.isEmpty()) {
                    User lUser = (User) results.get(0);
                    if (mGetUserOrigin == ChatFragment.ORIGIN_ID) {
                        mChat.endInit(lUser);
                    } else if (mGetUserOrigin == ContactsFragment.ORIGIN_ID) {
                        mContacts.receiveUser(lUser);
                    } else if (mGetUserOrigin == this.ORIGIN_ID) {
                        createNewConversation(lUser);
                    }

                }

                break;

            case "POST_MESSAGE":
                if (responseCode == 201) {
                    Log.i(TAG, "Post of new message Successfull");
                    mChat.displayMessages();
                }

                break;

        }

        //Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    public void receiveNewTalk(Talk pNewTalk) {
        mChat = new ChatFragment(pNewTalk);
        displayChat();

    }

    public void postNewTalk(Talk pTalk) {
        new POST_TALK(pTalk).perform(new WeakReference<>(this));
    }

    public void sendNewMessage(String pTalkId, Message pNewMessage) {
        new POST_MESSAGE(pTalkId, pNewMessage).perform(new WeakReference<>(this));
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        switch (operation.getName()) {
            case "POST_PHOTO":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        ArrayList<Photo> photos = new ArrayList<>();
                        photos.add(new Photo(Escaper.escapePhotoURL(results.get("photo"))));
                        mChat.receivePhotosUrl(photos.get(0));
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }

            case "GET_PHOTO":
                switch (responseCode) {
                    case 200:
                        if (mGetPhotosOrigin == this.ORIGIN_ID) {
                            mMapForPhotos.get(results.get("url")).getMessagesPhotos().add(ImageMaster.byteStringToDrawable(results.get("photo")));
                            mNbOfPhotos--;
                            if (mNbOfPhotos == 0) {
                                displayConversation();
                            }
                        } else if (mGetPhotosOrigin == ContactsFragment.ORIGIN_ID) {
                            mContacts.receivePhoto(ImageMaster.byteStringToDrawable(results.get("photo")), results.get("url"));
                        }
                        else if(mGetPhotosOrigin==ChatFragment.ORIGIN_ID)
                        {
                            mChat.receivePhotos(ImageMaster.byteStringToDrawable(results.get("photo")), results.get("url"));
                        }

                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }


            default:
                Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
                return;
        }


        //Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public void postPhoto(byte[] pPhoto) {
        new POST_PHOTO(pPhoto).perform(new WeakReference<>(this));
    }
}
