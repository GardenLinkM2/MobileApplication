package com.gardenlink_mobile.wsconnecting.operations;

import android.util.Log;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.serialization.UserSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class GET_USER extends Operation {

    private String url = API_URL + "Users";
    private static ISerializer serializer = new UserSerializer();

    public GET_USER(String id) {
        if (!id.isEmpty()) {
            url += "/" + id;
        }
        else {
            Log.e("OPERATION_GET_USER", "GET_USER: id is empty");
        }
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.get(sender, url, serializer, null,this, Session.getInstance().getSessionToken());
    }
}
