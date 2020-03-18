package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class CREATE_USER extends Operation {

    private String url = AUTHENTICATION_URL + "users";
    private static ISerializer serializer = null;

    public CREATE_USER(String password, String firstname, String lastname, String phone, String email, String avatar, Boolean newsletter) {
        criteria = new HashMap<String, Object>() {{
            put("password", password);
            put("firstName", firstname);
            put("lastName", lastname);
            put("phone", phone);
            put("email", email);
            put("avatar", avatar);
            put("newsletter", newsletter);
        }};
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.post(sender, url, serializer, criteria, this, null);
    }
}
