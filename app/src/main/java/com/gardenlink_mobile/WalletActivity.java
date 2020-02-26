package com.gardenlink_mobile;

import android.os.Bundle;

public class WalletActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity);

        Bundle currentBundle = getIntent().getExtras();
        int id = currentBundle.getInt(CURRENT_ACTIVITY_ID);
        initMenu(id);
    }
}
