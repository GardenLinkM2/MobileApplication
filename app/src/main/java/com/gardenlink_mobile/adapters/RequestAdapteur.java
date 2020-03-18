package com.gardenlink_mobile.adapters;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.entities.Location;
import com.gardenlink_mobile.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class RequestAdapteur extends ArrayAdapter<Leasing> {

    private static String DATE_FORMAT = "dd-MM-yyyy";
    private static String WEEKS = " moi(s)";
    private static User renter;
    private static Garden garden;

    public RequestAdapteur(Context context, ArrayList<Leasing> results) {
        super(context, 0, results);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Leasing result = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_item, parent, false);
        }

        setItemClickListenerOnRequest(convertView, result);
        TextView requestTitle = convertView.findViewById(R.id.requestTitle);
        TextView requestSender = convertView.findViewById(R.id.requestSender);
        CircleImageView requestSenderAvatar = convertView.findViewById(R.id.sender_avatar);
        TextView requestDuration = convertView.findViewById(R.id.requestDuration);
        TextView requestMoment = convertView.findViewById(R.id.requestMoment);

        //TODO : refacto
        renter = new User();
        renter.setFirstName("Denis");
        renter.setLastName("Pouglou");
        garden = new Garden();
        Location location = new Location();
        location.setPostalCode(63000);
        location.setCity("Clermont-Ferrand");
        garden.setId("1");
        garden.setLocation(location);
        garden.setName("petit jardinarzeazeezazeaazzaezaeezazezaeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeazea");
        garden.setLocation(location);

        requestTitle.setText(garden.getName());
        requestSender.setText(renter.getLastName() + " " + renter.getFirstName());
        requestSenderAvatar.setImageDrawable(getContext().getDrawable(R.drawable.sample_avatar));
        Random random = new Random();
        int randomNumber = random.nextInt(10 - 1 + 1) + 1;
        requestDuration.setText(Integer.toString(randomNumber) + WEEKS);
        requestMoment.setText(new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(new Date()));
        return convertView;
    }

    private void setItemClickListenerOnRequest(View convertView, Leasing result) {
        ImageView acceptButton = convertView.findViewById(R.id.accept_button);
        ImageView declineButton = convertView.findViewById(R.id.decline_button);
        ImageView detailsButton = convertView.findViewById(R.id.detail_button);

        acceptButton.setOnClickListener(v -> {
            //TODO : make real call with id in the intent to retrieve the details of the garden
            // TODO : mix String and Id from R.String.* is not good .... signed NK
            Toast.makeText(getContext(), R.string.accept_request + renter.getLastName() + " " + renter.getFirstName() + R.string.on_land + garden.getName(), Toast.LENGTH_SHORT).show();
            remove(result);
        });

        declineButton.setOnClickListener(v -> {
            //TODO : make real call with id in the intent to retrieve the details of the garden
            Toast.makeText(getContext(), R.string.decline_request + renter.getLastName() + " " + renter.getFirstName() + R.string.on_land + garden.getName(), Toast.LENGTH_SHORT).show();
            remove(result);
        });

        //TODO : make real call with id in the intent to retrieve the details of the garden
        detailsButton.setOnClickListener(v -> Toast.makeText(getContext(), R.string.show_details + garden.getName(), Toast.LENGTH_SHORT).show());
    }
}