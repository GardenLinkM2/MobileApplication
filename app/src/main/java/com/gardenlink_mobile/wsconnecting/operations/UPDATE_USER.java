package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.session.Session;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class UPDATE_USER extends Operation {

    private String url = AUTHENTICATION_URL + "users";

    public UPDATE_USER(User user) {
        url += "/" + Session.getInstance().getUuid();
        criteria = new HashMap<String, Object>() {{
            put("password", user.getPassword());
            put("id", Session.getInstance().getUuid());
            put("firstName", user.getFirstName());
            put("lastName", user.getLastName());
            put("email", user.getEmail());
            put("avatar", user.getAvatar());
            put("phone", user.getPhone());
            put("newsletter", user.getNewsletter());
        }};
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender) {
        super.perform(sender);
        Caller.put(sender, url, criteria, this, Session.getInstance().getUserToken());
    }
}
