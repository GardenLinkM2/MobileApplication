package com.gardenlink_mobile;

import android.os.Bundle;

public class MainActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        initMenu();
    }
}
