package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.Talk;
import com.gardenlink_mobile.serialization.GardenSerializer;
import com.gardenlink_mobile.serialization.TalkSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class POST_TALK extends Operation {

    private String url = API_URL + "Talks";
    private static TalkSerializer serializer = new TalkSerializer();
    private Object argument;

    public POST_TALK(Talk pTalk)
    {
        argument=pTalk;
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.post(sender,url,serializer,argument,this, Session.getInstance().getSessionToken());

    }

}
