package com.gardenlink_mobile.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.adapters.LandAdapter;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.adapters.LeasingAdapter;
import com.gardenlink_mobile.utils.ImageMaster;
import com.gardenlink_mobile.wsconnecting.operations.GET_GARDEN;
import com.gardenlink_mobile.wsconnecting.operations.GET_PHOTO;
import com.gardenlink_mobile.wsconnecting.operations.GET_USER_ME_GARDENS;
import com.gardenlink_mobile.wsconnecting.operations.Operation;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MyLandsFragment extends Fragment implements IWebConnectable {

    private static final String TAG = "MyLandsFrament";

    private ArrayList<Leasing> landsListAvailable;
    private ArrayList<Leasing> landsListReserved;
    private ListView listViewLandsReserved;
    private ListView listViewLandsAvailable;
    private ArrayList<Leasing> getAllLeasing;
    private ArrayList<Garden> getAllMyGardens;
    private Garden garden;
    private Leasing leasing;
    private int size;
    private HashMap<String, Garden> gardensMap;
    private int photosToRetrieve;

    public static MyLandsFragment newInstance() {
        return (new MyLandsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mylands_fragment, container, false);

        getMyLeasings();
        listViewLandsReserved = view.findViewById(R.id.myLandsList);

        listViewLandsAvailable = view.findViewById(R.id.myAvailableLandsList);
        return view;
    }

    private void getMyLeasings() {
        new GET_USER_ME_GARDENS().perform(new WeakReference<>(this));
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        switch (operation.getName()) {
            case "GET_MY_LEASING":
                switch (responseCode) {
                    case 200:
                        if (results == null) {
                            Log.w(TAG, "Operation " + operation.getName() + " completed successfully with empty results.");
                            Toast.makeText(getContext(), getResources().getString(R.string.no_result), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        List<Leasing> leasings = (List<Leasing>) results;
                        getAllLeasing = new ArrayList<>(leasings);
                        size = results.size();
                        Log.e(TAG, "Results size : " + size);
                        for (int i = 0; i < size; i++) {
                            leasing = getAllLeasing.get(i);
                            Log.e(TAG, "LEasing : " + leasing.getState().toString());
                            new GET_GARDEN(leasing.getGarden()).perform(new WeakReference<>(this));
                        }
                        new GET_USER_ME_GARDENS().perform(new WeakReference<>(this));
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
                            Log.e(TAG, "Garden ID = " + garden.getId());
                            switch (leasing.getState().getLeasingStatus()) {
                                case "InProgress":
                                    LeasingAdapter leasingAdapterReserved = new LeasingAdapter(this.getContext(), getAllLeasing, garden);
                                    listViewLandsReserved.setAdapter(leasingAdapterReserved);
                                    return;
                                default:
                                    return;
                            }
                        }
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        return;
                }
            case "GET_USER_ME_GARDENS":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        List<Garden> gardens = (List<Garden>) results;
                        getAllMyGardens = new ArrayList<>(gardens);
                        size = results.size();
                        photosToRetrieve = size;
                        Log.e(TAG, "Results size : " + size);
                        gardensMap = new HashMap<>();
                        for (Garden garden : getAllMyGardens) {
                            try {
                                String photoUrl = garden.getPhotos().get(0).getFileName();
                                gardensMap.put(photoUrl, garden);
                                new GET_PHOTO(photoUrl).perform(new WeakReference<>(this));
                            } catch (NullPointerException e) {
                                gardensMap.put(UUID.randomUUID().toString(), garden);
                                decrementPhotosToRetrieve();
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
        switch (operation.getName()) {
            case "GET_PHOTO":
                switch (responseCode) {
                    case 200:
                        Log.i(TAG, "Operation " + operation.getName() + " completed successfully.");
                        Garden garden = (Garden) gardensMap.get(results.get("url"));
                        garden.setDrawableFirstPhoto(ImageMaster.byteStringToDrawable(results.get("photo")));
                        decrementPhotosToRetrieve();
                        return;
                    default:
                        Log.e(TAG, "Operation " + operation.getName() + " failed with response code " + responseCode);
                        decrementPhotosToRetrieve();
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

    private void decrementPhotosToRetrieve() {
        photosToRetrieve -= 1;
        if (photosToRetrieve == 0) {
            Log.i(TAG, "All photos retrieved");
            getAllMyGardens = new ArrayList<>();
            gardensMap.forEach((k, v) -> {
                getAllMyGardens.add(v);
            });
            ArrayList<Garden> listGardenReserved = new ArrayList<Garden>();
            ArrayList<Garden> listGardenAvailable = new ArrayList<Garden>();
            for (int i = 0; i < size; i++) {
                garden = getAllMyGardens.get(i);
                if (!garden.getIsReserved()) {
                    listGardenAvailable.add(garden);
                    Log.i(TAG, "Garden Not Reserved: " + garden.getName() + " ID : " + garden.getId());
                } else {
                    listGardenReserved.add(garden);
                    Log.i(TAG, "Garden Reserved : " + garden.getName() + " ID : " + garden.getId());
                }
            }
            LandAdapter landAdapterAvailable = new LandAdapter(this.getContext(), listGardenAvailable);
            listViewLandsAvailable.setAdapter(landAdapterAvailable);
            LandAdapter landAdapterReserved = new LandAdapter(this.getContext(), listGardenReserved);
            listViewLandsReserved.setAdapter(landAdapterReserved);
        }
    }
}
