package com.example.chung.rest2example;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by chung on 7/29/16.
 */
public interface SoundCloudApiService {

    @GET("/users/" + Config.USER_ID + "/playlists?client_id=" + Config.CLIENT_ID)
    void getTracks(Callback<Playlist[]> cb);
    //Playlist[] getTracks();

}
