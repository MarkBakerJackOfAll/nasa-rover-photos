package com.mbaker.nasa.api.rover.data;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PhotoMetadata {
    //Photo Unique info
    private int id;
    private URL imageSrc;

    //Date
    private int sol;
    @JsonProperty("earth_date")
    private String earthDate;

    //Camera detail
    private int cameraId;
    private String cameraName;
    private String cameraFullName;

    //Rover detail
    private int roverId;
    private String roverName;

    @JsonProperty("camera")
    public void upackCamera(Map<String, Object> camera) {
        cameraId = (int) camera.get("id");
        cameraName = (String) camera.get("name");
        cameraFullName = (String) camera.get("full_name");
        //Choosing to skip rover_id as it should be a duplicate from the rover section
    }

    @JsonProperty("rover")
    public void upackRover(Map<String, Object> rover) {
        roverId = (int) rover.get("id");
        roverName = (String) rover.get("name");
        //Choosing to skip landing_date, launch_date and status
    }

    @JsonProperty("img_src")
    public void setImageSrc(String imageSrcString) throws MalformedURLException {
        imageSrc = new URL(imageSrcString);
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public URL getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(final URL imageSrc) {
        this.imageSrc = imageSrc;
    }

    public int getSol() {
        return sol;
    }

    public void setSol(final int sol) {
        this.sol = sol;
    }

    public String getEarthDate() {
        return earthDate;
    }

    public void setEarthDate(final String earthDate) {
        this.earthDate = earthDate;
    }

    public int getCameraId() {
        return cameraId;
    }

    public void setCameraId(final int cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(final String cameraName) {
        this.cameraName = cameraName;
    }

    public String getCameraFullName() {
        return cameraFullName;
    }

    public void setCameraFullName(final String cameraFullName) {
        this.cameraFullName = cameraFullName;
    }

    public int getRoverId() {
        return roverId;
    }

    public void setRoverId(final int roverId) {
        this.roverId = roverId;
    }

    public String getRoverName() {
        return roverName;
    }

    public void setRoverName(final String roverName) {
        this.roverName = roverName;
    }

    public String toString() {
        String output = "";
        output += "id:             " + id + "\n";
        output += "imageSrc:       " + imageSrc + "\n";
        output += "sol:            " + sol + "\n";
        output += "earthDate:      " + earthDate + "\n";
        output += "cameraId:       " + cameraId + "\n";
        output += "cameraName:     " + cameraName + "\n";
        output += "cameraFullName: " + cameraFullName + "\n";
        output += "roverId:        " + roverId + "\n";
        output += "roverName:      " + roverName + "\n";
        return output;
    }
}
