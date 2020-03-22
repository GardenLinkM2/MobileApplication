package com.gardenlink_mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Message;
import com.gardenlink_mobile.entities.Talk;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.fragments.ContactsFragment;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.ImageMaster;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ContactAdapter extends ArrayAdapter<Talk> {

    private HashMap<String,User> mUserMap;



    public ContactAdapter(Context context, ArrayList<Talk> results,HashMap<String,User> pUserMap)
    {
        super(context,0,results);
        mUserMap=pUserMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Talk lTalk = getItem(position);
        User lInterlocutor = mUserMap.get(lTalk.getId());
        boolean lHasNewMessage = false;


        convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_item, parent, false);

        ImageView lAvatar = convertView.findViewById(R.id.contact_avatar);
        ImageView lNewMessageIcon = convertView.findViewById(R.id.newMessageIcon);
        TextView lName = convertView.findViewById(R.id.contact_name);
        TextView lDate = convertView.findViewById(R.id.lastMessageDate);


        lName.setText(lInterlocutor.getFirstName());

        for(Message lM : lTalk.getMessages())
        {
            if(!lM.getRead() && !lM.getSender().equals(Session.getInstance().getCurrentUser().getId()))
            {
                lHasNewMessage = true;
            }
        }

        if(lHasNewMessage)
        {
            lNewMessageIcon.setVisibility(View.VISIBLE);
        }
        else
        {
            lNewMessageIcon.setVisibility(View.INVISIBLE);
        }

        if(lTalk.getMessages()!= null && !lTalk.getMessages().isEmpty())
        {
            SimpleDateFormat lSdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            lDate.setText(lSdf.format(new Date(lTalk.getMessages().get(lTalk.getMessages().size()-1).getCreationDate().longValue()*1000)));
        }


        if(mUserMap.get(lTalk.getId()).getDrawablePhoto()!=null)
        {
            lAvatar.setImageDrawable(mUserMap.get(lTalk.getId()).getDrawablePhoto());
        }
        else {
            lAvatar.setImageResource(R.drawable.image_not_found);
        }



        return convertView;
    }
}
