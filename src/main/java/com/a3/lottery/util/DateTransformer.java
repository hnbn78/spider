package com.a3.lottery.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateTransformer {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'.0Z'";

    public static String dateTransformBetweenTimeZone(Date sourceDate, DateFormat formatter, TimeZone sourceTimeZone,
            TimeZone targetTimeZone) {
        Long targetTime = sourceDate.getTime() - sourceTimeZone.getRawOffset() + targetTimeZone.getRawOffset();
        return DateTransformer.getTime(new Date(targetTime), formatter);
    }

    public static String getTime(Date date, DateFormat formatter) {
        return formatter.format(date);
    }

    public static String getNowTimeFromSteamTime(Date date) {
        String fmt = "yyyy-MM-dd HH:mm";
        TimeZone srcTimeZone = TimeZone.getTimeZone("GMT-8");
        TimeZone destTimeZone = TimeZone.getTimeZone("GMT+8");
        DateFormat formatter = new SimpleDateFormat(fmt);
        return dateTransformBetweenTimeZone(date, formatter, srcTimeZone, destTimeZone);
    }

    public static void main(String[] args) throws ParseException {
        // 2019-05-30T10:15:16.0Z
        // 2019-05-30T10:18:21
        // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // String str = "2019-05-30 18:15:16";
        // Date date = sdf.parse(str);
        //
        // DateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        // // Date date = Calendar.getInstance().getTime();
        // TimeZone srcTimeZone = TimeZone.getTimeZone("GMT+8");
        // TimeZone destTimeZone = TimeZone.getTimeZone("GMT");
        // System.out.println(DateTransformer.dateTransformBetweenTimeZone(date, formatter, srcTimeZone, destTimeZone));
        //

    }
}
