package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;

public class FORGOTTEN_PASSWORD extends Operation {

    private String url = AUTHENTICATION_URL + "lostpassword";

    public FORGOTTEN_PASSWORD(String email) {
        url += "/" + email;
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.get(sender, url, null, null, this, null);
    }
}
