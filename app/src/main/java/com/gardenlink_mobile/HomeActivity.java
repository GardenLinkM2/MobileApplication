package com.gardenlink_mobile;

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
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gardenlink_mobile.utils.CriteriaFragment;
import com.gardenlink_mobile.utils.GeolocationService;
import com.gardenlink_mobile.utils.SearchFragment;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.List;

public class HomeActivity extends NavigableActivity {

    private static final String TAG = "HomeActivity";

    private SearchFragment mSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        mSearch = new SearchFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.searchFragment, mSearch);

        ft.show(mSearch);

        ft.commit();


        initMenu();

        View lFragment = findViewById(R.id.searchFragment);

        lFragment.setClipToOutline(true);

    }



    public void toPost(View view)
    {
        //TODO : make the call to post
    }
}
