package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Message;
import com.gardenlink_mobile.entities.Photo;
import com.gardenlink_mobile.utils.JSONMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MessageSerializer implements ISerializer<Message> {

    @Override
    public Message deserialize(JSONObject input) throws JSONException {
        Message message = new Message();
        message.setCreationDate(input.optDouble("creationDate"));
        message.setId(input.optString("id"));
        try {
            message.setRead(input.getBoolean("isRead"));
        }
        catch (Exception e) {
            message.setRead(null);
        }
        message.setSender(input.optString("sender"));
        message.setText(input.optString("text"));
        List<Photo> photos = JSONMaster.tryDeserializeMany(new PhotoSerializer(), input.optString("photos"));
        if (photos != null) message.setPhotos(photos);
        return message;

    }

    @Override
    public JSONObject serialize(Message input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("creationDate", input.getCreationDate());
        output.putOpt("id", input.getId());
        output.putOpt("isRead", input.getRead());
        output.putOpt("sender", input.getSender());
        output.putOpt("text", input.getText());
        if (input.getPhotos() != null) {
            JSONArray jPhotos = new JSONArray();
            for (Photo photo : input.getPhotos()) {
                jPhotos.put(new PhotoSerializer().serialize(photo));
            }
            output.put("photos", jPhotos);
        }
        return output;
    }
}
