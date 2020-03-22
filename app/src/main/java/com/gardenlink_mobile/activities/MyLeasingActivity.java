package com.gardenlink_mobile.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.adapters.LeasingAdapter;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.wsconnecting.operations.GET_GARDEN;
import com.gardenlink_mobile.wsconnecting.operations.GET_MY_LEASING;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import com.google.android.material.snackbar.Snackbar;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyLeasingActivity extends NavigableActivity implements IWebConnectable {

    private static final String TAG = "MyLeasingActivity";
    private Boolean GET_MY_LEASING_FLAG = false;
    private Boolean GET_GARDEN = false;

    private ArrayList<Leasing> getAllLeasing;

    private ListView listViewMyLeasing;
    private ListView listViewMyRequest;
    private ListView listViewMyEndedLeasing;
    private Leasing leasing;
    private Garden garden;
    private ArrayList<Garden> gardensListInProgress;
    private ArrayList<Garden> gardensListInDemand;
    private ArrayList<Garden> gardensListFinished;
    private int size = 0;
    private int currentSize = 0;
    private int okGarden = 0;
    private ArrayList<Leasing> getAllLeasingInProgress;
    private ArrayList<Leasing> getAllLeasingInDemand;
    private ArrayList<Leasing> getAllLeasingFinished;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myleasing_activity);

        initMenu();
        getMyLeasing();
        createLists();
    }

    private void createLists() {
        gardensListInProgress = new ArrayList<Garden>();
        gardensListInDemand = new ArrayList<Garden>();
        gardensListFinished = new ArrayList<Garden>();

        listViewMyLeasing = findViewById(R.id.myLeasingList);
        listViewMyRequest = findViewById(R.id.myRequestList);
        listViewMyEndedLeasing = findViewById(R.id.myEndedLeasingList);
    }

    private void getMyLeasing() {
        Toast.makeText(this, "Chargement en cours...", Toast.LENGTH_SHORT).show();
        new GET_MY_LEASING().perform(new WeakReference<>(this));
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        switch (operation.getName()) {
            case "GET_MY_LEASING":
                switch (responseCode) {
                    case 200:
                        if (results == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            Snackbar snackbar = Snackbar.make(findViewById(R.id.myLeasing_Activity), getResources().getString(R.string.no_result), Snackbar.LENGTH_LONG);
                            View sbView= snackbar.getView();
                            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
                            snackbar.show();
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        List<Leasing> leasings = (List<Leasing>) results;
                        size = leasings.size();
                        getAllLeasing = new ArrayList<>(leasings);
                        getAllLeasingInProgress = new ArrayList<Leasing>();
                        getAllLeasingInDemand = new ArrayList<Leasing>();
                        getAllLeasingFinished = new ArrayList<Leasing>();
                        for (int i = 0; i < size; i++) {
                            leasing = getAllLeasing.get(i);
                            switch (leasing.getState().getLeasingStatus()) {
                                case "InProgress":
                                    getAllLeasingInProgress.add(leasing);
                                    currentSize++;
                                    new GET_GARDEN(leasing.getGarden()).perform(new WeakReference<>(this));
                                    break;
                                case "InDemand":
                                    getAllLeasingInDemand.add(leasing);
                                    currentSize++;
                                    new GET_GARDEN(leasing.getGarden()).perform(new WeakReference<>(this));
                                    break;
                                case "Finished":
                                    getAllLeasingFinished.add(leasing);
                                    currentSize++;
                                    new GET_GARDEN(leasing.getGarden()).perform(new WeakReference<>(this));
                                    break;
                                default:
                                    Log.e(TAG, "Status not allowed");
                                    break;
                            }
                        }
                        setGET_MY_LEASING_FLAG(true);
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.myLeasing_Activity), getResources().getString(R.string.search_error), Snackbar.LENGTH_LONG);
                        View sbView= snackbar.getView();
                        sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed));
                        snackbar.show();
                        return;
                }
            case "GET_GARDEN":
                switch (responseCode) {
                    case 200:
                        garden = (Garden) results.get(0);
                        getLeasing(garden.getId());
                        Log.e(TAG, "results.get(0) :" + results.get(0));
                        Log.e(TAG, "garden :" + garden);
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Log.e(TAG, "receiveResults: reception du get : " + garden.toString());
                        if (leasing != null) {
                            if (leasing.getState() != null) {
                                Log.e(TAG, "Garden ID = " + garden.getId());
                                Log.i(TAG, "state : " + leasing.getState().getLeasingStatus());
                                switch (leasing.getState().getLeasingStatus()) {
                                    case "InDemand":
                                        gardensListInDemand.add(garden);
                                        okGarden++;
                                        break;
                                    case "InProgress":
                                        gardensListInProgress.add(garden);
                                        okGarden++;
                                        break;
                                    case "Finished":
                                        gardensListFinished.add(garden);
                                        okGarden++;
                                        break;
                                    default:
                                        okGarden++;
                                        break;
                                }
                            }
                        }
                        setGET_GARDEN(true);
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
        }
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public String getTag() {
        return TAG;
    }

    public void setGET_MY_LEASING_FLAG(Boolean GET_SESSION_TOKEN_flag) {
        this.GET_MY_LEASING_FLAG = GET_SESSION_TOKEN_flag;
        assessFlags();
    }

    public void setGET_GARDEN(Boolean GET_SESSION_TOKEN_flag) {
        this.GET_GARDEN = GET_SESSION_TOKEN_flag;
        assessFlags();
    }

    private void assessFlags() {
        if (GET_MY_LEASING_FLAG && GET_GARDEN && okGarden == currentSize) {

            LeasingAdapter myLeasing = new LeasingAdapter(this, getAllLeasingInProgress, gardensListInProgress);
            listViewMyLeasing.setAdapter(myLeasing);

            LeasingAdapter myRequest = new LeasingAdapter(this, getAllLeasingInDemand, gardensListInDemand);
            listViewMyRequest.setAdapter(myRequest);

            LeasingAdapter myEndedLeasing = new LeasingAdapter(this, getAllLeasingFinished, gardensListFinished);
            listViewMyEndedLeasing.setAdapter(myEndedLeasing);

            ((ProgressBar) findViewById((R.id.progress_bar_myleasing))).setVisibility(View.GONE);
        }
    }

    public void getLeasing(String id) {
        for (int i = 0; i < size; i++) {
            Leasing item = getAllLeasing.get(i);
            if (item.getGarden().equals(id)) {
                leasing = item;
                return;
            }
        }
        return;
    }
}
