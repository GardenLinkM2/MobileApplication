package com.gardenlink_mobile;

import android.os.Bundle;

public class WalletActivity extends NavigableActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity);

        initMenu();
    }
}
