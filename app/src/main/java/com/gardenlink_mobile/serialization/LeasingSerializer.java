package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Leasing;

import org.json.JSONException;
import org.json.JSONObject;

public class LeasingSerializer implements ISerializer<Leasing> {

    @Override
    public Leasing deserialize(JSONObject input) {
        Leasing leasing = new Leasing();
        leasing.setId(input.optString("id"));
        leasing.setTime(input.optInt("time"));
        leasing.setBegin(input.optString("begin"));
        leasing.setEnd(input.optString("end"));
        leasing.setRenew(input.optBoolean("renew"));
        leasing.setState(input.optInt("state"));
        leasing.setGarden(input.optString("garden"));
        return leasing;
    }



    @Override
    public JSONObject serialize(Leasing input) throws JSONException
    {
        JSONObject output = new JSONObject();

        output.putOpt("id",input.getId());
        output.putOpt("time",input.getTime());
        output.putOpt("begin",input.getBegin());
        output.putOpt("end",input.getEnd());
        output.putOpt("renew",input.getRenew());
        output.putOpt("state",input.getState());
        output.putOpt("garden",input.getGarden());

        return output;

    }

}
