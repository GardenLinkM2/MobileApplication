package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.serialization.GardenSerializer;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.serialization.TalkSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class GET_TALKS extends Operation {

    private String url = API_URL + "Talks";
    private static ISerializer serializer = new TalkSerializer();

    public GET_TALKS(){}

    @Override
    public void perform(WeakReference<IWebConnectable> sender)
    {
        super.perform(sender);
        Caller.get(sender, url, serializer, null,this, Session.getInstance().getSessionToken());

    }

}
