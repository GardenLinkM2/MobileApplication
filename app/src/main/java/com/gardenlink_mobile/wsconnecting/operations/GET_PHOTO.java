package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class GET_PHOTO extends Operation {

    private String url;

    public GET_PHOTO(String url) {
        this.url = url;
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.get(sender, url, this);
    }
}
