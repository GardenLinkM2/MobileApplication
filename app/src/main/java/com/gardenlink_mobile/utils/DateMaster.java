package com.gardenlink_mobile.utils;

import java.sql.Timestamp;
import java.util.Date;

public class DateMaster {
    public static Date TimeStampToDate(Double timestamp){
        Long timestampLong;
        try {
            timestampLong = Long.parseLong(timestamp.toString());
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
