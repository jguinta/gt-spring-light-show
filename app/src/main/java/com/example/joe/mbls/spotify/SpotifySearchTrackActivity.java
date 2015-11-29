package com.example.joe.mbls.spotify;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.MainMenu;
import com.R;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;

public class SpotifySearchTrackActivity extends AppCompatActivity implements
        PlayerNotificationCallback, OnClickListener {

    private SpotifyService spotifyService;
    private ListView listView;
    private SpotifyArrayAdapter adapter;
    private ArrayList<Track> tracks = new ArrayList<Track>();
    private Player mPlayer;

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_track);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Search Tracks");
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        SpotifyApplication spotifyApplication = ((SpotifyApplication) getApplicationContext());
        spotifyService = spotifyApplication.getSpotifyService();
        mPlayer = spotifyApplication.getPlayer();

        listView = (ListView) findViewById(R.id.responseView);

        registerForContextMenu(listView);


        adapter = new SpotifyArrayAdapter<>(this, R.layout.track_row, tracks);
        listView.setAdapter(adapter);

        Button queryButton = (Button) findViewById(R.id.queryButton);
        // because we implement OnClickListener we only have to pass "this"
        // (much easier)
        queryButton.setOnClickListener(this);
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
              //  intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            case R.id.spotify_go_home:
                startActivity(new Intent(this, SpotifyMain.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.track_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Track track = (Track) adapter.getItem(info.position);
        switch(item.getItemId()) {
            case R.id.add_track_to_playlist:
                mPlayer.queue(track.uri);
                return true;
            case R.id.track_play_now:
                mPlayer.clearQueue();
                mPlayer.skipToNext();
                mPlayer.queue(track.uri);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        // detect the view that was "clicked"
        switch (view.getId()) {
            case R.id.queryButton:
                new RetrieveSongTask(((EditText) findViewById(R.id.song)).getText().toString()).execute();
                break;
        }
    }



    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {
        Log.d("SpotifyMain", "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(ErrorType errorType, String errorDetails) {
        Log.d("SpotifyMain", "Playback error received: " + errorType.name());
    }




    class RetrieveSongTask extends AsyncTask<Void, Void, String> {
        private String API_URL = "https://api.spotify.com/v1/search";
        private Exception exception;


        private String song;

        public RetrieveSongTask(String song) {
            this.song = song;
        }

        protected void onPreExecute() {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            adapter.clear();
        }

        protected String doInBackground(Void... urls) {


            TracksPager tracksPager = spotifyService.searchTracks(song);
            tracks.addAll(tracksPager.tracks.items);
            return "success";
        }


        protected void onPostExecute(String result) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
}