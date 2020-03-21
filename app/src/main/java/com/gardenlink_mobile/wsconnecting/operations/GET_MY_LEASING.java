package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.serialization.LeasingSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class GET_MY_LEASING extends Operation {

    private String url = API_URL + "Leasing/me";
    private static LeasingSerializer serializer = new LeasingSerializer();

    public GET_MY_LEASING(){}

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.get(sender, url, serializer, null, this, Session.getInstance().getSessionToken());
    }
}

