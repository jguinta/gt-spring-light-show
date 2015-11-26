package com.example.joe.mbls.spotify;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.MainMenu;
import com.R;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.PlayerStateCallback;

public class MainActivity extends AppCompatActivity implements PlayerNotificationCallback {



    private Player mPlayer;

    private Button btnSearchTracks;
    private Button btnSearchArtists;
    private Button btnMySongs;
    private Button btnMyPlaylists;
    private ImageButton btnPlayPause;
    private ImageButton btnSkipNext;
    private ImageButton btnSkipPrevious;
    private ImageButton btnShuffle;
    private ImageButton btnRepeat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();

        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);


        mPlayer = ((SpotifyApplication) getApplication()).getPlayer();
        mPlayer.addPlayerNotificationCallback(this);


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
                mPlayer.skipToPrevious();
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


    }

    @Override
    public void onPlaybackEvent(PlayerNotificationCallback.EventType eventType, PlayerState playerState) {
        if (playerState.playing) {
            btnPlayPause.setTag("pause");
            btnPlayPause.setImageResource(R.drawable.pause);
        }

    }


    @Override
    public void onPlaybackError(PlayerNotificationCallback.ErrorType errorType, String errorDetails) {
        Log.d("MainActivity", "Playback error received: " + errorType.name());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_spotify_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_home:
                Intent intent = new Intent(this, MainMenu.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            case R.id.spotify_go_home:
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}