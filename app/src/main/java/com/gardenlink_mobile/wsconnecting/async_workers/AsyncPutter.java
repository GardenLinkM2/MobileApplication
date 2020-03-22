package com.gardenlink_mobile.wsconnecting.async_workers;

import android.os.AsyncTask;
import android.util.Log;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.utils.CustomSSLSocketFactory;
import com.gardenlink_mobile.utils.JSONMaster;
import com.gardenlink_mobile.wsconnecting.operations.Operation;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

public class AsyncPutter<T> extends AsyncTask<String, Void, String> {

    private WeakReference<IWebConnectable> sender;
    private Operation operation;
    private HashMap criteria;
    private int responseCode;
    private String authorization;

    public AsyncPutter(WeakReference<IWebConnectable> sender, Operation operation, HashMap criteria, String authorization) {
        this.sender = sender;
        this.operation = operation;
        this.criteria = criteria;
        this.authorization = authorization;
    }

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

            conn.setSSLSocketFactory(CustomSSLSocketFactory.getSSLSocketFactory());

            String jsonInputString = JSONMaster.createJsonInputString(criteria);

            if (authorization != null) conn.setRequestProperty("Authorization",authorization);

            conn.setRequestMethod("PUT");
            if (jsonInputString != null) {
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                conn.setChunkedStreamingMode(0);
                OutputStream os = conn.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
                osw.write(jsonInputString);
                osw.flush();
                osw.close();
            }
            responseCode = conn.getResponseCode();
            conn.disconnect();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            return "";
        }
        return "";
    }

    @Override
    protected void onPostExecute(String result) {
        IWebConnectable realSender = sender.get();
        if (realSender != null) {
            // Casting is necessary to disambiguate the method call
            realSender.receiveResults(responseCode, operation);
            return;
        }
    }
}
