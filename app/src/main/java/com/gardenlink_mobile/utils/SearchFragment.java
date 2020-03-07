package com.gardenlink_mobile.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import com.gardenlink_mobile.R;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG="SearchFragment";

    private View mView;

    private CriteriaFragment mCriteria;

    private Animation slideDown;
    private Animation slideUp;

    private TextInputLayout searchInputLayout;

    private boolean isFragmentShow = false;

    private LocationManager locationManager = null;

    public static final int GeolocPermission=0;

    private RotateAnimation rotateOpen;

    private RotateAnimation rotateClose;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, parent, false);
        mView= view;

        View.OnClickListener criteriasListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCriterias(view);
            }
        };

        view.findViewById(R.id.criteriaArrow).setOnClickListener(criteriasListener);
        view.findViewById(R.id.criteriaText).setOnClickListener(criteriasListener);

        mCriteria = new CriteriaFragment();

        FragmentTransaction ft = getChildFragmentManager().beginTransaction();

        ft.replace(R.id.criteriaFragment, mCriteria);

        ft.hide(mCriteria);

        ft.commit();


        View lFragment = view.findViewById(R.id.criteriaFragment);

        lFragment.setClipToOutline(true);

        slideDown = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_down);
        slideUp = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.slide_up);


        initSearch();

        if (locationManager == null) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        }

        rotateOpen = new RotateAnimation(0, -90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateOpen.setDuration(300);
        rotateOpen.setInterpolator(new LinearInterpolator());
        rotateOpen.setFillAfter(true);


        rotateClose = new RotateAnimation(-90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateClose.setDuration(300);
        rotateClose.setInterpolator(new LinearInterpolator());
        rotateClose.setFillAfter(true);



        return view;
    }


    private void initSearch() {
        searchInputLayout = mView.findViewById(R.id.searchTextInputLayout);

        searchInputLayout.setEndIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO : make call to research

            }
        });

        searchInputLayout.setStartIconOnClickListener(new View.OnClickListener() {
                                                          @Override
                                                          public void onClick(View view) {

                                                              getGeolocation();
                                                          }
                                                      }

        );

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GeolocPermission: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    findLocation();

                }

                return;
            }

        }
    }

    @SuppressLint("MissingPermission")
    private void findLocation() {


        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                processLocation(location);
                locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };


        if (locationManager == null) {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        }

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getActivity().getApplicationContext(), "Localisation en cours...", Toast.LENGTH_SHORT).show();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("GPS désactivé")
                    .setMessage("Votre GPS semble être désactivé, veuillez l'activer pour bénéficier des services de localisation")
                    .setPositiveButton("OK",null);

            final AlertDialog alert=builder.create();
            alert.show();

        }

    }


    private void getGeolocation() {


        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    GeolocPermission);

        }
        else{
            findLocation();
        }


    }


    private void processLocation(Location location)
    {

        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addreses = null;
        if(location != null) {
            try {
                addreses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            } catch (IOException loE) {
               Log.e(TAG, "Error trying to get location");
            }
            if (addreses == null) {
                Toast.makeText(getActivity().getApplicationContext(), "Erreur : Aucune Localisation trouvée", Toast.LENGTH_SHORT).show();
            } else {
                mCriteria.setCityField(addreses.get(0).getLocality());
                mCriteria.setPostCodeField(addreses.get(0).getPostalCode());
                mCriteria.setStreetNameField(addreses.get(0).getThoroughfare());
                mCriteria.setStreetNumberField(addreses.get(0).getSubThoroughfare());
                //TODO:add location detail to session

                Toast.makeText(getActivity().getApplicationContext(), "Localisation terminée !", Toast.LENGTH_SHORT).show();

            }
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Localisation indisponible", Toast.LENGTH_SHORT).show();
        }

    }


    public void toggleCriterias(View view) {
        FragmentManager lF = getChildFragmentManager();

        if (isFragmentShow) {

            lF.beginTransaction().setCustomAnimations(android.R.anim.fade_in, R.anim.slide_up).hide(mCriteria).commit();
            mView.findViewById(R.id.criteriaFragment).startAnimation(slideUp);
            mView.findViewById(R.id.criteriaArrow).startAnimation(rotateClose);
            isFragmentShow = false;
        } else {
            mView.findViewById(R.id.criteriaFragment).startAnimation(slideDown);
            lF.beginTransaction().setCustomAnimations(android.R.anim.fade_in, R.anim.slide_up).show(mCriteria).commit();
            mView.findViewById(R.id.criteriaArrow).startAnimation(rotateOpen);

            isFragmentShow = true;
        }

    }





    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

    }
}
