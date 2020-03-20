package com.gardenlink_mobile.adapters;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Score;
import com.gardenlink_mobile.fragments.CommentsFragment;

import java.util.ArrayList;
import java.util.List;

public class CommentAdapter extends ArrayAdapter<Score> {

    private int mTagColor;

    public CommentAdapter(Context context, List<Score> comments, int color) {
        super(context,0,comments);
        mTagColor = color;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Score lComment = getItem(position);
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.comment_item, parent, false);
        }
        TextView lUsername = convertView.findViewById(R.id.commentUserName);
        ImageView lPicture = convertView.findViewById(R.id.commentPicture);
        lUsername.setText(lComment.getRater().getFirstName());
        parseTextForResponseTag(lComment.getComment(),convertView);

        //TODO : make the real call for the image
        lPicture.setImageResource(R.drawable.image_not_found);
        convertView.setOnClickListener(null);
        return convertView;
    }

    private void parseTextForResponseTag(String pMessage, View convertView)
    {
        TextView lMessageField = convertView.findViewById(R.id.commentMesssage);
        if(!pMessage.isEmpty())
        {
            String lMessage=pMessage;
            if(pMessage.charAt(0) == CommentsFragment.RESPONSE_TAG)
            {
                int i=0;
                while(pMessage.charAt(i)!=' ')
                {
                    i++;
                }
                Spannable spannable = new SpannableString(lMessage);
                spannable.setSpan(new ForegroundColorSpan(mTagColor),0,i,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                lMessageField.setText(spannable, TextView.BufferType.SPANNABLE);
            }
            else
            {
                lMessage = pMessage;
                lMessageField.setText(lMessage);
            }
        }
    }
}
