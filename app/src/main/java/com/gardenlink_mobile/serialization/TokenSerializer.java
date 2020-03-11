package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Token;

import org.json.JSONException;
import org.json.JSONObject;

public class TokenSerializer implements ISerializer<Token> {

    @Override
    public Token deserialize(JSONObject input) {
        Token token = new Token();
        token.setToken(input.optString("token"));
        return token;
    }

    @Override
    public JSONObject serialize(Token input) throws JSONException {
        JSONObject output = new JSONObject();
        output.putOpt("token", input.getToken());
        return output;
    }
}
