package com.mbaker.nasa.api.image;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import java.lang.IllegalArgumentException;

import java.net.MalformedURLException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

import org.springframework.util.FileSystemUtils;

import com.mbaker.nasa.api.image.ImageDownloader;
import com.mbaker.nasa.api.rover.data.PhotoMetadata;

@RunWith(MockitoJUnitRunner.class)
public class ImageDownloaderTest {

    public static String BASE_DIR = "/tmp/imageDownlaoder";

    public URL mockUrl;

    @Mock
    public HttpURLConnection mockConnection;

    @Before
    public void setup() {
        File dir = new File(BASE_DIR);
        dir.mkdir();

        final URLStreamHandler handler = new URLStreamHandler() {
    
            @Override
            protected URLConnection openConnection(final URL url) throws IOException {
                return mockConnection;
            }
        };

        try {
            mockUrl = new URL("http://test.nasa.gov", "test.nasa.gov", 80, "", handler);
        } catch (MalformedURLException e) {
            fail("Unnexpected exception in before");
        }
    }

    @After
    public void cleanUp() {
        FileSystemUtils.deleteRecursively(new File(BASE_DIR));
    }

    @Test
    public void testConstructorNominal() {
        String dirName = BASE_DIR + "/constructor/nominal";
        File dir = new File(dirName);
        dir.mkdirs();

        ImageDownloader cut = null;
        try {
            cut = new ImageDownloader(dirName);
        } catch(IllegalArgumentException e) {
            fail("Unexpected exception was thrown in Nominal Constructor test");
        }
        assertNotNull(cut);
        assertTrue(Files.exists(Paths.get(dirName)));
        assertFalse(Files.isRegularFile(Paths.get(dirName)));
    }

    @Test
    public void testConstructorDirDNE() {
        String dirName = BASE_DIR + "/constructor/dirDNE";

        ImageDownloader cut = null;
        try {
            cut = new ImageDownloader(dirName);
        } catch(IllegalArgumentException e) {
            fail("Unexpected exception was thrown in Constructor DirDNE test");
        }
        assertNotNull(cut);
        assertTrue(Files.exists(Paths.get(dirName)));
        assertFalse(Files.isRegularFile(Paths.get(dirName)));
    }

    @Test
    public void testConstructorDirIsFile() {
        String dirName = BASE_DIR + "/constructor/file";
        File file = new File(dirName);
        file.getParentFile().mkdirs();
        try {
            file.createNewFile();
        } catch(IOException e) {
            fail("Unexpected exception was thrown while creating file in Constructor DirIsFile test");
        }

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                    new ImageDownloader(dirName);
                });
        assertTrue(exception.getMessage().contains("is not a directory"));
    }

    @Test
    public void testRedirect200() {
        String dirName = BASE_DIR + "/redirect/twohundred";
        ImageDownloader cut = new ImageDownloader(dirName);

        try {
            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);        
        } catch(IOException e) {
            fail("Unexpected exception was thrown while mocking in Redirect 200 test");
        }

        HttpURLConnection result = cut.redirect(mockUrl);

        assertEquals(result, mockConnection);
    }

    @Test
    public void testRedirect301() {
        String dirName = BASE_DIR + "/redirect/threeohone";
        String redirectUrl = "http://test.new.url.com";
        ImageDownloader cut = new ImageDownloader(dirName);

        try {
            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_MOVED_PERM);
            when(mockConnection.getHeaderField("Location")).thenReturn(redirectUrl);
        } catch(IOException e) {
            fail("Unexpected exception was thrown while mocking in Redirect 301 test");
        }

        HttpURLConnection result = cut.redirect(mockUrl);

        assertEquals(result.getURL().toString(), redirectUrl);
    }

    @Test
    public void testRedirect301NoLocation() {
        String dirName = BASE_DIR + "/redirect/threeohone";
        String redirectUrl = "http://test.new.url.com";
        ImageDownloader cut = new ImageDownloader(dirName);

        try {
            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_MOVED_PERM);
            when(mockConnection.getHeaderField("Location")).thenReturn(null);
        } catch(IOException e) {
            fail("Unexpected exception was thrown while mocking in faulty Redirect 301 test");
        }

        HttpURLConnection result = cut.redirect(mockUrl);

        assertNull(result);
    }

    @Test
    public void testRedirectr404() {
        String dirName = BASE_DIR + "/redirect/twohundred";
        ImageDownloader cut = new ImageDownloader(dirName);

        try {
            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_NOT_FOUND);
        } catch(IOException e) {
            fail("Unexpected exception was thrown while mocking in Redirect 404 test");
        }

        HttpURLConnection result = cut.redirect(mockUrl);

        assertNull(result);
    }

    @Test
    public void testDownloadNominal() {
        String dirName = BASE_DIR + "/download/nominal";
        ImageDownloader cut = new ImageDownloader(dirName);

        String fakeImageData = "This is not a jpg";
        String rover  = "red";
        String camera = "polaroid";
        String date   = "2019-12-25";
        int id     = 123;
        String fileLocation = dirName + "/" + rover + "_" + camera + "_" 
                                            + date + "_" + id + ".jpg";

        PhotoMetadata photo = new PhotoMetadata();
        photo.setRoverName(rover);
        photo.setCameraName(camera);
        photo.setEarthDate(date);
        photo.setId(id);
        photo.setImageSrc(mockUrl);

        try {
            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
            ByteArrayInputStream fakeImage = new ByteArrayInputStream(fakeImageData.getBytes());
            when(mockConnection.getInputStream()).thenReturn(fakeImage);
        } catch(IOException e) {
            fail("Unexpected exception was thrown while mocking in Download Nominal");
        }

        cut.download(photo);

        Path filePath = Paths.get(fileLocation);
        assertTrue(Files.exists(filePath)); 
        assertTrue(Files.isRegularFile(filePath));
        
        String fileContents = null;
        try {
            fileContents = Files.readString(filePath);
        } catch(IOException e) {
            fail("Unexpected exception was thrown while file checking in Download Nominal");
        }

        assertEquals(fileContents,fakeImageData);
    }

    @Test
    public void testDownloadIOException() {
        String dirName = BASE_DIR + "/download/exception";
        ImageDownloader cut = new ImageDownloader(dirName);

        String rover  = "dog";
        String camera = "iphone";
        String date   = "2020-06-04";
        int id     = 321;
        String fileLocation = dirName + "/" + rover + "_" + camera + "_" 
                                            + date + "_" + id + ".jpg";

        PhotoMetadata photo = new PhotoMetadata();
        photo.setRoverName(rover);
        photo.setCameraName(camera);
        photo.setEarthDate(date);
        photo.setId(id);
        photo.setImageSrc(mockUrl);

        try {
            when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
            when(mockConnection.getInputStream()).thenThrow(new IOException("Fake Exception"));
        } catch(IOException e) {
            fail("Unexpected exception was thrown while mocking in Download Nominal");
        }

        cut.download(photo);

        Path filePath = Paths.get(fileLocation);
        assertFalse(Files.exists(filePath)); 
    }
}
