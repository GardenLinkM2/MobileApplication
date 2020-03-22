package com.gardenlink_mobile.fragments;

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
import androidx.core.content.ContextCompat;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gardenlink_mobile.activities.HomeActivity;
import com.gardenlink_mobile.R;
import com.gardenlink_mobile.activities.SearchResultsActivity;
import com.gardenlink_mobile.entities.Criteria;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String TAG = "SearchFragment";
    public static final String SEARCH_FIELD_CONTENT = "searchName";
    public static final String CRITERIA_CONTENT = "criterias";
    public static final String LOCATION_CONTENT = "location";
    public static final String MIN_AREA_CONTENT = "minArea";
    public static final String MAX_AREA_CONTENT = "maxArea";
    public static final String MIN_DURATION_CONTENT = "minDuration";
    public static final String MAX_DURATION_CONTENT = "maxDuration";
    public static final String MIN_PRICE_CONTENT = "minPrice";
    public static final String MAX_PRICE_CONTENT = "maxPrice";
    public static final int GeolocPermission = 0;

    private View mView;
    private CriteriaFragment mCriteria;
    private Animation slideDown;
    private Animation slideUp;
    private TextInputLayout searchInputLayout;
    private boolean isFragmentShow = false;
    private LocationManager locationManager = null;
    private RotateAnimation rotateOpen;
    private RotateAnimation rotateClose;
    private Integer mCriteriaColor = null;
    private boolean mIsOnResult = false;
    private ProgressBar progressBar;

    public CriteriaFragment getmCriteria() {
        return mCriteria;
    }

    public SearchFragment() {
        super();
        mCriteria = new CriteriaFragment();
    }

    public SearchFragment(int color, boolean pIsOnResult) {
        super();
        mCriteriaColor = color;
        mIsOnResult = pIsOnResult;
        mCriteria = new CriteriaFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.search_fragment, parent, false);
        mView = view;

        progressBar = getActivity().findViewById(R.id.progress_bar);
        View.OnClickListener criteriasListener = view1 -> toggleCriterias(view1);
        view.findViewById(R.id.criteriaArrow).setOnClickListener(criteriasListener);
        view.findViewById(R.id.criteriaText).setOnClickListener(criteriasListener);
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.criteriaFragment, mCriteria).hide(mCriteria).commit();

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

        if (mCriteriaColor != null) {
            ((TextView) mView.findViewById(R.id.criteriaText)).setTextColor(mCriteriaColor);
        }
        return view;
    }

    private void initSearch() {
        searchInputLayout = mView.findViewById(R.id.searchTextInputLayout);
        searchInputLayout.setEndIconOnClickListener(view -> {
            String lSearchName = ((TextInputEditText) mView.findViewById(R.id.searchField)).getText().toString();
            if (mIsOnResult) {
                progressBar.setVisibility(View.VISIBLE);
                ((SearchResultsActivity) getActivity()).setmSearchTitle(lSearchName);
                ((SearchResultsActivity) getActivity()).loadData();
            } else {
                ((HomeActivity) getActivity()).toSearchResult(lSearchName);
            }
        });

        searchInputLayout.setStartIconOnClickListener(view -> getGeolocation()
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case GeolocPermission: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
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

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Snackbar snackbar;
            try {
                snackbar = Snackbar.make(getActivity().findViewById(R.id.resultPage), getResources().getString(R.string.location_in_progress), Snackbar.LENGTH_LONG);
            } catch (Exception e) {
                snackbar = Snackbar.make(getActivity().findViewById(R.id.home_Activity), getResources().getString(R.string.location_in_progress), Snackbar.LENGTH_LONG);
            }
            View sbView= snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorBlue_accueil_postButton));
            snackbar.show();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(getResources().getString(R.string.gps_off))
                    .setMessage(getResources().getString(R.string.gps_off_message))
                    .setPositiveButton(getResources().getString(R.string.ok), null);

            final AlertDialog alert = builder.create();
            alert.show();
        }
    }

    private void getGeolocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    GeolocPermission);
        } else {
            findLocation();
        }
    }

    private void processLocation(Location location) {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addreses = null;
        if (location != null) {
            try {
                addreses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            } catch (IOException loE) {
                Log.e(TAG, "Error trying to get location");
            }
            if (addreses == null) {
                Snackbar snackbar;
                try {
                    snackbar = Snackbar.make(getActivity().findViewById(R.id.resultPage), getResources().getString(R.string.location_succed), Snackbar.LENGTH_LONG);
                } catch (Exception e) {
                    snackbar = Snackbar.make(getActivity().findViewById(R.id.home_Activity), getResources().getString(R.string.location_succed), Snackbar.LENGTH_LONG);
                }
                View sbView= snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
                snackbar.show();
            } else {
                mCriteria.setCityField(addreses.get(0).getLocality());
                mCriteria.setPostCodeField(addreses.get(0).getPostalCode());
                mCriteria.setStreetNameField(addreses.get(0).getThoroughfare());
                mCriteria.setStreetNumberField(addreses.get(0).getSubThoroughfare());
                //TODO:add location detail to session
                Snackbar snackbar;
                try {
                    snackbar = Snackbar.make(getActivity().findViewById(R.id.resultPage), getResources().getString(R.string.location_succed), Snackbar.LENGTH_LONG);
                } catch (Exception e) {
                    snackbar = Snackbar.make(getActivity().findViewById(R.id.home_Activity), getResources().getString(R.string.location_succed), Snackbar.LENGTH_LONG);
                }
                View sbView= snackbar.getView();
                sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorGreen_snackbar));
                snackbar.show();
            }
        } else {
            Snackbar snackbar;
            try {
                snackbar = Snackbar.make(getActivity().findViewById(R.id.resultPage), getResources().getString(R.string.unavailable), Snackbar.LENGTH_LONG);
            } catch (Exception e) {
                snackbar = Snackbar.make(getActivity().findViewById(R.id.home_Activity), getResources().getString(R.string.unavailable), Snackbar.LENGTH_LONG);
            }
            View sbView= snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorRed));
            snackbar.show();
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

    public void setSearchInput(final String pInput) {
        ((TextInputEditText) mView.findViewById(R.id.searchField)).setText(pInput);
    }

    public Criteria getCriteria() {
        Criteria lCriteria = new Criteria();
        lCriteria.setDirectAccess(mCriteria.getDirectAccess());
        lCriteria.setEquipments(mCriteria.getEquipmentProvided());
        lCriteria.setOrientation(mCriteria.getOrientation());
        lCriteria.setTypeOfClay(mCriteria.getSoilType());
        lCriteria.setWaterAccess(mCriteria.getWaterProvided());
        return lCriteria;
    }

    public com.gardenlink_mobile.entities.Location getLocation() {
        com.gardenlink_mobile.entities.Location lLocation = new com.gardenlink_mobile.entities.Location();
        lLocation.setStreet(mCriteria.getStreetName());
        lLocation.setCity(mCriteria.getCity());
        lLocation.setPostalCode(mCriteria.getPostalCode());
        lLocation.setStreetNumber(mCriteria.getStreetNumber());
        return lLocation;
    }
}
