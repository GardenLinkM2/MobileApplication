package com.gardenlink_mobile.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Garden;

import java.util.ArrayList;

public class ResultsAdapter extends ArrayAdapter<Garden> {

    private static String PRICE_POSTFIX="€ / Mois";
    private static String SIZE_POSTFIX=" m2";
    private static String DURATION_POSTFIX=" mois";

    public ResultsAdapter(Context context, ArrayList<Garden> results)
    {
        super(context,0,results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Garden result = getItem(position);
        if(convertView==null)
        {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.result_item,parent,false);
        }

        TextView lTitle = convertView.findViewById(R.id.resultTitle);
        TextView lPrice = convertView.findViewById(R.id.resultPrice);
        TextView lSize = convertView.findViewById(R.id.resultSize);
        TextView lDuration = convertView.findViewById(R.id.resultDuration);
        TextView lLocation = convertView.findViewById(R.id.resultLocation);
        ImageView lImage = convertView.findViewById(R.id.resultPhoto);

        lTitle.setText(result.getName());
        lPrice.setText(result.getCriteria().getPrice()+PRICE_POSTFIX);
        lDuration.setText(result.getMinUse()+DURATION_POSTFIX);
        lLocation.setText(result.getLocation().getPostalCode() + " "+result.getLocation().getCity());

        if (result.getDrawableFirstPhoto() != null)

            lImage.setImageDrawable(result.getDrawableFirstPhoto());
        else lImage.setImageResource(R.drawable.image_not_found);
        lImage.setVisibility(View.VISIBLE);
        ((ProgressBar) convertView.findViewById((R.id.progress_bar_photo))).setVisibility(View.GONE);
        return convertView;
    }
}
