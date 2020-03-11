package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Tokens;

import org.json.JSONException;
import org.json.JSONObject;

public class TokensSerializer implements ISerializer<Tokens> {

    @Override
    public Tokens deserialize(JSONObject input) throws JSONException {
        Tokens tokens = new Tokens();
        tokens.setAccessToken(input.optString("access_token"));
        tokens.setUserToken(input.optString("user_token"));
        return tokens;
    }

    // Tokens are never sent
    @Override
    public JSONObject serialize(Tokens input) {
        return null;
    }
}
