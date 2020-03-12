package com.gardenlink_mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gardenlink_mobile.R;

public class MainActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initMenu();
    }

    public void toConnexion(View view) {
        Intent localIntentConnexion = new Intent(this, ConnectionActivity.class);
        startActivity(localIntentConnexion);
    }

    public void toResults(View view) {
        Intent lIntent = new Intent(this, SearchResultsActivity.class);
        startActivity(lIntent);
    }

}
