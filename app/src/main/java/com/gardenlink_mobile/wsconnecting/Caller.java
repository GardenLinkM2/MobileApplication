package com.gardenlink_mobile.wsconnecting;

import android.graphics.Bitmap;
import android.net.Uri;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.wsconnecting.async_workers.*;
import com.gardenlink_mobile.wsconnecting.operations.Operation;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class Caller {

    public static <T> void post(WeakReference<IWebConnectable> sender, String url, ISerializer<T> serializer, HashMap criteria, Operation operation, String authorization) {
        AsyncPoster worker = new AsyncPoster(serializer, sender, operation, criteria, authorization);
        String[] params = new String[1];
        params[0] = url;
        worker.execute(params);
    }

    public static <T> void post(WeakReference<IWebConnectable> sender, String url, ISerializer<T> serializer, Object argument, Operation operation, String authorization) {
        AsyncPoster worker = new AsyncPoster(serializer, sender, operation, argument, authorization);
        String[] params = new String[1];
        params[0] = url;
        worker.execute(params);
    }

    public static <T> void post(WeakReference<IWebConnectable> sender, String url, Operation operation, byte[] content){
        AsyncPoster worker = new AsyncPoster(sender, operation, content);
        String[] params = new String[1];
        params[0] = url;
        worker.execute(params);
    }

    public static <T> void get(WeakReference<IWebConnectable> sender, String url, ISerializer<T> serializer, HashMap criteria, Operation operation, String authorization){
        AsyncGetter worker = new AsyncGetter(serializer, sender, operation, criteria, authorization);
        String[] params = new String[1];
        params[0] = url;
        worker.execute(params);
    }

    public static void get(WeakReference<IWebConnectable> sender, String url, Operation operation){
        AsyncGetter worker = new AsyncGetter(sender, operation);
        String[] params = new String[1];
        params[0] = url;
        worker.execute(params);
    }

    public static void put(WeakReference<IWebConnectable> sender, String url, HashMap criteria, Operation operation, String authorization) {
        AsyncPutter worker = new AsyncPutter(sender, operation, criteria, authorization);
        String[] params = new String[1];
        params[0] = url;
        worker.execute(params);
    }

    public static void delete(WeakReference<IWebConnectable> sender, String url, HashMap criteria, Operation operation, String authorization) {
        AsyncDeleter worker = new AsyncDeleter(sender, operation, criteria, authorization);
        String[] params = new String[1];
        params[0] = url;
        worker.execute(params);
    }
}
