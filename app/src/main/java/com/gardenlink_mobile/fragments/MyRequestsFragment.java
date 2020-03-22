package com.gardenlink_mobile.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.adapters.RequestAdapter;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.operations.GET_GARDEN;
import com.gardenlink_mobile.wsconnecting.operations.GET_MY_LEASING;
import com.gardenlink_mobile.wsconnecting.operations.GET_USER;
import com.gardenlink_mobile.wsconnecting.operations.Operation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyRequestsFragment extends Fragment implements IWebConnectable {

    private static final String TAG = "MyRequestsFrament";
    private Boolean GET_MY_LEASING_FLAG = false;
    private Boolean GET_USER = false;
    private Boolean GET_GARDEN = false;
    private User currentUser;

    private ListView listViewDemand;
    private ArrayList<Leasing> getAllLeasing;
    private ArrayList<Leasing> currentAllLeaging;
    private Leasing leasing;
    private Garden garden;
    private User renter;
    private ArrayList<User> rentersList;
    private ArrayList<Garden> gardensList;
    private int okUser = 0;
    private int okGarden = 0;
    private int currentSize = 0;

    public static MyRequestsFragment newInstance() {
        return (new MyRequestsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myrequests_fragment, container, false);
        currentUser = Session.getInstance().getCurrentUser();
        loadData();
        listViewDemand = view.findViewById(R.id.myRequestList);
        return view;
    }

    private void loadData() {
        rentersList = new ArrayList<>();
        gardensList = new ArrayList<>();
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
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        List<Leasing> leasings = (List<Leasing>) results;
                        int size = leasings.size();
                        getAllLeasing = new ArrayList<>(leasings);
                        currentAllLeaging = new ArrayList<Leasing>();
                        for (int i = 0; i < size; i++) {
                            Leasing item = leasings.get(i);
                            if (!getAllLeasing.get(i).getRenter().equals(currentUser.getId()) && item.getState().getLeasingStatus().equals("InDemand")) {
                                Log.e(TAG, "Results size : " + size);
                                leasing = item;
                                currentAllLeaging.add(leasing);
                                Log.e(TAG, "Leasing : " + leasing.toString());
                                new GET_GARDEN(leasing.getGarden()).perform(new WeakReference<>(this));
                            }
                        }
                        currentSize = currentAllLeaging.size();
                        setGET_MY_LEASING_FLAG(true);
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            case "GET_USER":
                switch (responseCode) {
                    case 200:
                        renter = (User) results.get(0);
                        rentersList.add(renter);
                        okUser++;
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Log.e(TAG, "receiveResults: reception du get : " + garden.toString());
                        setGET_USER(true);
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            case "GET_GARDEN":
                switch (responseCode) {
                    case 200:
                        garden = (Garden) results.get(0);
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Log.e(TAG, "receiveResults: reception du get : " + garden.toString());
                        if (leasing.getState() != null) {
                            switch (leasing.getState().getLeasingStatus()) {
                                case "InDemand":
                                    gardensList.add(garden);
                                    okGarden++;
                                    new GET_USER(leasing.getRenter()).perform(new WeakReference<>(this));
                                    setGET_GARDEN(true);
                                    return;
                            }
                        }
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

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    public void setGET_MY_LEASING_FLAG(Boolean GET_SESSION_TOKEN_flag) {
        this.GET_MY_LEASING_FLAG = GET_SESSION_TOKEN_flag;
        assessFlags();
    }

    public void setGET_USER(Boolean GET_SESSION_TOKEN_flag) {
        this.GET_USER = GET_SESSION_TOKEN_flag;
        assessFlags();
    }

    public void setGET_GARDEN(Boolean GET_SESSION_TOKEN_flag) {
        this.GET_GARDEN = GET_SESSION_TOKEN_flag;
        assessFlags();
    }

    private void assessFlags() {
        if (GET_MY_LEASING_FLAG && GET_GARDEN && GET_USER && okUser == currentSize && okGarden == currentSize) {
            RequestAdapter requestAdapter = new RequestAdapter(this.getContext(), currentAllLeaging, gardensList, rentersList);
            listViewDemand.setAdapter(requestAdapter);
        }
    }
}
