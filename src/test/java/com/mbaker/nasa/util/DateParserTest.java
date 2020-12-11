package com.mbaker.nasa.util;

import java.text.ParseException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.mbaker.nasa.util.DateParser;

public class DateParserTest {
    @Test
    public void noChange() {
        String date         = "2020-12-21";
        String expectedDate = "2020-12-21";

        String parsedDate = null;
        try {
            parsedDate = DateParser.reformatDate(date);
        } catch(ParseException e) {
            fail("Unnexpected exception thrown in test: " + e.getMessage());
            
        }

        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void slashes() {
        String date         = "1/7/17";
        String expectedDate = "2017-01-07";
    
        String parsedDate = null;
        try {
            parsedDate = DateParser.reformatDate(date);
        } catch(ParseException e) {
            fail("Unnexpected exception thrown in test: " + e.getMessage());
        }
    
        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void slashesPadded() {
        String date         = "06/01/20";
        String expectedDate = "2020-06-01";

        String parsedDate = null;
        try {
            parsedDate = DateParser.reformatDate(date);
        } catch(ParseException e) {
            fail("Unnexpected exception thrown in test: " + e.getMessage());
        }

        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void dashShortName() {
        String date         = "Jul-04-1999";
        String expectedDate = "1999-07-04";

        String parsedDate = null;
        try {
            parsedDate = DateParser.reformatDate(date);
        } catch(ParseException e) {
            fail("Unnexpected exception thrown in test: " + e.getMessage());
        }

        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void dashLongName() {
        String date         = "October-12-2010";
        String expectedDate = "2010-10-12";

        String parsedDate = null;
        try {
            parsedDate = DateParser.reformatDate(date);
        } catch(ParseException e) {
            fail("Unnexpected exception thrown in test: " + e.getMessage());
        }

        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void longWritten() {
        String date         = "November 5, 2015";
        String expectedDate = "2015-11-05";

        String parsedDate = null;
        try {
            parsedDate = DateParser.reformatDate(date);
        } catch(ParseException e) {
            fail("Unnexpected exception thrown in test: " + e.getMessage());
        }

        assertEquals(expectedDate, parsedDate);
    }

    @Test
    public void failJunkData() {
        String date         = "Nope no date here";

        Exception exception = assertThrows(ParseException.class, () -> {
                    String parsedDate = DateParser.reformatDate(date);
                });

        assertTrue(exception.getMessage().contains(date));
    }

    @Test
    public void failBadDate() {
        String date         = "November 31, 2015";

        Exception exception = assertThrows(ParseException.class, () -> {
                    String parsedDate = DateParser.reformatDate(date);
                });

        assertTrue(exception.getMessage().contains(date));
    }
}
