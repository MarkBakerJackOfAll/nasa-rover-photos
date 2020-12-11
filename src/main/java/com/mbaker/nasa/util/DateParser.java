package com.mbaker.nasa.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateParser {

    public static String reformatDate(String inputDate) throws ParseException {
        Date date = null;
        String [] formats = {
                                "yyyy-MM-dd",
                                "MMM-d-yyyy",
                                "MMM d, yyyy",
                                "MM/dd/yy"
                            };
        boolean found = false;
        for (String format : formats) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(format);
                dateFormat.setLenient(false);
                date = dateFormat.parse(inputDate);
                found = true;
                break;
            }
            catch (ParseException e) {
                found = false;
            }
        }

        if (!found) {
            throw new ParseException("Unparseable date: \"" + inputDate + "\"", 0);
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date).toString();
    }
}
