package com.gardenlink_mobile;

import android.os.Bundle;

public class MessagingActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messaging_activity);

        Bundle currentBundle = getIntent().getExtras();
        int id = currentBundle.getInt(CURRENT_ACTIVITY_ID);
        initMenu(id);
    }
}
