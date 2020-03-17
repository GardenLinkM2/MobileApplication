package com.gardenlink_mobile.serialization;

import com.gardenlink_mobile.entities.Message;
import com.gardenlink_mobile.entities.Talk;
import com.gardenlink_mobile.utils.JSONMaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class TalkSerializer implements ISerializer<Talk> {

    @Override
    public Talk deserialize(JSONObject input) throws JSONException {
        Talk talk = new Talk();
        talk.setSubject(input.getString("subject"));
        talk.setId(input.getString("id"));
        talk.setArchived(input.getBoolean("isArchived"));
        talk.setReceiver(new UserSerializer().deserialize(input.getJSONObject("receiver")));
        talk.setSender(new UserSerializer().deserialize(input.getJSONObject("sender")));

        List<Message> messages = JSONMaster.tryDeserializeMany(new MessageSerializer(), input.optString("messages"));
        if (messages != null) talk.setMessages(messages);
        return talk;
    }

    @Override
    public JSONObject serialize(Talk input) throws JSONException {
        if (input == null) return null;
        JSONObject output = new JSONObject();
        output.putOpt("isArchived", input.getArchived());
        output.putOpt("id", input.getId());
        output.putOpt("subject", input.getSubject());
        output.putOpt("sender", new UserSerializer().serialize((input.getSender())));
        output.putOpt("receiver", new UserSerializer().serialize((input.getReceiver())));
        if (input.getMessages() != null) {
            JSONArray jMessages = new JSONArray();
            for (Message message : input.getMessages()) {
                jMessages.put(new MessageSerializer().serialize(message));
            }
            output.put("messages", jMessages);
        }
        return output;
    }
}
