package com.gardenlink_mobile.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.adapters.RequestAdapteur;

import java.util.ArrayList;

public class MyRequestsFragment extends Fragment {

    private ArrayList<Leasing> demandList;
    private ListView listViewDemand;

    public static MyRequestsFragment newInstance() {
        return (new MyRequestsFragment());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.myrequests_fragment, container, false);
        loadData();
        listViewDemand = view.findViewById(R.id.myRequestList);
        RequestAdapteur requestAdapter = new RequestAdapteur(this.getContext(), demandList);
        listViewDemand.setAdapter(requestAdapter);
        return view;
    }

    private void loadData() {
        Leasing leasing1 = new Leasing();
        Leasing leasing2 = new Leasing();

        String date = "10-01-2020";
        leasing1.setId("123");
        leasing1.setEnd(date);
        String date2 = "03-03-2020";
        leasing2.setId("122");
        leasing2.setEnd(date2);

        demandList = new ArrayList<Leasing>();
        demandList.add(leasing1);
        demandList.add(leasing2);
        demandList.add(leasing1);
        demandList.add(leasing2);
        demandList.add(leasing1);
        demandList.add(leasing2);
    }
}
