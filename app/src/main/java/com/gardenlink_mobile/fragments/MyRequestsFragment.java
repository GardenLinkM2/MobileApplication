package com.gardenlink_mobile.fragments;

import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.adapters.RequestAdapter;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.utils.ImageMaster;
import com.gardenlink_mobile.wsconnecting.operations.GET_GARDEN;
import com.gardenlink_mobile.wsconnecting.operations.GET_MY_LEASING;
import com.gardenlink_mobile.wsconnecting.operations.GET_PHOTO;
import com.gardenlink_mobile.wsconnecting.operations.GET_USER;
import com.gardenlink_mobile.wsconnecting.operations.Operation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MyRequestsFragment extends Fragment implements IWebConnectable {

    private static final String TAG = "MyRequestsFrament";

    private ListView listViewDemand;

    private ArrayList<Leasing> leasings = new ArrayList<>();

    private int usersToRetrieve;
    private int gardensToRetrieve;
    private int usersPhotosToRetrieve;
    private int gardensPhotosToRetrieve;

    private boolean gettingGardensPhotos = false;
    private boolean gettingUsersPhotos = false;

    private HashMap<String, List<Leasing>> leasingsMap = new HashMap<>();
    private HashMap<String, Garden> gardensMap = new HashMap<>();
    private HashMap<String, User> usersMap = new HashMap<>();


    public static MyRequestsFragment newInstance() {
        return (new MyRequestsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myrequests_fragment, container, false);
        new GET_MY_LEASING().perform(new WeakReference<>(this));
        listViewDemand = view.findViewById(R.id.myRequestList);

        return view;
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
                        List<Leasing> leasingsResult = (List<Leasing>) results;
                        gardensToRetrieve = 0;
                        for (Leasing leasing : leasingsResult) {
                            if (leasing.getState().getLeasingStatus().equals("InDemand") && leasing.getOwner().equals(Session.getInstance().getCurrentUser().getId())) {
                                leasings.add(leasing);
                                if (leasingsMap.get(leasing.getGarden()) != null) {
                                    leasingsMap.get(leasing.getGarden()).add(leasing);
                                } else {
                                    leasingsMap.put(leasing.getGarden(), new ArrayList<>());
                                    leasingsMap.get(leasing.getGarden()).add(leasing);
                                    gardensToRetrieve += 1;
                                    new GET_GARDEN(leasing.getGarden()).perform(new WeakReference<>(this));
                                }
                            }
                        }
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            case "GET_USER":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        User user = (User) results.get(0);
                        if (user.getPhoto() != null && !user.getPhoto().isEmpty()) {
                            usersMap.put(user.getPhoto(), user);
                        } else {
                            for (Leasing leasing : leasingsMap.get(user.getId()))
                                leasing.setRenterObject(user);
                        }
                        decrementUsersToRetrieve();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            case "GET_GARDEN":
                switch (responseCode) {
                    case 200:
                        Garden garden = (Garden) results.get(0);
                        if (garden.getPhotos() != null && !garden.getPhotos().isEmpty() && garden.getPhotos().get(0) != null) {
                            gardensMap.put(garden.getPhotos().get(0).getFileName(), garden);
                        } else {
                            for (Leasing leasing : leasingsMap.get(garden.getId()))
                                leasing.setGardenObject(garden);
                        }
                        decrementGardensToRetrieve();
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
        switch (operation.getName()) {
            case "GET_PHOTO":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        if (gettingGardensPhotos) {
                            Garden garden = (Garden) gardensMap.get(results.get("url"));
                            if (garden == null) {
                                decrementGardensPhotosToRetrieve();
                                return;
                            }
                            garden.setDrawableFirstPhoto(ImageMaster.byteStringToDrawable(results.get("photo")));
                            for (Leasing leasing : leasingsMap.get(garden.getId()))
                                leasing.setGardenObject(garden);
                            decrementGardensPhotosToRetrieve();
                        }
                        if (gettingUsersPhotos) {
                            User user = (User) usersMap.get(results.get("url"));
                            if (user == null) {
                                decrementUsersPhotosToRetrieve();
                                return;
                            }
                            user.setDrawablePhoto(ImageMaster.byteStringToDrawable(results.get("photo")));
                            for (Leasing leasing : leasingsMap.get(user.getId()))
                                leasing.setRenterObject(user);
                            decrementUsersPhotosToRetrieve();
                        }
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        if (gettingGardensPhotos) decrementGardensPhotosToRetrieve();
                        if (gettingGardensPhotos) decrementUsersPhotosToRetrieve();
                        return;
                }

            default:
                Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
                return;
        }
    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    private void decrementGardensToRetrieve() {
        gardensToRetrieve -= 1;
        if (gardensToRetrieve == 0) {
            gettingGardensPhotos = true;
            gardensPhotosToRetrieve = gardensMap.size();
            if (gardensPhotosToRetrieve > 0) {
                gardensMap.forEach((k, v) -> {
                    new GET_PHOTO(k).perform(new WeakReference<>(this));
                });
            }
            else {
                gardensPhotosToRetrieve = 1;
                decrementGardensPhotosToRetrieve();
            }
        }
    }

    private void decrementGardensPhotosToRetrieve() {
        gardensPhotosToRetrieve -= 1;
        if (gardensPhotosToRetrieve == 0) {
            leasings = new ArrayList<>();
            for(List<Leasing> leasingsList : leasingsMap.values()) {
                for (Leasing leasing : leasingsList) {
                    leasings.add(leasing);
                }
            }
            leasingsMap = new HashMap<>();
            usersToRetrieve = 0;
            for (Leasing leasing : leasings) {
                if (leasingsMap.get(leasing.getRenter()) != null) {
                    leasingsMap.get(leasing.getRenter()).add(leasing);
                } else {
                    leasingsMap.put(leasing.getRenter(), new ArrayList<>());
                    leasingsMap.get(leasing.getRenter()).add(leasing);
                    usersToRetrieve += 1;
                    new GET_USER(leasing.getRenter()).perform(new WeakReference<>(this));
                }
            }
        }
    }

    private void decrementUsersToRetrieve() {
        usersToRetrieve -= 1;
        if (usersToRetrieve == 0) {
            gettingGardensPhotos = false;
            gettingUsersPhotos = true;
            usersPhotosToRetrieve = usersMap.size();
            if (usersPhotosToRetrieve > 0) {
                usersMap.forEach((k, v) -> {
                    new GET_PHOTO(k).perform(new WeakReference<>(this));
                });
            } else {
                usersPhotosToRetrieve = 1;
                decrementUsersPhotosToRetrieve();
            }
        }
    }


    private void decrementUsersPhotosToRetrieve() {
        usersPhotosToRetrieve -= 1;
        if (usersPhotosToRetrieve == 0) {
            leasings.clear();
            for(List<Leasing> leasingsList : leasingsMap.values())
                for(Leasing leasing : leasingsList)
                    leasings.add(leasing);
            ArrayList<Leasing> leasingsForAdapter = new ArrayList<>();
            ArrayList<Garden> gardensForAdapter = new ArrayList<>();
            ArrayList<User> usersForAdapter = new ArrayList<>();
            for(Leasing leasing : leasings){
                leasingsForAdapter.add(leasing);
                gardensForAdapter.add(leasing.getGardenObject());
                usersForAdapter.add(leasing.getRenterObject());
            }
            RequestAdapter requestAdapter = new RequestAdapter(this.getContext(), leasingsForAdapter, gardensForAdapter, usersForAdapter);
            listViewDemand.setAdapter(requestAdapter);
        }
    }
}
