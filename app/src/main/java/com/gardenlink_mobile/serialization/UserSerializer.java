package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.User;

import org.json.JSONException;
import org.json.JSONObject;

public class UserSerializer implements ISerializer<User> {

    @Override
    public User deserialize(JSONObject input) throws JSONException {
        User user = new User();
        user.setId(input.optString("id"));
        user.setEmail(input.optString("email"));
        user.setFirstName(input.optString("firstName"));
        user.setLastName(input.optString("lastName"));
        user.setPhoto(input.optString("photo"));
        return user;
    }

    @Override
    public JSONObject serialize(User input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("lastName", input.getLastName());
        output.putOpt("firstName", input.getFirstName());
        output.putOpt("id", input.getId());
        output.putOpt("email", input.getEmail());
        output.putOpt("photo", input.getPhoto());
        return output;
    }
}
