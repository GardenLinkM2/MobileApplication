package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Tokens;

import org.json.JSONException;
import org.json.JSONObject;

public class TokensSerializer implements ISerializer<Tokens> {

    @Override
    public Tokens deserialize(JSONObject input) throws JSONException {
        Tokens tokens = new Tokens();
        tokens.setAccessToken(input.getString("access_token"));
        tokens.setUserToken(input.getString("user_token"));
        return tokens;
    }

    @Override
    public Tokens[] deserializeMany(JSONObject[] input) {
        return new Tokens[0];
    }

    @Override
    public JSONObject serialize(Tokens input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Tokens[] input) {
        return new JSONObject[0];
    }
}
