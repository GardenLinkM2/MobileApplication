package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Wallet;

import org.json.JSONException;
import org.json.JSONObject;

public class WalletSerializer implements ISerializer<Wallet> {

    @Override
    public Wallet deserialize(JSONObject input) {
        Wallet wallet = new Wallet();
        wallet.setBalance((float) input.optDouble("balance"));
        return wallet;
    }

    @Override
    public JSONObject serialize(Wallet input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("wallet", input.getBalance());
        return output;
    }
}
