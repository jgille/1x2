package org.jon.ivmark.bet1x2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Cutoff {

    private static long cutOff(String dateString) throws ParseException {
        TimeZone tz = TimeZone.getTimeZone("Europe/Stockholm");
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(tz);
        Date date = df.parse(dateString);

        return date.getTime();
    }

    public static boolean isAfterCutOff(String dateString) {
        long cutOff;
        try {
            cutOff = cutOff(dateString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Illegal date string", e);
        }
        return cutOff < System.currentTimeMillis();
    }
}
