package com.gardenlink_mobile.wsconnecting.operations;

import android.util.Log;

import com.gardenlink_mobile.IWebConnectable;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.serialization.UserSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class GET_USER_ME extends Operation {

    private static String url = AUTHENTICATION_URL + "users/me";
    private static ISerializer serializer = new UserSerializer();

    public GET_USER_ME() {
        Log.i("GET_USER_ME : ", this.getName());
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.get(sender, url, serializer, null,this, Session.getInstance().getUserToken());
    }
}
