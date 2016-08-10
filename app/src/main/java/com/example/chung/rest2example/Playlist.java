package com.example.chung.rest2example;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by chung on 7/29/16.
 */
public class Playlist {

    @SerializedName("tracks")
    private List<Track> scTracks;

    @SerializedName("title")
    private String scPlaylistTitle;

    public List<Track> getTracks() {
        return scTracks;
    }

    public String getTitle() {
        return scPlaylistTitle;
    }

}
