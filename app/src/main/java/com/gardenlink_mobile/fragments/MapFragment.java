package com.gardenlink_mobile.fragments;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.entities.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG="MapFragment";

    private GoogleMap mMap;

    private Location mGardenLocation;

    public MapFragment(Location pLocation)
    {
        mGardenLocation=pLocation;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.map_fragment, parent, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;
            Address address = new Address(Locale.getDefault());

            try {
                address=findGPSCoordinates(mGardenLocation);
                LatLng garden = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(garden).title("Localisation du Jardin"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(garden,10f));
            }catch(IOException loE)
            {
                Log.e(TAG,"Error recovering GPS location");
            }
    }

    private Address findGPSCoordinates(Location pLocation) throws IOException {
        String lLocationName = pLocation.getStreetNumber()+", "+pLocation.getStreet()+", "+pLocation.getPostalCode()+", "+pLocation.getCity();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> lAddreses = geocoder.getFromLocationName(lLocationName,1);

        return lAddreses.get(0);


    }
}
