package com.gardenlink_mobile.activities;

import android.os.Bundle;
import android.util.Log;

import com.gardenlink_mobile.R;
import com.gardenlink_mobile.wsconnecting.operations.Operation;

import java.util.HashMap;
import java.util.List;

public class WalletActivity extends NavigableActivity implements IWebConnectable {

    private static final String TAG = "WalletActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wallet_activity);

        initMenu();
    }

    @Override
    public <T> void receiveResults(int responseCode, List<T> results, Operation operation) {
        Log.e(TAG,"Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, HashMap<String, String> results, Operation operation) {
        Log.e(TAG,"Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public void receiveResults(int responseCode, Operation operation) {
        Log.e(TAG, "Received results from uninmplemented operation " + operation.getName() + " with response code " + responseCode);
    }

    @Override
    public String getTag() {
        return TAG;
    }
}
