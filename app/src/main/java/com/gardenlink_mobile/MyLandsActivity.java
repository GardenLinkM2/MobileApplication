package com.gardenlink_mobile;

import android.os.Bundle;

public class MyLandsActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylands_layout);

        initMenu();
    }
}
