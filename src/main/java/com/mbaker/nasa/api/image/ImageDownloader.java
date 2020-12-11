package com.mbaker.nasa.api.image;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.IOException;

import java.lang.IllegalArgumentException;

import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mbaker.nasa.api.rover.data.PhotoMetadata;

public class ImageDownloader {
    private String downloadDir;
    private static final Logger logger = LoggerFactory.getLogger(ImageDownloader.class);

    public ImageDownloader(String downloadDir) throws IllegalArgumentException {
        this.downloadDir = downloadDir;
        
        // Create directory if it doesn't exist
        if(!Files.exists(Paths.get(downloadDir))) {
            File dir = new File(downloadDir);
            if(!dir.mkdirs()) {
                throw new IllegalArgumentException("Provided download directory [" +
                                                   downloadDir + "] faile to be created");
            }
        }
        // Ensure it wasn't a preexisting file
        if(Files.isRegularFile(Paths.get(downloadDir))) {
            throw new IllegalArgumentException("Provided download directory [" +
                                               downloadDir + "] is not a directory");
        }
    }

    public HttpURLConnection redirect(URL url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_MOVED_PERM ||
                responseCode == HttpURLConnection.HTTP_MOVED_TEMP) {

                String redirect = connection.getHeaderField("Location");
                if (redirect != null) {
                    connection = (HttpURLConnection) (new URL(redirect)).openConnection();
                } else {
                    logger.error("Http code inidicated moved, but no redirect location " +
                                 "found for url [{}]", url.toString());
                    return null;
                }
            } else if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.error("Unsupported HTTP response [{}]", connection.getResponseCode());
                return null;
            }
        } catch(MalformedURLException e) {
            logger.error("Bad URL when redirecting", e);
            return null;
        } catch(IOException e) {
            logger.error("IOException when redirecting", e);
            return null;
        }
        return connection;
    }

    public void download(PhotoMetadata photoMetadata) {
        try {
            /*
             * File name guaranteed to be unique and descriptive so we can avoid
             * rerequesting a pre cached file.
             */
            String filename = downloadDir + "/" + photoMetadata.getRoverName() + "_"
                                                + photoMetadata.getCameraName() + "_"
                                                + photoMetadata.getEarthDate() + "_"
                                                + photoMetadata.getId() + ".jpg";

            if(Files.exists(Paths.get(filename))) {
                /*
                 * Note while this is more efficient in reducing unneeded network traffic
                 * it doesn't check for corrupted data on disk. Small tradeoff.
                 */
                logger.info("File [{}] has already been cached, skipping redownload",
                            filename);
                return;
            }

            URL url = photoMetadata.getImageSrc();
            
            HttpURLConnection connection = redirect(url);
            if (connection == null) {
                //Error has already been printed
                return;
            }
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                logger.error("Unsupported HTTP response [{}]", connection.getResponseCode());
                return;
            }
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
    
            FileOutputStream outputStream = new FileOutputStream(filename);

            byte[] buffer = new byte[1024];
            int read = inputStream.read(buffer);
            while (read != -1) {
                outputStream.write(buffer, 0, read);
                read = inputStream.read(buffer);
            }
            inputStream.close();
            outputStream.close();
        }
        catch(IOException e) {
            logger.error("IOException when downloading", e);
        }
    }
}
