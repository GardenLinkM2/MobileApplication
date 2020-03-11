package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Token;

import org.json.JSONObject;

public class TokenSerializer implements ISerializer<Token> {

    @Override
    public Token deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Token[] deserializeMany(JSONObject[] input) {
        return new Token[0];
    }

    @Override
    public JSONObject serialize(Token input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Token[] input) {
        return new JSONObject[0];
    }
}
