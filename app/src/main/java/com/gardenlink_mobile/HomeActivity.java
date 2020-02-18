package com.gardenlink_mobile;

import android.os.Bundle;

public class HomeActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        initMenu();
    }
}
