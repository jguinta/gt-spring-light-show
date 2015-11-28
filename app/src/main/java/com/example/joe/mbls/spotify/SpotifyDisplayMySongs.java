package com.example.joe.mbls.spotify;

import android.app.Activity;
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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.MainMenu;
import com.R;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.SavedTrack;
import retrofit.http.QueryMap;

public class SpotifyDisplayMySongs extends AppCompatActivity {

    private SpotifyService spotifyService;
    private ListView listView;
    private SpotifyArrayAdapter adapter;
    private ArrayList<SavedTrack> tracks = new ArrayList<SavedTrack>();
    private Player mPlayer;
    private ImageButton btnPlayPause;

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_items_list);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        SpotifyApplication spotifyApplication = ((SpotifyApplication) getApplicationContext());
        spotifyService = spotifyApplication.getSpotifyService();
        mPlayer = spotifyApplication.getPlayer();

        listView = (ListView) findViewById(R.id.responseView);

        registerForContextMenu(listView);


        adapter = new SpotifyArrayAdapter<>(this, R.layout.track_row, tracks);
        listView.setAdapter(adapter);

        new RetrieveMySongsTask().execute();
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
                startActivity(new Intent(this, MainActivity.class));
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
        SavedTrack track = (SavedTrack) adapter.getItem(info.position);
        switch(item.getItemId()) {
            case R.id.add_track_to_playlist:
                mPlayer.queue(track.track.uri);
                return true;
            case R.id.track_play_now:
                mPlayer.clearQueue();
                mPlayer.skipToNext();
                mPlayer.queue(track.track.uri);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }






    class RetrieveMySongsTask extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            adapter.clear();
        }

        protected String doInBackground(Void... urls) {

            HashMap<String, Object> map = new HashMap();
            int i = 0;
            map.put("limit", 50);
            map.put("offset", i);
            Pager<SavedTrack> tracksPager = spotifyService.getMySavedTracks(map);
            tracks.addAll(tracksPager.items);
            while (tracksPager.next != null) {

                map.put("offset", i += 50);
                tracksPager = spotifyService.getMySavedTracks(map);
                tracks.addAll(tracksPager.items);
            }

            return "success";
        }


        protected void onPostExecute(String result) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
}