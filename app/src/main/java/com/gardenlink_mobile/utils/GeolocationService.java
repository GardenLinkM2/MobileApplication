package com.gardenlink_mobile.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

import com.gardenlink_mobile.activities.NavigableActivity;

import java.io.IOException;
import java.util.List;

//TODO : class not used, try to refactor the geolocation or delete the class
public class GeolocationService {


    private NavigableActivity mContext;

    private LocationManager mLocationManager;

    public static final int GeolocPermission=0;

    private Address mAddress;


    public GeolocationService(NavigableActivity context, LocationManager locationManager)
    {
        mContext=context;
        mLocationManager = locationManager;
    }


    @SuppressLint("MissingPermission")
    private void findLocation() {


        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                processLocation(location);
                mLocationManager.removeUpdates(this);
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


        if(mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(mContext, "Localisation en cours...", Toast.LENGTH_SHORT).show();
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        else
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("GPS désactivé")
                    .setMessage("Votre GPS semble être désactivé, veuillez l'activer pour bénéficier des services de localisation")
                    .setPositiveButton("OK",null);

            final AlertDialog alert=builder.create();
            alert.show();

        }

    }


    public Address getGeolocation() {


        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(mContext,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    GeolocPermission);

        }
        else{
            findLocation();
        }

        return mAddress;
    }


    private void processLocation(Location location)
    {

        Geocoder geocoder = new Geocoder(mContext);
        List<Address> addreses = null;
        if(location != null) {
            try {
                addreses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            } catch (IOException loE) {
                //Log.e(TAG, "Error trying to get location");
            }
            if (addreses == null) {
                Toast.makeText(mContext, "Erreur : Aucune Localisation trouvée", Toast.LENGTH_SHORT).show();
            } else {
                mAddress = addreses.get(0);
                //TODO:add location detail to session

                Toast.makeText(mContext, "Localisation terminée !", Toast.LENGTH_SHORT).show();

            }
        }
        else{
            Toast.makeText(mContext, "Localisation indisponible", Toast.LENGTH_SHORT).show();
        }

    }



}
