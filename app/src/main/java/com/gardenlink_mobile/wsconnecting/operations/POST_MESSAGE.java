package com.gardenlink_mobile.wsconnecting.operations;

import android.util.Log;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.Message;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.serialization.MessageSerializer;
import com.gardenlink_mobile.serialization.UserSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class POST_MESSAGE extends Operation {
    private String url = API_URL + "Talks";
    private static ISerializer serializer = new MessageSerializer();
    private Object argument;

    public POST_MESSAGE(String id, Message pArgument) {
        if (!id.isEmpty()) {
            url += "/" + id;
            argument=pArgument;
        }
        else {
            Log.e("OPERATION_POST_MESSAGE", "POST_MESSAGE: id is empty");
        }
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.post(sender, url, serializer, argument,this, Session.getInstance().getSessionToken());
    }
}
