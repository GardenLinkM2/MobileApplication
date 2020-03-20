package com.gardenlink_mobile.wsconnecting.async_workers;

import android.os.AsyncTask;
import android.util.Log;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.utils.CustomSSLSocketFactory;
import com.gardenlink_mobile.utils.JSONMaster;
import com.gardenlink_mobile.wsconnecting.operations.Operation;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.net.ssl.HttpsURLConnection;

public class AsyncPoster<T> extends AsyncTask<String, Void, String> {

    private ISerializer<T> serializer = null;
    private WeakReference<IWebConnectable> sender;
    private Operation operation;
    private HashMap criteria = null;
    private Object argument = null;
    private int responseCode;
    private Boolean failure = false;
    private String authorization;
    private byte[] content = null;

    public AsyncPoster(ISerializer<T> serializer, WeakReference<IWebConnectable> sender, Operation operation, HashMap criteria, String authorization) {
        this.serializer = serializer;
        this.sender = sender;
        this.operation = operation;
        this.criteria = criteria;
        this.authorization = authorization;
    }

    public AsyncPoster(ISerializer<T> serializer, WeakReference<IWebConnectable> sender, Operation operation, Object argument, String authorization) {
        this.serializer = serializer;
        this.sender = sender;
        this.operation = operation;
        this.argument = argument;
        this.authorization = authorization;
    }

    public AsyncPoster(WeakReference<IWebConnectable> sender, Operation operation, byte[] content) {
        this.sender = sender;
        this.operation = operation;
        this.content = content;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        try {
            URL url = new URL(urls[0]);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setSSLSocketFactory(CustomSSLSocketFactory.getSSLSocketFactory());

            String jsonInputString = null;
            if (criteria != null)
                jsonInputString = JSONMaster.createJsonInputString(criteria);
            if (argument != null)
                jsonInputString = serializer.serialize((T) argument).toString();
            if (authorization != null) conn.setRequestProperty("Authorization", authorization);
            conn.setRequestMethod("POST");

            conn.setDoInput(true);
            if (jsonInputString != null) {
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setChunkedStreamingMode(0);
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(jsonInputString);
                osw.flush();
                osw.close();
            }

            if (content != null) {
                String boundary = UUID.randomUUID().toString();
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                DataOutputStream request = new DataOutputStream(conn.getOutputStream());

                request.writeBytes("--" + boundary + "\r\n");
                request.writeBytes("Content-Disposition: form-data; name=\"description\"\r\n\r\n");
                request.writeBytes("description" + "\r\n");

                request.writeBytes("--" + boundary + "\r\n");
                request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + "fichier.jpg" + "\"\r\n\r\n");
                request.write(content);
                request.writeBytes("\r\n");
                request.writeBytes("--" + boundary + "--\r\n");
                request.flush();
                request.close();
            }

            responseCode = conn.getResponseCode();
            StringBuilder response = new StringBuilder();
            BufferedReader br;
            if (200 <= responseCode && responseCode <= 299)
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                failure = true;
            }

            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine);
            }
            result = response.toString();
            conn.disconnect();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            return result;
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        if (failure) Log.e("HTTP Error",result);
        if (result.isEmpty() || failure) {
            IWebConnectable realSender = sender.get();
            if (realSender != null) {
                if (serializer != null)
                    // Casting is necessary to disambiguate the method call
                    realSender.receiveResults(responseCode, (List<Object>) null, operation);
                else
                    // Casting is necessary to disambiguate the method call
                    realSender.receiveResults(responseCode, (HashMap<String, String>) null, operation);
                return;
            }
        }
        if (operation.getName().equals("POST_PHOTO")){
            IWebConnectable realSender = sender.get();
            if (realSender != null){
                realSender.receiveResults(responseCode, new HashMap<String,String>(){{put("photo",result);}}, operation);
                return;
            }
        }
        try {
            List<T> jsonResult = new ArrayList<>();
            HashMap<String, String> mapJsonResult = new HashMap<>();
            if (serializer != null)
                jsonResult = JSONMaster.processJsonOutput(result, serializer);
            else
                mapJsonResult = JSONMaster.processJsonOutput(result);
            IWebConnectable realSender = sender.get();
            if (realSender != null) {
                if (serializer != null)
                    realSender.receiveResults(responseCode, jsonResult, operation);
                else
                    realSender.receiveResults(responseCode, mapJsonResult, operation);
            }
        } catch (JSONException e) {
            Log.e("AsyncPoster", e.getMessage());
        }
    }
}
