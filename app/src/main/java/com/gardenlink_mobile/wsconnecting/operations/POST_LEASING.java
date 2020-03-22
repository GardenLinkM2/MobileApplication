package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.serialization.LeasingSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class POST_LEASING extends Operation {

    private String url = API_URL + "Leasing";
    private static LeasingSerializer serializer = new LeasingSerializer();
    private Object argument;

    public POST_LEASING(Leasing leasing) {
        argument = leasing;
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.post(sender, url, serializer, argument, this, Session.getInstance().getSessionToken());
    }
}