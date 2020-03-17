package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class DELETE_ACCOUNT extends Operation {

    private String url = AUTHENTICATION_URL + "users";

    public DELETE_ACCOUNT() {
        url += "/" + Session.getInstance().getUuid();
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.delete(sender, url, null, this, null);
    }
}
