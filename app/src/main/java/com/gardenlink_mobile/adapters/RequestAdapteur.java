package com.gardenlink_mobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.activities.DetailAnnouncement;
import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.activities.MyLandsActivity;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.entities.LeasingStatus;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.gardenlink_mobile.wsconnecting.operations.PUT_LEASING;
import com.gardenlink_mobile.utils.DateMaster;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class RequestAdapteur extends ArrayAdapter<Leasing> implements IWebConnectable {

    private static final String TAG = "RequestAdapteur";
    private static Boolean GET_MY_LEASING_FLAG = false;
    private static Boolean toRemove = false;
    private static Boolean dontRemove = false;

    private static String DATE_FORMAT = "dd-MM-yyyy";
    private static String MONTH = " mois";
    private static User renter;
    private static Garden garden;
    private final Context myContext;
    private Leasing leasing;
    private ArrayList<Garden> gardensList;
    private ArrayList<User> rentersList;

    public RequestAdapteur(Context context, ArrayList<Leasing> results, ArrayList<Garden> gardensList, ArrayList<User> rentersList) {
        super(context, 0, results);
        this.gardensList = gardensList;
        this.rentersList = rentersList;
        this.myContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        leasing = getItem(position);
        if (!gardensList.isEmpty()) {
            garden = gardensList.get(position);

        }
        if (!rentersList.isEmpty()) {
            renter = rentersList.get(position);
        }

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.request_item, parent, false);
        }

        TextView requestTitle = convertView.findViewById(R.id.requestTitle);
        TextView requestSender = convertView.findViewById(R.id.requestSender);
        CircleImageView requestSenderAvatar = convertView.findViewById(R.id.sender_avatar);
        TextView requestDuration = convertView.findViewById(R.id.requestDuration);
        TextView requestBeginDate = convertView.findViewById(R.id.requestBeginDate);
        TextView requestMoment = convertView.findViewById(R.id.requestMoment);

        if (garden != null) {
            requestTitle.setText(garden.getName());
            if (renter != null) {
                requestSender.setText(renter.getLastName() + " " + renter.getFirstName());
                requestSenderAvatar.setImageDrawable(getContext().getDrawable(R.drawable.sample_avatar));
                Date beginDate = DateMaster.TimeStampToDate(leasing.getBegin());
                Date endDate = DateMaster.TimeStampToDate(leasing.getEnd());
                if (beginDate != null) {
                    requestBeginDate.setText(new SimpleDateFormat(DATE_FORMAT).format(beginDate));
                    if (endDate != null) {
                        Integer end = getMonthDifference(beginDate, endDate);
                        requestDuration.setText(end + MONTH);
                    }
                }
                Date creationDate = DateMaster.TimeStampToDate(leasing.getCreation());
                if (creationDate != null) {
                    requestMoment.setText(new SimpleDateFormat(DATE_FORMAT).format(creationDate));
                }
                setItemClickListenerOnRequest(convertView, leasing, leasing.getId(), garden.getId());
            } else {
                Log.e(TAG, "Renter doesn't exist for leasing : " + leasing.getId());
            }
        } else {
            Log.e(TAG, "Garden doesn't exist for leasing : " + leasing.getId());
        }
        return convertView;
    }

    private void setItemClickListenerOnRequest(View convertView, Leasing leasing, String id, String gardenId) {
        ImageView acceptButton = convertView.findViewById(R.id.accept_button);
        ImageView declineButton = convertView.findViewById(R.id.decline_button);
        ImageView detailsButton = convertView.findViewById(R.id.detail_button);

        acceptButton.setOnClickListener(v -> {
            LeasingStatus inProgress = new LeasingStatus("InProgress");
            leasing.setState(inProgress);
            new PUT_LEASING(leasing, id).perform(new WeakReference<>(this));
        });

        declineButton.setOnClickListener(v -> {
            LeasingStatus refused = new LeasingStatus("Refused");
            leasing.setState(refused);
            new PUT_LEASING(leasing, id).perform(new WeakReference<>(this));
        });

        detailsButton.setOnClickListener(v -> {
            Intent localIntentDetail = new Intent(getContext(), DetailAnnouncement.class);
            localIntentDetail.putExtra(DetailAnnouncement.EXTRA_MESSAGE, gardenId);
            getContext().startActivity(localIntentDetail);
        });
    }

    private void itemToRemove(Leasing leasingToRemove) {
        while (!GET_MY_LEASING_FLAG){
            if (toRemove){
                Log.i(TAG, "Item to remove : " + leasingToRemove.toString());
                this.remove(leasingToRemove);
                Log.i(TAG, "Item removed");
                return;
            }
            if (dontRemove){
                Log.i(TAG, "don't remove item cause of failed put");
                return;
            }
        }
        return;
    }


    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        switch (operation.getName()) {
            case "PUT_LEASING":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Intent intent = new Intent(getContext(), MyLandsActivity.class);
                        intent.putExtra("viewpager_position", 1);
                        getContext().startActivity(intent);
                        ((MyLandsActivity)myContext).finish();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            default:
                Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
                return;
        }
    }

    private int getMonthDifference(Date fromDate, Date toDate) {
        if (fromDate == null || toDate == null)
            return 0;
        float days = (float) ((toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
        return (int) (days / 30);
    }

    @Override
    public String getTag() {
        return TAG;
    }
}
