package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Demand;

import org.json.JSONException;
import org.json.JSONObject;

public class DemandSerializer implements ISerializer<Demand> {

    @Override
    public Demand deserialize(JSONObject input) throws JSONException {
        Demand demand = new Demand();
        demand.setFirstMessage(input.optString("firstMessage"));
        demand.setStatus(new StatusSerializer().deserialize(input.optJSONObject("status")));
        return demand;
    }

    @Override
    public JSONObject serialize(Demand input) throws JSONException {
        JSONObject output = new JSONObject();
        output.putOpt("firstMessage", input.getFirstMessage());
        output.putOpt("status",new StatusSerializer().serialize(input.getStatus()));
        return output;
    }
}
