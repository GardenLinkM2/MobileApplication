package com.gardenlink_mobile.wsconnecting;

import com.gardenlink_mobile.activities.IWebConnectable;
import com.gardenlink_mobile.serialization.ISerializer;
import com.gardenlink_mobile.wsconnecting.async_workers.*;
import com.gardenlink_mobile.wsconnecting.operations.Operation;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public class Caller{

    public static <T> void post(WeakReference<IWebConnectable> sender, String url, ISerializer<T> serializer, HashMap criteria, Operation operation, String authorization){
        AsyncPoster worker = new AsyncPoster(serializer, sender, operation, criteria, authorization);
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

    public static void put(WeakReference<IWebConnectable> sender, String url, HashMap criteria, Operation operation, String authorization){
        AsyncPutter worker = new AsyncPutter(sender, operation, criteria, authorization);
        String[] params = new String[1];
        params[0] = url;
        worker.execute(params);
    }

    public static void delete(WeakReference<IWebConnectable> sender, String url, HashMap criteria, Operation operation, String authorization){
        AsyncDeleter worker = new AsyncDeleter(sender, operation, criteria, authorization);
        String[] params = new String[1];
        params[0] = url;
        worker.execute(params);
    }
}
