package com.gardenlink_mobile.activities;

import android.os.Bundle;
import android.util.Log;

import com.gardenlink_mobile.adapters.PageAdapter;
import com.gardenlink_mobile.R;
import com.gardenlink_mobile.wsconnecting.operations.Operation;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.HashMap;
import java.util.List;

public class MyLandsActivity extends NavigableActivity implements IWebConnectable {

    private static final String TAG = "MyLandsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylands_activity);

        initMenu();
        this.configureViewPagerAndTabs();
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

    private void configureViewPagerAndTabs() {
        Bundle extras = getIntent().getExtras();
        int position = 0;
        ViewPager pager = (ViewPager) findViewById(R.id.tabPager);
        if(extras != null) {
            position = extras.getInt("viewpager_position");
        }
        pager.setAdapter(new PageAdapter(getSupportFragmentManager(), getApplicationContext()));
        TabLayout tabs = (TabLayout) findViewById(R.id.tabLayout);
        tabs.setupWithViewPager(pager);
        pager.setCurrentItem(position);

        tabs.setTabMode(TabLayout.MODE_FIXED);
    }
}