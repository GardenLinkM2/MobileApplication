package com.gardenlink_mobile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Message;
import com.gardenlink_mobile.session.Session;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MessagesAdapter extends ArrayAdapter<Message> {

    private Bitmap mTest = null;

    public MessagesAdapter(Context context, ArrayList<Message> results) {
        super(context, 0, results);
    }

    public MessagesAdapter(Context context, ArrayList<Message> results,Bitmap image) {
        super(context, 0, results);
        mTest=image;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Message lMessage = getItem(position);


        if (lMessage.getSender().equals(Session.getInstance().getCurrentUser().getId())) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.my_message_item, parent, false);
        } else {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.their_message_item, parent, false);
            lMessage.setRead(true);
        }

        TextView lMessageBody = convertView.findViewById(R.id.message_body);

        TextView lMessageDate = convertView.findViewById(R.id.messageTime);

        LinearLayout lLayout = (LinearLayout)convertView.findViewById(R.id.messageImageContainer);

        if (lMessage.getMessagesPhotos() != null) {
            for (Drawable lDrawable : lMessage.getMessagesPhotos()) {
                ImageView image = new ImageView(getContext());
                image.setLayoutParams(new android.view.ViewGroup.LayoutParams(250, 250));
                image.setImageDrawable(lDrawable);
                image.setMaxHeight(250);
                image.setMaxWidth(250);
                image.setVisibility(View.VISIBLE);
                lLayout.addView(image);


            }
            lLayout.setVisibility(View.VISIBLE);
        }

        if(lMessage.getCreationDate()!=null)
        {

            SimpleDateFormat lSdf = new SimpleDateFormat("dd/MM HH:mm");

            lMessageDate.setText(lSdf.format(new Date(lMessage.getCreationDate().longValue()*1000)));
        }


        lMessageBody.setText(lMessage.getText());

        convertView.setOnClickListener(null);


        return convertView;
    }


}
