package com.gardenlink_mobile;
//TODO : Delete me
public class MOCK_WS {

    private final static String ID="Denis";
    private final static String pass="pouglou";

    public static boolean identification(String pLogin,String pPassword)
    {
        if(ID.equals(pLogin) && pass.equals(pPassword))
        {
            return true;
        }
        else{
            return false;
        }

    }



}
