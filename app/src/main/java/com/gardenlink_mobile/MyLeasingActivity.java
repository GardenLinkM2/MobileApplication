package com.gardenlink_mobile;

import android.os.Bundle;

public class MyLeasingActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myleasing_activity);

        initMenu();
    }
}
