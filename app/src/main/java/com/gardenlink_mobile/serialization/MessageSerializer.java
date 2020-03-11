package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Message;

import org.json.JSONObject;

public class MessageSerializer implements ISerializer<Message> {

    @Override
    public Message deserialize(JSONObject input) {
        return null;
    }

    @Override
    public Message[] deserializeMany(JSONObject[] input) {
        return new Message[0];
    }

    @Override
    public JSONObject serialize(Message input) {
        return null;
    }

    @Override
    public JSONObject[] deserializeMany(Message[] input) {
        return new JSONObject[0];
    }
}
