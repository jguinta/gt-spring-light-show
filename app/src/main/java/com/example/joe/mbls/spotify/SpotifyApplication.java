package com.example.joe.mbls.spotify;

import android.app.Application;
import android.provider.ContactsContract;

import com.spotify.sdk.android.player.Player;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by JOE on 9/21/2015.
 */
public class SpotifyApplication extends Application {

    private SpotifyService spotifyService = null;
    private Player mPlayer  = null;
    private String userId  = null;
    private DatagramSocket socket;
    private InetAddress inetAddress;

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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void establishConnection() {
        try {

            socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(6454));
            inetAddress = InetAddress.getByName("255.255.255.255");
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }
}
