package com.certuit.base.service.base;

import com.certuit.base.config.APIConstants;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.PlaceAutocompleteRequest;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.*;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GoogleService {
    GeoApiContext geoApiContext = new GeoApiContext.Builder().apiKey(APIConstants.GOOGLE_API_KEY).build();

    public GoogleService(){}

    public AutocompletePrediction[] getAutocompleteResult(String address)  {
        try {
            AutocompletePrediction[] places = PlacesApi.placeAutocomplete(geoApiContext, address, new PlaceAutocompleteRequest.SessionToken()).
                    components(ComponentFilter.country("MX")).await();
            return places;
        }catch (Exception e) {
            return new AutocompletePrediction[0];
        }
    }

    public DirectionsResult getRoute(LatLng startPlace, LatLng finishPlace) throws InterruptedException, ApiException, IOException {
        DirectionsResult routes = DirectionsApi.newRequest(geoApiContext)
                .origin(new com.google.maps.model.LatLng(startPlace.lat, startPlace.lng))
                .destination(new com.google.maps.model.LatLng(finishPlace.lat, finishPlace.lng))
                .mode(TravelMode.DRIVING)
                .await();
        return routes;
    }

    public PlaceDetails getPlaceDetail(String placeId)  {
        try {
            PlaceDetails place = PlacesApi.placeDetails(geoApiContext, placeId).await();
            return place;
        }catch (Exception e) {
            return null;
        }
    }

}
