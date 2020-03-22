package com.gardenlink_mobile.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.activities.MessagingActivity;
import com.gardenlink_mobile.adapters.ContactAdapter;
import com.gardenlink_mobile.entities.Photo;
import com.gardenlink_mobile.entities.Talk;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.CustomListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContactsFragment extends Fragment {


    private View mView;

    private List<Talk> mTalks;

    private List<User> mUsers;

    private HashMap<String,User> mUserPerTalk;

    private HashMap<String,User> mUserPerPhoto;

    private int mNumberOfPhotos;

    public static final Integer ORIGIN_ID = 2;


    public ContactsFragment(List<Talk> pTalks)
    {
        super();
        mTalks = pTalks;
        mUsers = new ArrayList<>();
        mUserPerTalk = new HashMap<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contacts_fragment, container, false);


        mView = view;

        mUsers.clear();

        recoverUsers();

        ((ListView) mView.findViewById(R.id.contactList)).setOnItemClickListener((adapterView, lView, i, l) -> ((MessagingActivity)getActivity()).loadConversation(mTalks.get(i)));

        if (mTalks.isEmpty())
        {
            mView.findViewById(R.id.noTalksMessage).setVisibility(View.VISIBLE);
        }

        return view;
    }


    private void recoverUsers()
    {
        for(Talk lTalk : mTalks)
        {
            ((MessagingActivity)getActivity()).getUser(findInterlocutor(lTalk),this.ORIGIN_ID);
        }
    }

    public void receiveUser(User pUser)
    {
        if(pUser!=null) {
            mUsers.add(pUser);
        }

        if(mUsers.size() == mTalks.size())
        {
            recoverUsersAvatars();
        }
    }

    private void recoverUsersAvatars()
    {
        countNumberOfPhotos();
        mUserPerPhoto = new HashMap<>();
        if(mNumberOfPhotos>0) {
            for (User lUser : mUsers) {
                if (lUser.getPhoto() != null && !lUser.getPhoto().isEmpty()) {
                    mUserPerPhoto.put(lUser.getPhoto(), lUser);
                    ((MessagingActivity) getActivity()).getPhotos(lUser.getPhoto(), this.ORIGIN_ID);
                }

            }
        }
        else
        {
            displayContacts();
        }
    }

    public void receivePhoto(Drawable pPhoto,String filename)
    {
        mNumberOfPhotos--;

        mUserPerPhoto.get(filename).setDrawablePhoto(pPhoto);

        if(mNumberOfPhotos==0)
        {
            displayContacts();
        }

    }


    private void displayContacts()
    {
        createMapForUserPerTalk();
        ArrayList<Talk> lArray = new ArrayList<>(mTalks);
        CustomListView lList = mView.findViewById(R.id.contactList);
        ContactAdapter lAdapter = new ContactAdapter(getContext(), lArray,mUserPerTalk);

        lList.setAdapter(lAdapter);


    }

    private void countNumberOfPhotos()
    {
        mNumberOfPhotos = 0;
        for(User lUser : mUsers)
        {
            if(lUser.getPhoto()!=null && !lUser.getPhoto().isEmpty())
            {
                mNumberOfPhotos++;
            }
        }

    }

    public void updateTalks(List<Talk> pTalks,boolean pIsOnContact)
    {
        mTalks = pTalks;

        if(pIsOnContact) {
            recoverUsers();
        }
    }

    private void createMapForUserPerTalk()
    {
        for(Talk lTalk : mTalks)
        {
            mUserPerTalk.put(lTalk.getId(),findUserForTalk(findInterlocutor(lTalk)));
        }

    }

    private User findUserForTalk(String pId){

        for(User lUser : mUsers)
        {
            if(pId.equals(lUser.getId()))
            {
                return lUser;
            }
        }
        return null;
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
