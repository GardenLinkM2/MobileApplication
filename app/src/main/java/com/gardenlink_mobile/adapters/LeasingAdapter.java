package com.gardenlink_mobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
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
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.utils.DateMaster;

import java.util.ArrayList;
import java.util.Date;

public class LeasingAdapter extends ArrayAdapter<Leasing> {

    private static final String TAG = "LeasingAdapteur";

    private static String DURATION_POSTFIX = " semaine(s) restante";
    private static String DATE_FORMAT = "dd-MM-yyyy";
    private static String STATE = "Etat : ";
    private static String IN_DEMAND = "En Attente..";

    private static Integer SEUIL = 2;
    private Leasing leasing;
    private Garden garden;
    private ArrayList<Garden> gardensList;

    public LeasingAdapter(Context context, ArrayList<Leasing> results, Garden garden) {
        super(context, 0, results);
        this.garden = garden;
    }

    public LeasingAdapter(Context context, ArrayList<Leasing> results, ArrayList<Garden> gardensList) {
        super(context, 0, results);
        this.gardensList = gardensList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        leasing = getItem(position);

        if (!gardensList.isEmpty()) {
            garden = gardensList.get(position);
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.land_item, parent, false);
        }

        TextView landTitle = convertView.findViewById(R.id.resultTitle);
        TextView landDuration = convertView.findViewById(R.id.resultDuration_State);
        TextView landLocation = convertView.findViewById(R.id.resultLocation);
        ImageView landImage = convertView.findViewById(R.id.resultPhoto);
        setItemClickListenerOnRequest(convertView, garden.getId());

        if (garden != null) {
            landTitle.setText(garden.getName());

            Date beginDate = DateMaster.TimeStampToDate(leasing.getBegin());
            Date endDate = DateMaster.TimeStampToDate(leasing.getEnd());


            if (leasing.getState() != null) {
                switch (leasing.getState().getLeasingStatus()) {
                    case "InDemand":
                        landDuration.setText(STATE + IN_DEMAND);
                        Log.i(TAG, "Leasing InDemand");
                        break;
                    case "InProgress":
                        if (beginDate != null && endDate != null) {
                            Integer end = getWeeksDifference(beginDate, endDate);
                            if (end != null) {
                                landDuration.setText(end + DURATION_POSTFIX);
                                if (end <= SEUIL) {
                                    landDuration.setTextColor(Color.RED);
                                } else {
                                    landDuration.setTextColor(Color.BLACK);
                                }
                            }
                        }
                        Log.i(TAG, "Leasing InProgress");
                        break;
                    case "Finished":
                        Log.i(TAG, "Leasing Finished");
                        break;
                    default:
                        Log.i(TAG, "Error in LeasingState");
                        break;
                }
            }
            landLocation.setText(garden.getLocation().getPostalCode() + " " + garden.getLocation().getCity());
            landImage.setImageResource(R.drawable.image_not_found);
        } else {
            Log.e(TAG, "Garden doesn't exist for leasing : " + leasing.getId());
        }
        return convertView;
    }

    private void setItemClickListenerOnRequest(View convertView, String id) {
        ConstraintLayout details = convertView.findViewById(R.id.resultDetails);
        details.setOnClickListener(v -> {
            Intent localIntentDetail = new Intent(getContext(), DetailAnnouncement.class);
            localIntentDetail.putExtra(DetailAnnouncement.EXTRA_MESSAGE, id);
            getContext().startActivity(localIntentDetail);
        });
    }

    private int getWeeksDifference(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return 0;
        float days = (float) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
        return (int) (days / 7);
    }
}
