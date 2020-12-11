package com.mbaker.nasa;

import java.lang.IllegalArgumentException;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mbaker.nasa.api.image.ImageDownloader;
import com.mbaker.nasa.api.rover.RoverApiWrapper;
import com.mbaker.nasa.api.rover.data.PhotoQueryResponse;
import com.mbaker.nasa.api.rover.data.PhotoMetadata;
import com.mbaker.nasa.util.DateFileReader;

class RoverImageRetriever {
    private static final Logger logger = LoggerFactory.getLogger(RoverImageRetriever.class);
    //private static final String API_KEY="GX1A9GY2sCRizdPsXweMwxBLCIjsqBhjf4rbVmWb";
    private static final String API_KEY="DEMO_KEY";

    private static final String DEFAULT_ROVER = "curiosity";
    private static final String DEFAULT_OUTPUT_DIR = "/tmp/imageDownloads";
    private static final String DEFAULT_INPUT_FILE = "/tmp/inputFile.txt";
    private static boolean downloadAll = false;            

    public static void main(String [] args) {
        RoverApiWrapper roverApi = new RoverApiWrapper(API_KEY);
        ImageDownloader imageDownloader = null;
        try {
            imageDownloader = new ImageDownloader(DEFAULT_OUTPUT_DIR);
        } catch (IllegalArgumentException e) {
            logger.error("ImageDownloader failed to initialize");
            System.exit(-1);
        }

        String inputFile = DEFAULT_INPUT_FILE;
        if (args.length > 0) {
            inputFile = args[0];
        }
        List<String> dateList = DateFileReader.readFile(inputFile);

        if(dateList != null) {
            for(String date : dateList) {
                PhotoQueryResponse response = roverApi.query(date, DEFAULT_ROVER);
                List<PhotoMetadata> photos = response.getPhotos();

                logger.debug("Downloading from [{}]", date);
                if(downloadAll) {
                    //Itterate over all photos and download them
                    for(PhotoMetadata photo : photos) {
                        imageDownloader.download(photo);
                    }
                } else {
                    //Default, choose random photo to download
                    Random rand = new Random();
                    int randomIndex = rand.nextInt(photos.size());
                    imageDownloader.download(photos.get(randomIndex));
                }
            }
        } else {
            logger.error("Reading file");
        }

    }
}
