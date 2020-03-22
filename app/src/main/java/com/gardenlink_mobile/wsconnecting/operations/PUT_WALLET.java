package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.Wallet;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.serialization.UserSerializer;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class PUT_WALLET extends Operation {

    private String url = API_URL + "wallets";

    public PUT_WALLET(Float balance, String id) {
        url += "/" + id;
        criteria = new HashMap<String, Object>() {{
            put("balance", balance);
        }};
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.put(sender, url, criteria, this, Session.getInstance().getSessionToken());
    }
}
