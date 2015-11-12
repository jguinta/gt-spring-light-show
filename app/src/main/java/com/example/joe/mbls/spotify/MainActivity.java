package com.example.joe.mbls.spotify;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.R;
import com.joe.artnet.DmxPacket;
import com.joe.artnet.SimpleDmxLight;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;

public class MainActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

    // TODO: Replace with your client ID
    private static final String CLIENT_ID = "7229dfb6591f476bacb0f4cd936c7061";
    // TODO: Replace with your redirect URI
    private static final String REDIRECT_URI = "spring-light-show-login://callback";

    private Player mPlayer;
    private SpotifyService spotifyService;
    private SpotifyApi spotifyApi;

    private Button btnSearchTracks;
    private Button btnSearchArtists;
    private Button btnMySongs;
    private Button btnMyPlaylists;
    private ImageButton btnPlayPause;
    private ImageButton btnSkipNext;
    private ImageButton btnSkipPrevious;
    private ImageButton btnShuffle;
    private ImageButton btnRepeat;

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearchTracks = (Button) findViewById(R.id.search_tracks);
        btnSearchTracks.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SpotifySearchTrackActivity.class);
                startActivity(intent);
            }
        });

        btnSearchArtists = (Button) findViewById(R.id.search_artists);
        btnSearchArtists.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SpotifySearchArtistActivity.class);
                startActivity(intent);
            }
        });

        btnMySongs = (Button) findViewById(R.id.my_songs);
        btnMySongs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SpotifyDisplayMySongs.class);
                startActivity(intent);
            }
        });

        btnMyPlaylists = (Button) findViewById(R.id.my_playlists);
        btnMyPlaylists.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SpotifyDisplayMyPlaylists.class);
                startActivity(intent);
            }
        });

        btnPlayPause = (ImageButton) findViewById(R.id.activity_now_playing_play);
        btnPlayPause.setTag("pause");
        btnPlayPause.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnPlayPause.getTag().equals("play")) {
                    mPlayer.resume();
                    btnPlayPause.setTag("pause");
                    btnPlayPause.setImageResource(R.drawable.pause);
                } else {
                    mPlayer.pause();
                    btnPlayPause.setTag("play");
                    btnPlayPause.setImageResource(R.drawable.play);
                }
            }

        });

        btnSkipNext = (ImageButton) findViewById(R.id.activity_now_playing_skip_next);
        btnSkipNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.skipToNext();
            }
        });

        btnSkipPrevious = (ImageButton) findViewById(R.id.activity_now_playing_skip_previous);
        btnSkipPrevious.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.skipToNext();
            }
        });

        btnRepeat = (ImageButton) findViewById(R.id.activity_now_playing_repeat);
        btnRepeat.setTag("false");
        btnRepeat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnRepeat.getTag().equals("false")) {
                    mPlayer.setRepeat(true);
                    btnRepeat.setTag("true");
                    btnRepeat.setImageResource(R.drawable.ic_menu_repeat_on);

                } else {
                    mPlayer.setRepeat(false);
                    btnRepeat.setImageResource(R.drawable.ic_menu_repeat_off);
                    btnRepeat.setTag("false");
                }

            }
        });

        btnShuffle = (ImageButton) findViewById(R.id.activity_now_playing_shuffle);
        btnShuffle.setTag("false");
        btnShuffle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnShuffle.getTag().equals("false")) {
                    mPlayer.setShuffle(true);
                    btnShuffle.setImageResource(R.drawable.ic_menu_shuffle_on);
                    btnShuffle.setTag("true");
                } else {
                    mPlayer.setShuffle(false);
                    btnShuffle.setImageResource(R.drawable.ic_menu_shuffle_off);
                    btnShuffle.setTag("false");
                }

            }
        });

        AuthenticationRequest.Builder builder = new AuthenticationRequest.Builder(CLIENT_ID,
                AuthenticationResponse.Type.TOKEN,
                REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private", "user-library-read", "playlist-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);


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


                SimpleAudioController audioController = new SimpleAudioController(dmxPacket);
                audioController.establishConnection();
                Player.Builder builder = new Player.Builder(playerConfig);
                builder.setAudioController(audioController);



               // mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                mPlayer = Spotify.getPlayer(builder, this, new Player.InitializationObserver() {
                    @Override
                    public void onInitialized(Player player) {
                        mPlayer.addConnectionStateCallback(MainActivity.this);
                        mPlayer.addPlayerNotificationCallback(MainActivity.this);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                    }
                });

                spotifyApplication.setPlayer(mPlayer);
                spotifyApplication.setSpotifyService(spotifyService);
                new RetrieveUserIdTask().execute();


            }
        }
    }


    @Override
    public void onLoggedIn() {
        Log.d("MainActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("MainActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("MainActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("MainActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("MainActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("MainActivity", "Playback event received: " + eventType.name());
    }


    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
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