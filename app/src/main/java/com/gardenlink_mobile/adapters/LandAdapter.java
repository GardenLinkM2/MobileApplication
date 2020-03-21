package com.gardenlink_mobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.activities.DetailAnnouncement;
import com.gardenlink_mobile.entities.Garden;

import java.util.ArrayList;

public class LandAdapter extends ArrayAdapter<Garden> {

    private static final String TAG = "LandAdapteur";

    private Garden garden;

    public LandAdapter(Context context, ArrayList<Garden> results, Garden garden) {
        super(context, 0, results);
        this.garden = garden;
    }

    public LandAdapter(Context context, ArrayList<Garden> results) {
        super(context, 0, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        garden = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.land_item, parent, false);
        }

        TextView landTitle = convertView.findViewById(R.id.resultTitle);
        TextView landLocation = convertView.findViewById(R.id.resultLocation);
        ImageView landImage = convertView.findViewById(R.id.resultPhoto);
        setItemClickListenerOnRequest(convertView, garden);

        landTitle.setText(garden.getName());
        landLocation.setText(garden.getLocation().getPostalCode() + " " + garden.getLocation().getCity());
        landTitle.setText(garden.getName());
        if (garden.getDrawableFirstPhoto() != null)
            landImage.setImageDrawable(garden.getDrawableFirstPhoto());
        else
            landImage.setImageResource(R.drawable.image_not_found);
        return convertView;
    }

    private void setItemClickListenerOnRequest(View convertView, Garden result) {
        ConstraintLayout details = convertView.findViewById(R.id.resultDetails);
        details.setOnClickListener(v -> {
            Intent localIntentDetail = new Intent(getContext(), DetailAnnouncement.class);
            localIntentDetail.putExtra(DetailAnnouncement.EXTRA_MESSAGE, result.getId());
            getContext().startActivity(localIntentDetail);
        });
    }
}