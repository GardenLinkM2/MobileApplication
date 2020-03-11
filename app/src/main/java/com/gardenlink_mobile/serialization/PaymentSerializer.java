package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Payment;

import org.json.JSONObject;

public class PaymentSerializer implements ISerializer<Payment> {

    @Override
    public Payment deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Payment[] deserializeMany(JSONObject[] input) {
        return new Payment[0];
    }

    @Override
    public JSONObject serialize(Payment input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Payment[] input) {
        return new JSONObject[0];
    }
}
