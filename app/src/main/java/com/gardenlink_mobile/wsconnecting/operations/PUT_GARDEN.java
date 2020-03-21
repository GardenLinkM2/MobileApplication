package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class PUT_GARDEN extends Operation {

    private String url = API_URL + "Gardens";

    public PUT_GARDEN(Garden garden, String id) {
        url += "/" + id;
        criteria = new HashMap<String, Object>() {{
            put("isReserved", garden.getIsReserved());
        }};
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.put(sender, url, criteria, this, Session.getInstance().getSessionToken());
    }
}