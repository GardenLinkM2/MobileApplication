package com.gardenlink_mobile;

import android.os.Bundle;

public class MyAccountActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount_activity);

        initMenu();
    }
}
