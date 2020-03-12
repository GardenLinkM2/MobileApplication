package com.gardenlink_mobile.utils;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Leasing;

import java.util.ArrayList;

public class MyLandsFragment extends Fragment {

    private ArrayList<Leasing> landsListAvailable;
    private ArrayList<Leasing> landsListReserved;
    private ListView listViewLandsReserved;
    private ListView listViewLandsAvailable;

    public static MyLandsFragment newInstance() {
        return (new MyLandsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mylands_fragment, container, false);

        loadDataReserved();
        listViewLandsReserved = view.findViewById(R.id.myLandsList);
        LandsAdapter landsAdapterReserved = new LandsAdapter(this.getContext(), landsListReserved);
        listViewLandsReserved.setAdapter(landsAdapterReserved);

        loadDataAvailable();
        listViewLandsAvailable = view.findViewById(R.id.myAvailableLandsList);
        LandsAdapter landsAdapterAvailable = new LandsAdapter(this.getContext(), landsListAvailable);
        listViewLandsAvailable.setAdapter(landsAdapterAvailable);

        setItemClickListenerOnLeasing();

        return view;
    }

    public void setItemClickListenerOnLeasing() {
        ((ListView) listViewLandsAvailable.findViewById(R.id.myAvailableLandsList)).setOnItemClickListener((adapterView, view, i, l) -> toPostDetail(landsListAvailable.get(i)));

        ((ListView) listViewLandsReserved.findViewById(R.id.myLandsList)).setOnItemClickListener((adapterView, view, i, l) -> toPostDetail(landsListReserved.get(i)));
    }

    private void loadDataAvailable() {
        Leasing leasing1 = new Leasing();
        Leasing leasing2 = new Leasing();

        leasing1.setId("123");
        String date = "10-01-2020";
        leasing1.setEnd(date);
        leasing2.setId("122");
        String date2 = "03-03-2020";
        leasing2.setEnd(date2);

        landsListAvailable = new ArrayList<Leasing>();
        landsListAvailable.add(leasing1);
        landsListAvailable.add(leasing2);
    }

    private void loadDataReserved() {
        Leasing leasing1 = new Leasing();
        Leasing leasing2 = new Leasing();
        Leasing leasing3 = new Leasing();

        leasing1.setId("123");
        String date = "03-03-2020";
        leasing1.setEnd(date);
        leasing2.setId("122");
        String date2 = "24-02-2020";
        leasing2.setEnd(date2);
        leasing3.setId("122");
        String date3 = "24-01-2020";
        leasing3.setEnd(date3);

        landsListReserved = new ArrayList<Leasing>();
        landsListReserved.add(leasing1);
        landsListReserved.add(leasing2);
        landsListReserved.add(leasing3);
        landsListReserved.add(leasing2);
        landsListReserved.add(leasing1);
        landsListReserved.add(leasing3);
        landsListReserved.add(leasing2);
    }

    private void toPostDetail(Leasing leasing) {
        //TODO : make real call with id in the intent to retrieve the details of the garden
        Toast.makeText(getContext(), "Vous avez cliqué sur l'annonce", Toast.LENGTH_SHORT).show();
    }

}
