package com.mbaker.nasa.api.rover;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import com.mbaker.nasa.api.rover.data.PhotoQueryResponse;
import com.mbaker.nasa.api.rover.data.PhotoMetadata;

public class RoverApiWrapper {
    private static final String ROVER_URL = "https://api.nasa.gov/mars-photos/api/v1/rovers";

    //Query Param key names
    private static final String QUERY_PARAM_EARTH_DATE = "earth_date";
    private static final String QUERY_PARAM_CAMERA     = "camera";
    private static final String QUERY_PARAM_API_KEY    = "api_key";
    //Not using paging, but it is an available query parameter
    //private static final String QUERY_PARAM_PAGE       = "page";

    //User's NASA API key
    private String apiKey;

    //REST facilitators
    private final JacksonJsonProvider jacksonJsonProvider;
    private Client client;

    public RoverApiWrapper(String apiKey) {
        jacksonJsonProvider = new JacksonJaxbJsonProvider();
        client = ClientBuilder.newClient(new ClientConfig(jacksonJsonProvider));
        this.apiKey = apiKey;
    }

    public PhotoQueryResponse query(String date, String rover) {
        return query(date, rover, null);
    }

    public PhotoQueryResponse query(String date, String rover, String camera) {

        WebTarget webTarget = client.target(ROVER_URL + "/" + rover + "/photos")
                                    .queryParam(QUERY_PARAM_EARTH_DATE, date)
                                    .queryParam(QUERY_PARAM_API_KEY, apiKey);
        if(camera != null) {
            webTarget = webTarget.queryParam(QUERY_PARAM_CAMERA, camera);
        }

        PhotoQueryResponse response = webTarget.request(MediaType.APPLICATION_JSON)
                                     .get(PhotoQueryResponse.class);

        return response;
    }
}
