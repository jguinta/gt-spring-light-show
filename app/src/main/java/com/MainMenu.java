package com;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.joe.mbls.spotify.SimpleAudioController;
import com.example.joe.mbls.spotify.SpotifyApplication;
import com.example.joe.mbls.spotify.SpotifyMain;
import com.joe.artnet.DmxPacket;
import com.joe.artnet.ShortWrapper;
import com.joe.artnet.SimpleDmxLight;
import com.joe.artnet.StripLight;
import com.ringdroid.RingdroidSelectActivity;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

/**
 * Created by evgeniy on 9/22/15.
 */
public class MainMenu extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {


    private static final String CLIENT_ID = "7229dfb6591f476bacb0f4cd936c7061";
    private static final String REDIRECT_URI = "spring-light-show-login://callback";

    private Player mPlayer;
    private SpotifyService spotifyService;
    private SpotifyApi spotifyApi;

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;


    private DatagramSocket socket;
    private  DmxPacket defaultPacket;
    private BlockingQueue<DmxPacket> dmxPackets = new LinkedBlockingQueue<>();
    private BlockingQueue<ShortWrapper> raw = new LinkedBlockingQueue<>();
    private InetAddress inetAddress;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button spotifyPlayer = (Button) findViewById(R.id.spotifyPlayer);

        spotifyPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (((SpotifyApplication)getApplicationContext()).getPlayer() == null ||
                        !((SpotifyApplication)getApplicationContext()).getPlayer().isInitialized()) {
                    Log.e("MAINACTIVITY", "REAUTHENTICATING........................");
                    AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                            AuthenticationResponse.Type.TOKEN,
                            REDIRECT_URI);
                    builder.setScopes(new String[]{"user-read-private", "user-library-read", "playlist-read-private", "streaming"});
                    AuthenticationRequest request = builder.build();

                    AuthenticationClient.openLoginActivity(MainMenu.this, REQUEST_CODE, request);
                } else {
                    Intent intent = new Intent(getApplicationContext(), SpotifyMain.class);
                    startActivity(intent);
                }


            }
        });


        establishConnection();

        //float[] x = MusicAlgorithm.getMetrics(mBuffer);
        defaultPacket=new DmxPacket();
        SimpleDmxLight light = new SimpleDmxLight();
        defaultPacket.addLight(light);
        //Log.e("Sample Player Values", x[0] + ", " + x[1] + ", " + x[2] + ", " + x[3]);
        DmxPacket result = new DmxPacket(defaultPacket);
        result.setRed((byte) 150);
        result.setGreen((byte) 0);
        result.setBlue((byte) 0);
        result.setBrightness((byte) 255);
        try {
            dmxPackets.put(result);
        } catch (InterruptedException e) {
            System.out.println("broke here");
        }
        new SendDmxPacket().execute();

        Button googlePlayer = (Button) findViewById(R.id.googlePlayer);
        googlePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RingdroidSelectActivity.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                Config playerConfig = new Config(this, response.getAccessToken(), CLIENT_ID);
                SpotifyApplication spotifyApplication = ((SpotifyApplication)getApplicationContext());
                spotifyApi = new SpotifyApi();
                spotifyApi.setAccessToken(playerConfig.oauthToken);
                spotifyService = spotifyApi.getService();


                // Default DmxPacket
                DmxPacket dmxPacket = new DmxPacket();

                // Add all lights here
                SimpleDmxLight light = new SimpleDmxLight();
                dmxPacket.addLight(light);

                StripLight stripLight = new StripLight();
                dmxPacket.addLight(stripLight);


                SimpleAudioController audioController = new SimpleAudioController(dmxPacket);
                audioController.establishConnection();
                Player.Builder builder = new Player.Builder(playerConfig);
                builder.setAudioController(audioController);



                // mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                mPlayer = Spotify.getPlayer(builder, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer.addConnectionStateCallback(MainMenu.this);
                        mPlayer.addPlayerNotificationCallback(MainMenu.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("SpotifyMain", "Could not initialize player: " + throwable.getMessage());
                    }
                });

                spotifyApplication.setPlayer(mPlayer);
                spotifyApplication.setSpotifyService(spotifyService);
                new RetrieveUserIdTask().execute();

                startActivity(new Intent(getApplicationContext(), SpotifyMain.class));


            }
        }
    }


    private class SendDmxPacket extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... packet) {
            try {
                //   Log.d("sndDmxPacket", "Sending DmxPacket ");
                byte[] bytes = dmxPackets.take().buildDmxPacket();
                Log.e("sndDmxPacket", "values = " + bytes[4] + " " + bytes[5] + " " + bytes[6] + " " + bytes[7]);
                DatagramPacket udpSendPacket = new DatagramPacket(bytes, bytes.length, inetAddress, 6454);
                socket.send(udpSendPacket);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return null;
        }
    }

    @Override
    public void onLoggedIn() {
        Log.d("SpotifyMain", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("SpotifyMain", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("SpotifyMain", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("SpotifyMain", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("SpotifyMain", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(PlayerNotificationCallback.EventType eventType, PlayerState playerState) {

    }


    @Override
    public void onPlaybackError(PlayerNotificationCallback.ErrorType errorType, String errorDetails) {
        Log.d("SpotifyMain", "Playback error received: " + errorType.name());
    }


    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

    class RetrieveUserIdTask extends AsyncTask<Void, Void, String> {



        protected String doInBackground(Void... urls) {
            ((SpotifyApplication) getApplicationContext()).setUserId(spotifyService.getMe().id);
            return null;
        }


    }



}
