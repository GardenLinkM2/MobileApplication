package com.gardenlink_mobile.wsconnecting.operations;

import android.util.Log;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.GardenODataQueryOptions;
import com.gardenlink_mobile.serialization.GardenSerializer;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class GET_GARDEN extends Operation {

    private String url = API_URL + "gardens";
    private static ISerializer serializer = new GardenSerializer();

    public GET_GARDEN(String id) {
        if (!id.isEmpty()) {
            url += "/" + id;
        }
        else {
            Log.e("OPERATION_GET_GARDEN", "GET_GARDEN: id is empty");
        }
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.get(sender, url, serializer, null,this, Session.getInstance().getSessionToken());
    }
}
