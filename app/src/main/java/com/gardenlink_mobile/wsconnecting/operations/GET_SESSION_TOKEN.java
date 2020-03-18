package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class GET_SESSION_TOKEN extends Operation {

    private String url = API_URL + "syn";
    private static ISerializer<Object> serializer = null;

    public GET_SESSION_TOKEN(String accessToken) {
        criteria = new HashMap<String, Object>() {{
            put("token", accessToken);
        }};
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.post(sender, url, serializer, criteria, this, null);
    }
}
