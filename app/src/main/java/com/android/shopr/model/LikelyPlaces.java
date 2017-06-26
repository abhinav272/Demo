package com.android.shopr.model;

/**
 * Created by Abhinav on 25/06/17.
 */
public class LikelyPlaces {

    private String placeName;
    private String placeId;
    private String placeFullName;

    public String getPlaceFullName() {
        return placeFullName;
    }

    public void setPlaceFullName(String placeFullName) {
        this.placeFullName = placeFullName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }
}
