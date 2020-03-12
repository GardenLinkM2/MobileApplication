package com.gardenlink_mobile.serialization;

import android.util.Log;

import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Photo;
import com.gardenlink_mobile.entities.Status;
import com.gardenlink_mobile.utils.JSONMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class GardenSerializer implements ISerializer<Garden> {

    @Override
    public Garden deserialize(JSONObject input) throws JSONException {
        Garden garden = new Garden();

        garden.setId(input.optString("id"));
        garden.setName(input.optString("name"));
        garden.setIsReserved(input.optBoolean("isReserved"));
        garden.setMinUse(input.optInt("minUse"));
        garden.setDescription(input.optString("description"));
        garden.setLocation(new LocationSerializer().deserialize(input.optJSONObject("location")));
        garden.setOwner(input.optString("owner"));
        garden.setValidation(new Status(input.optInt("validation")));
        garden.setCriteria(new CriteriaSerializer().deserialize(input.optJSONObject("criteria")));


        List<Photo> photos = JSONMaster.tryDeserializeMany(new PhotoSerializer(), input.optString("photos"));
        if (photos != null) garden.setPhotos(photos);

        return garden;
    }


    @Override
    public JSONObject serialize(Garden input) throws JSONException {
        JSONObject output = new JSONObject();

        output.putOpt("id", input.getId());
        output.putOpt("name", input.getName());
        output.putOpt("isReserved", input.getIsReserved());
        output.putOpt("minUse", input.getMinUse());
        output.putOpt("description", input.getDescription());
        output.putOpt("location", new LocationSerializer().serialize(input.getLocation()));
        output.putOpt("owner", input.getOwner());
        output.putOpt("validation",input.getValidation().getStatus());
        output.putOpt("criteria", new CriteriaSerializer().serialize(input.getCriteria()));
        if (input.getPhotos() != null)
        {
            JSONArray jPhotos = new JSONArray();
            for(Photo photo : input.getPhotos()){
                jPhotos.put(new PhotoSerializer().serialize(photo));
            }
            output.put("photos",jPhotos);
        }
        return output;
    }


}
