package com.gardenlink_mobile.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.activities.MessagingActivity;
import com.gardenlink_mobile.activities.PostAnnouncement;
import com.gardenlink_mobile.adapters.MessagesAdapter;
import com.gardenlink_mobile.entities.Message;
import com.gardenlink_mobile.entities.Photo;
import com.gardenlink_mobile.entities.Talk;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.ImageMaster;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class ChatFragment extends Fragment {

    private final static String TAG = "ChatFragment";

    private final static Integer MAX_MESSAGES_DISPLAYED = 10;

    private View mView;

    private Talk mTalk;

    private static final int readStoragePermission = 0;

    private ArrayList<Bitmap> mJointImages;

    private boolean mIsNewTalk = false;

    private static final String ERROR_SELECT_IMAGE = "Erreur! Le fichier sélectionné n'est pas une image!";

    public static final Integer ORIGIN_ID = 1;

    private Message mCurrentMessageInSending;

    private List<Message> mUpdateMessages;

    private HashMap<String,Message> mPhotoForUpdatesMessages;

    private Integer mNbOfPhotosForUpdate;

    private boolean mIsOnContact;

    private int mPage = 1;

    private boolean hasBeenPosted = false;

    public ChatFragment(Talk pTalk) {
        super();
        mTalk = pTalk;
    }

    public ChatFragment(Talk pTalk, Boolean pIsNewTalk) {
        super();
        mTalk = pTalk;
        mIsNewTalk = pIsNewTalk;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        mView = view;
        mPage=1;

        ((MessagingActivity) getActivity()).getUser(findInterlocutor(mTalk), this.ORIGIN_ID);

        return view;
    }

    public void endInit(User pUser) {
        mJointImages = new ArrayList<>();
        initSendField();
        displayMessages();
        if (pUser != null) {
            ((TextView) mView.findViewById(R.id.contactTitle)).setText(pUser.getFirstName());
        }
        toScrollViewBottom();
    }

    private void initSendField() {
        TextInputLayout lInputLayout = mView.findViewById(R.id.MessageTextInputLayout);

        lInputLayout.setEndIconOnClickListener(view -> {
            sendMessage();
        });


        lInputLayout.setStartIconOnClickListener(view -> {
            initUploadAvatar();
        });

    }

    private void sendMessage() {
        TextInputLayout lInputLayout = mView.findViewById(R.id.MessageTextInputLayout);
        TextInputEditText lInput = mView.findViewById(R.id.messageField);

        String lMessageText = lInput.getText().toString();
        Message lMessage = new Message();
        lMessage.setRead(false);
        lMessage.setText(lMessageText);
        lMessage.setSender(Session.getInstance().getCurrentUser().getId());
        lMessage.setCreationDate(System.currentTimeMillis());
        lMessage.setPhotos(new ArrayList<Photo>());

        Timestamp ts = new Timestamp(System.currentTimeMillis());



        lInput.setText("");
        lInput.clearFocus();
        lInputLayout.clearFocus();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mView.getWindowToken(), 0);
        toScrollViewBottom();

        if (!mJointImages.isEmpty()) {
            mCurrentMessageInSending = lMessage;
            for (Bitmap loB : mJointImages) {
                ByteArrayOutputStream uploadableImage = new ByteArrayOutputStream();
                loB.compress(Bitmap.CompressFormat.JPEG, 100, uploadableImage);
                byte[] imageBytes = uploadableImage.toByteArray();

                ((MessagingActivity) getActivity()).postPhoto(imageBytes);
            }

        } else {
            ((MessagingActivity) getActivity()).sendNewMessage(mTalk.getId(), lMessage);
        }

        hasBeenPosted = true;


    }

    public void receivePhotosUrl(Photo pPhoto) {
        if (pPhoto != null) {
            mCurrentMessageInSending.getPhotos().add(pPhoto);
        }
        if (mCurrentMessageInSending.getPhotos().size() == mJointImages.size()) {
            mJointImages.clear();
            displayImageForPreview();
            postMessage();
        }
    }

    public void postMessage() {
        ((MessagingActivity) getActivity()).sendNewMessage(mTalk.getId(), mCurrentMessageInSending);
    }

    public void updateTalk(Talk pTalk, boolean pIsOnContact) {

        if(pTalk.getMessages().size()>mTalk.getMessages().size()) {
            mIsOnContact=pIsOnContact;
            addNewMessages(pTalk);

        }
        else if(hasBeenPosted)
        {
            displayMessages();
            hasBeenPosted = false;
        }
    }

    private void addNewMessages(Talk pTalk)
    {
        mUpdateMessages = new ArrayList<Message>();
        mPhotoForUpdatesMessages = new HashMap<>();
        mNbOfPhotosForUpdate=0;

        int begin = mTalk.getMessages().size();

        for(int i=begin;i<pTalk.getMessages().size();i++)
        {
            mTalk.getMessages().add(pTalk.getMessages().get(i));
            mUpdateMessages.add(pTalk.getMessages().get(i));
            if(pTalk.getMessages().get(i).getPhotos()!= null && !pTalk.getMessages().get(i).getPhotos().isEmpty())
            {
                mNbOfPhotosForUpdate++;
                pTalk.getMessages().get(i).setMessagesPhotos(new ArrayList<Drawable>());
                for(Photo lPhoto : pTalk.getMessages().get(i).getPhotos())
                {

                    mPhotoForUpdatesMessages.put(lPhoto.getFileName(),pTalk.getMessages().get(i));
                    ((MessagingActivity)getActivity()).getPhotos(lPhoto.getFileName(),this.ORIGIN_ID);
                }
            }
        }

        if(mNbOfPhotosForUpdate==0)
        {
            doUpdate();
        }



    }

    public void receivePhotos(Drawable pPhoto,String pFileName)
    {
        mNbOfPhotosForUpdate--;
        mPhotoForUpdatesMessages.get(pFileName).getMessagesPhotos().add(pPhoto);


        if(mNbOfPhotosForUpdate==0)
        {
            doUpdate();
        }


    }

    private void doUpdate()
    {
        if (!mIsOnContact) {
            displayMessages();
            if (!mView.findViewById(R.id.messageField).hasFocus()) {
                toScrollViewBottom();
            }
        }
    }

    private void displayImageForPreview() {

        LinearLayout lPreview = mView.findViewById(R.id.attachementDisplay);

        lPreview.removeAllViews();

        for (Bitmap lBitmap : mJointImages) {
            ImageView image = new ImageView(getActivity());

            image.setLayoutParams(new android.view.ViewGroup.LayoutParams(125, 125));
            image.setMaxHeight(125);
            image.setMaxWidth(125);
            image.setImageBitmap(lBitmap);

            image.setOnClickListener(view -> {
                lPreview.removeView(view);
                mJointImages.remove(((BitmapDrawable) image.getDrawable()).getBitmap());
                displayImageForPreview();
            });

            lPreview.addView(image);
        }


    }

    private void toScrollViewBottom() {
        mView.post(new Runnable() {
            @Override
            public void run() {
                ScrollView lScrollView = mView.findViewById(R.id.messagesDisplay);
                lScrollView.fullScroll(View.FOCUS_DOWN);

                ScrollView lScroll = ((ScrollView) mView.findViewById(R.id.messagesDisplay));

                lScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                    @Override
                    public void onScrollChanged() {
                        int scrollY = lScroll.getScrollY(); // For ScrollView

                        if(scrollY >= lScroll.getMaxScrollAmount())
                        {
                            lScroll.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                                @Override
                                public void onScrollChanged() {
                                    int scrollY = lScroll.getScrollY(); // For ScrollView

                                    if(scrollY == 0 && (mPage*MAX_MESSAGES_DISPLAYED)<mTalk.getMessages().size())
                                    {
                                        lScroll.setScrollY(lScroll.getMaxScrollAmount());
                                        mPage++;
                                        displayMessages();
                                    }

                                }
                            });
                            lScroll.getViewTreeObserver().removeOnScrollChangedListener(this);
                        }



                    }
                });


            }

        });

    }

    private void initUploadAvatar() {

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    readStoragePermission);
        } else {
            openImageExplorer();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == readStoragePermission && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openImageExplorer();
        }
    }

    private void openImageExplorer() {
        final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {
                final Uri uri = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getActivity().getContentResolver().openInputStream(uri);
                    mJointImages.add(BitmapFactory.decodeStream(inputStream));
                    displayImageForPreview();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getResources().getString(R.string.error), Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e(TAG, "onActivityResult: ", e);
                }
            } else {
                Toast.makeText(getActivity(), ERROR_SELECT_IMAGE, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void receiveNewMessage(Message pMessage) {
        mTalk.getMessages().add(pMessage);
        displayMessages();

    }


    public void displayMessages() {
        ArrayList<Message> lMessages = new ArrayList<Message>();


        int maxMessages = mTalk.getMessages().size();
        int maxMessagesToDisplay = MAX_MESSAGES_DISPLAYED * mPage;
        int messageToDisplay = maxMessages>maxMessagesToDisplay ? maxMessagesToDisplay : maxMessages;
        for(int i=maxMessages - messageToDisplay; i<maxMessages;i++)
        {
            lMessages.add(mTalk.getMessages().get(i));
        }

        MessagesAdapter lAdapter = new MessagesAdapter(getContext(), lMessages);
        ListView lList = mView.findViewById(R.id.messagesList);

        lList.setAdapter(lAdapter);

    }

    public String getTalkId() {
        return mTalk.getId();
    }

    private String findInterlocutor(Talk pTalk)
    {
        if(pTalk.getSender().equals(Session.getInstance().getCurrentUser().getId()))
        {
            return  pTalk.getReceiver();
        }
        else
        {
            return  pTalk.getSender();
        }
    }
}
