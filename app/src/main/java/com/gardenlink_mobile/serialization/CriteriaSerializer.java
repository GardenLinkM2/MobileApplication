package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Criteria;

import org.json.JSONException;
import org.json.JSONObject;

public class CriteriaSerializer implements ISerializer<Criteria> {

    @Override
    public Criteria deserialize(JSONObject input) throws JSONException {
        Criteria criteria = new Criteria();
        criteria.setArea(input.getInt("area"));
        criteria.setDirectAccess(input.getBoolean("directAccess"));
        criteria.setEquipments(input.getBoolean("equipments"));
        criteria.setId(input.getString("id"));
        criteria.setLocation(new LocationSerializer().deserialize(input.getJSONObject("location")));
        criteria.setLocationTime(new TimeSpanSerializer().deserialize(input.getJSONObject("timeSpan")));
        criteria.setOrientation(input.getString("orientation"));
        criteria.setPrice(input.getDouble("price"));
        criteria.setTypeOfClay(input.getString("typeOfClay"));
        criteria.setWaterAccess(input.getBoolean("waterAccess"));
        return criteria;
    }

    @Override
    public Criteria[] deserializeMany(JSONObject[] input) {
        return new Criteria[0];
    }

    @Override
    public JSONObject serialize(Criteria input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Criteria[] input) {
        return new JSONObject[0];
    }
}
