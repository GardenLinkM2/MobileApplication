package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.IWebConnectable;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class DELETE_SELF_API extends Operation {

    private static String url = API_URL + "Users/me";

    public DELETE_SELF_API(){}

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.delete(sender, url, criteria,this, Session.getInstance().getSessionToken());
    }
}
