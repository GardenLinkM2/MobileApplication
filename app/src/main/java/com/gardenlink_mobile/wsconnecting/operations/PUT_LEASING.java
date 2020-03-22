package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.Leasing;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class PUT_LEASING extends Operation {

    private String url = API_URL + "Leasing";

    public PUT_LEASING(Leasing leasing, String id) {
        url += "/" + id;
        criteria = new HashMap<String, Object>() {{
            put("state", leasing.getState().getLeasingStatus());
        }};
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.put(sender, url, criteria, this, Session.getInstance().getSessionToken());
    }
}