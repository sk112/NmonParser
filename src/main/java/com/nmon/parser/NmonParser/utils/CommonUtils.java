package com.nmon.parser.NmonParser.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CommonUtils {

    public static Long getTimeStampFromFormattedTime(String time, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(formatter.parse(time));

        return cal.getTimeInMillis();
    }

}
