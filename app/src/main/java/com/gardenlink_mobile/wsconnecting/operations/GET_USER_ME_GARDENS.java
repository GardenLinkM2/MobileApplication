package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.serialization.GardenSerializer;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class GET_USER_ME_GARDENS extends Operation {

    private String url = API_URL + "Users/me/gardens";
    private static ISerializer serializer = new GardenSerializer();

    public GET_USER_ME_GARDENS(){}

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.get(sender, url, serializer, null,this, Session.getInstance().getSessionToken());
    }
}