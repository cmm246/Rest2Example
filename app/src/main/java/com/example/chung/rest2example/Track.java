package com.example.chung.rest2example;

import com.google.gson.annotations.SerializedName;

/**
 * Created by chung on 7/29/16.
 */
public class Track {

    @SerializedName("title")
    private String scTrackTitle;

    @SerializedName("artwork_url")
    private String scTrackArtworkURL;

    public String getTitle() {
        return scTrackTitle;
    }

    public String getArtworkURL() {
        return scTrackArtworkURL;
    }

}
