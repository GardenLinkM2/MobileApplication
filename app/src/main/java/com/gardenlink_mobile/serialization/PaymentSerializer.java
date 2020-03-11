package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Payment;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentSerializer implements ISerializer<Payment> {

    @Override
    public Payment deserialize(JSONObject input) {
        Payment payment = new Payment();
        payment.setDate(input.optString("date"));
        payment.setId(input.optString("id"));
        payment.setLeasing(new LeasingSerializer().deserialize(input.optJSONObject("leasing")));
        payment.setSum(input.optInt("sum"));
        return payment;
    }

    @Override
    public JSONObject serialize(Payment input) throws JSONException {
        JSONObject output = new JSONObject();
        output.putOpt("date", input.getDate());
        output.putOpt("id", input.getId());
        output.putOpt("leasing", new LeasingSerializer().serialize(input.getLeasing()));
        output.putOpt("sum", input.getSum());
        return output;
    }
}
