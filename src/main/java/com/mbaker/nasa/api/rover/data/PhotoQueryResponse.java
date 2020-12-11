package com.mbaker.nasa.api.rover.data;

import java.util.List;

public class PhotoQueryResponse {
    public List<PhotoMetadata> photos;

    public List<PhotoMetadata> getPhotos() {
        return photos;
    }

    public void setPhotos(final List<PhotoMetadata> photos) {
        this.photos = photos;
    }
}
