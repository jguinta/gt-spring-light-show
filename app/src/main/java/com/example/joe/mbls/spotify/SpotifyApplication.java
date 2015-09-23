package com.example.joe.mbls.spotify;

import android.app.Application;

import com.spotify.sdk.android.player.Player;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by JOE on 9/21/2015.
 */
public class SpotifyApplication extends Application {

    private SpotifyService spotifyService;
    private Player mPlayer;

    public void setSpotifyService(SpotifyService spotifyService) {
        this.spotifyService = spotifyService;
    }

    public SpotifyService getSpotifyService() {
        return spotifyService;
    }


    public void setPlayer(Player mPlayer) {
        this.mPlayer = mPlayer;
    }

    public Player getPlayer() {
        return mPlayer;
    }

}
