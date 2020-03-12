package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.serialization.GardenSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class POST_GARDEN extends Operation {

    private String url = API_URL + "Gardens";
    private static GardenSerializer serializer = new GardenSerializer();
    private Object argument;

    public POST_GARDEN(Garden garden) {
        argument = garden;
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.post(sender, url, serializer, argument,this, Session.getInstance().getSessionToken());
    }
}
