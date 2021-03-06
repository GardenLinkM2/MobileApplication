package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Garden;
import com.gardenlink_mobile.entities.Photo;
import com.gardenlink_mobile.entities.Report;
import com.gardenlink_mobile.entities.Status;
import com.gardenlink_mobile.utils.JSONMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GardenSerializer implements ISerializer<Garden> {

    @Override
    public Garden deserialize(JSONObject input) throws JSONException {
        Garden garden = new Garden();

        garden.setId(input.optString("id"));
        garden.setName(input.optString("name"));
        try {
            garden.setIsReserved(input.getBoolean("isReserved"));
        }
        catch (Exception e){
            garden.setIsReserved(null);
        }
        garden.setMinUse(input.optInt("minUse"));
        garden.setDescription(input.optString("description"));
        garden.setLocation(new LocationSerializer().deserialize(input.optJSONObject("location")));
        garden.setOwner(input.optString("owner"));
        garden.setValidation(new Status(input.optString("validation")));
        garden.setCriteria(new CriteriaSerializer().deserialize(input.optJSONObject("criteria")));

        try {
            List<Photo> photos = JSONMaster.tryDeserializeMany(new PhotoSerializer(), input.optString("photos"));
            if (photos != null) garden.setPhotos(photos);
        } catch (Exception e) {}
        try {
            List<Report> reports = JSONMaster.tryDeserializeMany(new ReportSerializer(), input.optString("reports"));
            if (reports != null) garden.setReports(reports);
        } catch (Exception e){}

        return garden;
    }

    @Override
    public JSONObject serialize(Garden input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();

        output.putOpt("id", input.getId());
        output.putOpt("name", input.getName());
        output.putOpt("isReserved", input.getIsReserved());
        output.putOpt("minUse", input.getMinUse());
        output.putOpt("description", input.getDescription());
        output.putOpt("location", new LocationSerializer().serialize(input.getLocation()));
        output.putOpt("owner", input.getOwner());
        Status validation = input.getValidation();
        if (validation != null) {
            output.putOpt("validation", input.getValidation().getStatus());
        }
        output.putOpt("criteria", new CriteriaSerializer().serialize(input.getCriteria()));
        if (input.getPhotos() != null) {
            JSONArray jPhotos = new JSONArray();
            for (Photo photo : input.getPhotos()) {
                jPhotos.put(new PhotoSerializer().serialize(photo));
            }
            output.put("photos", jPhotos);
        }
        if (input.getReports() != null) {
            JSONArray jReports = new JSONArray();
            for (Report report : input.getReports()) {
                jReports.put(new ReportSerializer().serialize(report));
            }
            output.put("reports", jReports);
        }
        return output;
    }
}
