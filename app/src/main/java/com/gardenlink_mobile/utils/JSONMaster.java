package com.gardenlink_mobile.utils;

import android.util.Log;

import com.gardenlink_mobile.serialization.ISerializer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class JSONMaster {

    public static String createJsonInputString(HashMap criteria) throws JSONException {
        if (criteria == null) return null;
        JSONObject jsonInputString = new JSONObject();
        Iterator it = criteria.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            jsonInputString.put(pair.getKey().toString(),pair.getValue());
        }
        return jsonInputString.toString();
    }

    /***
     * This method processes a Json output when it is known in advance that it will be deserializable to an array of entities
     * @param output
     * @param serializer
     * @param <T>
     * @return
     * @throws JSONException
     */
    public static <T> List<T> processJsonOutput(String output, ISerializer<T> serializer) throws JSONException {
        List<T> results = new ArrayList<>();
        JSONArray jArray;
        try {
            jArray = new JSONArray(output);
        }
        catch (JSONException e)
        {
            results.add(serializer.deserialize(new JSONObject(output)));
            return results;
        }
        IntStream.range(0, jArray.length()).forEach(index -> {
            try {
                results.add(serializer.deserialize(jArray.getJSONObject(index)));
            } catch (JSONException e) {
                Log.e("JSON Deserialization","Error while fetching value of index "+index+" using serializer "+serializer.getClass().toString());
            }
        });
        return results;
    }

    /***
     * This method processes a Json output when it is known in advance that it will be a simple map of fields (key->value)
     * @param output
     * @return
     * @throws JSONException
     */
    public static HashMap<String,String> processJsonOutput(String output) throws JSONException {
        HashMap<String,String> results = new HashMap<>();
        JSONObject jObject = new JSONObject(output);
        jObject.keys().forEachRemaining( key -> {
            try {
                results.put(key, jObject.getString(key));
            } catch (JSONException e) {
                Log.e("JSON Deserialization","Error while fetching value of key "+key);
            }
        });
        return results;
    }


    /***
     * This method processes a Json output when it is known in advance that it will be deserializable to an array of entities contained in a "data" Json object
     * @param output
     * @param serializer
     * @param <T>
     * @return
     * @throws JSONException
     */
    public static <T> List<T> processJsonOutputInData(String output, ISerializer<T> serializer) throws JSONException {
        JSONObject jObject= new JSONObject(output).getJSONObject("data");
        List<T> results = new ArrayList<>();
        JSONArray jArray = new JSONArray(jObject);
        IntStream.range(0, jArray.length()).forEach(index -> {
            try {
                results.add(serializer.deserialize(jArray.getJSONObject(index)));
            } catch (JSONException e) {
                Log.e("JSON Deserialization","Error while fetching value of index "+index+" using serializer "+serializer.getClass().toString());
            }
        });
        return results;
    }
}
