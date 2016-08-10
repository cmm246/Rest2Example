package com.example.chung.rest2example;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.login.LoginManager;

import java.util.ArrayList;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by chung on 7/29/16.
 */
public class SoundCloudActivity extends AppCompatActivity {

    private static final String TAG = "SoundCloudActivity";
    private ArrayList<String> bands;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sound_cloud);

        Intent intent = getIntent();
        bands = intent.getStringArrayListExtra(MainActivity.EXTRA_MESSAGE);

        // Load playlists and tracks into view
        getList();
    }

    // Get data from SoundCloud and populate views
    private void getList() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Config.API_URL).build();
        SoundCloudApiService soundCloudApiService = restAdapter.create(SoundCloudApiService.class);
        soundCloudApiService.getTracks(new Callback<Playlist[]>() {
            @Override
            public void success(Playlist[] playlist, Response response) {
                load(playlist);
            }
            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Error: " + error);
            }
        });
    }

    private void load(Playlist[] playlist) {
        SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();
        String playlistTitle;
        List<Track> tracks;
        for(int i = 0; i < playlist.length; i++) {
            List<Track> scTracks = new ArrayList<>();
            playlistTitle = playlist[i].getTitle();
            tracks = playlist[i].getTracks();
            for (int j=0; j < tracks.size(); j++) {
                //Log.e("TRACK ==== ", tracks.get(j).getTitle().toString());
                for (String b : bands) {
                    if (tracks.get(j).getTitle().toString().toLowerCase().contains(b.toLowerCase()))
                        scTracks.add(tracks.get(j));
                }
            }
            sectionAdapter.addSection(new PlaylistSection(this, playlistTitle, scTracks));
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(sectionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_soundcloud, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            logout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Logout of app
    private void logout() {
        LoginManager.getInstance().logOut();
        Intent login = new Intent(this, MainActivity.class);
        startActivity(login);
        finish();
    }

}
