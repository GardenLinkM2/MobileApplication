package com.gardenlink_mobile.utils;

import java.sql.Timestamp;
import java.util.Date;

public class DateMaster {
    public static Date TimeStampToDate(Double timestamp){
        Long timestampLong;
        try {
            timestampLong = Double.valueOf(timestamp).longValue()*1000;
        }
        catch (Exception e){
            return null;
        }
        if (timestampLong == null) return null;
        Timestamp ts = new Timestamp(timestampLong);
        Date date = new Date(ts.getTime());
        return date;
    }
}
