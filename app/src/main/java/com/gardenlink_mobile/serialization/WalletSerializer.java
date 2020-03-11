package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Wallet;

import org.json.JSONObject;

public class WalletSerializer implements ISerializer<Wallet> {

    @Override
    public Wallet deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Wallet[] deserializeMany(JSONObject[] input) {
        return new Wallet[0];
    }

    @Override
    public JSONObject serialize(Wallet input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Wallet[] input) {
        return new JSONObject[0];
    }
}
