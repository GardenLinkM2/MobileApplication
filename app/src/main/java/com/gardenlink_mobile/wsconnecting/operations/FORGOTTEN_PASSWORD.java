package com.gardenlink_mobile.wsconnecting.operations;

import android.util.Log;

import com.gardenlink_mobile.IWebConnectable;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.serialization.UserSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class FORGOTTEN_PASSWORD extends Operation {

    private String url = AUTHENTICATION_URL + "lostpassword";

    public FORGOTTEN_PASSWORD(String email) {
        url += "/" + email;
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.get(sender, url, null, null,this, null);
    }
}
