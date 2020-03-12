package com.gardenlink_mobile.utils;

import android.content.Context;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.entities.Location;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class LandsAdapter extends ArrayAdapter<Leasing> {

    private static String DURATION_POSTFIX = " semaine(s) restante";
    private static String DATE_FORMAT = "dd-MM-yyyy";
    private static Integer SEUIL = 2;

    public LandsAdapter(Context context, ArrayList<Leasing> results) {
        super(context, 0, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Leasing result = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.land_item, parent, false);
        }

        TextView landTitle = convertView.findViewById(R.id.resultTitle);
        TextView landDuration = convertView.findViewById(R.id.resultDuration);
        TextView landLocation = convertView.findViewById(R.id.resultLocation);
        ImageView landImage = convertView.findViewById(R.id.resultPhoto);

        //TODO Refacto
        Garden garden = new Garden();
        Location location = new Location();
        location.setPostalCode(63000);
        location.setCity("Clermont-Ferrand");
        garden.setId("1");
        garden.setLocation(location);
        garden.setName("petit jardinarzeazeezazeaazzaezaeezazezaeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeazea");
        garden.setLocation(location);

        landTitle.setText(garden.getName());

        Date dDate = stringToDate(result.getEnd(), DATE_FORMAT);
        Date dToday = getTodayDate();
        Integer end = getWeeksDifference(dDate, dToday);
        landDuration.setText(end + DURATION_POSTFIX);

        if (end <= SEUIL) {
            landDuration.setTextColor(Color.RED);
        } else {
            landDuration.setTextColor(Color.BLACK);
        }


        landLocation.setText(garden.getLocation().getPostalCode() + " " + garden.getLocation().getCity());
        landImage.setImageResource(R.drawable.image_not_found);

        return convertView;
    }

    private Date stringToDate(String aDate, String aFormat) {
        if (aDate == null) return null;
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpledateformat = new SimpleDateFormat(aFormat);
        return simpledateformat.parse(aDate, pos);
    }

    private int getWeeksDifference(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return 0;
        float days = (float) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
        return (int) (days / 7);
    }

    private Date getTodayDate() {
        String today = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date());
        return stringToDate(today, DATE_FORMAT);
    }


}
