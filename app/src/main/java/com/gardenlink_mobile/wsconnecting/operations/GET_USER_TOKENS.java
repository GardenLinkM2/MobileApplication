package com.gardenlink_mobile.wsconnecting.operations;

import com.gardenlink_mobile.IWebConnectable;
import com.gardenlink_mobile.serialization.TokensSerializer;
import com.gardenlink_mobile.wsconnecting.Caller;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class GET_USER_TOKENS extends Operation {

    private static String url = AUTHENTICATION_URL + "auth/token";
    private static TokensSerializer serializer = new TokensSerializer();

    public GET_USER_TOKENS(String email, String password) {
        criteria = new HashMap<String,Object>() {{
            put("clientId", "gardenlink");
            put("email", email);
            put("password", password);
        }};
    }

    @Override
    public void perform(WeakReference<IWebConnectable> sender){
        super.perform(sender);
        Caller.post(sender, url, serializer, criteria,this, null);
    }
}
