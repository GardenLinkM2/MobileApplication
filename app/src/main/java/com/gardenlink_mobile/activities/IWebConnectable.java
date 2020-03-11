package com.gardenlink_mobile.activities;

import com.gardenlink_mobile.wsconnecting.operations.Operation;

import java.util.HashMap;
import java.util.List;

public interface IWebConnectable {
    <T> void receiveResults(int responseCode, List<T> results, Operation operation);
    void receiveResults(int responseCode, HashMap<String,String> results, Operation operation);
    // This should be reserved for operations that CANNOT get a response body from the server and not be used when there was an error and no response body was available for an operation that should've had one
    void receiveResults(int responseCode, Operation operation);
    String getTag();
}
