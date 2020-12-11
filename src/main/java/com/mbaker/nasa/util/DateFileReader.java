package com.mbaker.nasa.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.text.ParseException;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mbaker.nasa.util.DateParser;

public class DateFileReader {

    private static final Logger logger = LoggerFactory.getLogger(DateFileReader.class);

    public static List<String> readFile(String filepath) {

        if (!Files.exists(Paths.get(filepath)) ||
            !Files.isRegularFile(Paths.get(filepath))) {
            
            logger.error("Filepath [{}] does not exist", filepath);
            return null;
        }

        File file = new File(filepath);

        BufferedReader bufferedReader;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
        } catch(FileNotFoundException e) {
            logger.error("File [{}] does not exist", filepath);
            return null;
        }

        List<String> dateList = new ArrayList<String>();

        try {
            String line = bufferedReader.readLine();
            while(line != null) {
                try {
                    dateList.add(DateParser.reformatDate(line));
                } catch(ParseException e) {
                    /*
                     * Chose to continue if a single line was not parsable into a date,
                     * and this log will tell the user which date had an issue and it
                     * is clearly isolated from the others.
                     */
                    logger.warn("Parsing Date, got: {}", e.getMessage());
                }
                line = bufferedReader.readLine();
            }
        } catch(IOException e) {
            /*
             * Choice to be made here.  If the file fails to be read, do you do best effort
             * and return all the dates you have parsed so far, or reject the input as invalid?
             * I chose to reject all data as invalid.  This is a safer failure state as you
             * avoid posible previous dates that have been corrupted
             */
             logger.error("Reading file");
             return null;
        }

        return dateList;
    }
}
