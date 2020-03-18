package com.gardenlink_mobile.session;

import com.gardenlink_mobile.entities.Location;
import com.gardenlink_mobile.entities.User;
import com.gardenlink_mobile.entities.Wallet;

public class Session {

    private static Session instance = null;

    private Session(){}

    public static Session getInstance() {
        if (instance == null) instance = new Session();
        return instance;
    }

    private String userName;
    private String sessionToken;
    private String userToken;
    private String accessToken;
    private Location location;
    private String uuid;
    private User currentUser;
    private Wallet currentUserWallet;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public Wallet getCurrentUserWallet() {
        return currentUserWallet;
    }

    public void setCurrentUserWallet(Wallet currentUserWallet) {
        this.currentUserWallet = currentUserWallet;
    }

    public void flush()
    {
        instance = null;
    }
}
