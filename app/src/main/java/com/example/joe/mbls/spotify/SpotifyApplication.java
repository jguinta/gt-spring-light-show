package com.example.joe.mbls.spotify;

import android.app.Application;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by JOE on 9/21/2015.
 */
public class SpotifyApplication extends Application {

    private SpotifyService spotifyService;

    public void setSpotifyService(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    public SpotifyService getSpotifyService() {
        return spotifyService;
    }
}
