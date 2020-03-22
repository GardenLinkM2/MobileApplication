package com.gardenlink_mobile.wsconnecting.operations;

import android.util.Log;

import com.gardenlink_mobile.BuildConfig;
import com.gardenlink_mobile.activities.IWebConnectable;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public abstract class Operation {

    protected static final String API_URL = BuildConfig.API_URL;
    protected static final String AUTHENTICATION_URL = BuildConfig.AUTHENTICATION_URL;
    protected static HashMap<String, Object> criteria = null;

    public void perform(WeakReference<IWebConnectable> sender) {
        Log.i(sender.get().getTag(), "Invoking operation " + this.getName());
    }

    public String getName() {
        String[] splitName = this.getClass().toString().split("\\.");
        return splitName[splitName.length - 1];
    }
}
