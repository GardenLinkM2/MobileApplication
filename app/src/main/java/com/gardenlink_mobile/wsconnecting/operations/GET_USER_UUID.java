package com.gardenlink_mobile.wsconnecting.operations;

import android.util.Log;

import com.gardenlink_mobile.IWebConnectable;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class GET_USER_UUID extends Operation {

    private static String url = AUTHENTICATION_URL + "token/introspect";
    private static ISerializer serializer = null;

    public GET_USER_UUID() {
        criteria = new HashMap<String,Object>() {{
            put("token", Session.getInstance().getUserToken());
        }};
        Log.i("GET_USER_UUID : ", this.getName());
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.post(sender, url, serializer, criteria,this, null);
    }
}
