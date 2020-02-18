package com.gardenlink_mobile;

import android.os.Bundle;

public class MessagingActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging_layout);

        initMenu();
    }
}
