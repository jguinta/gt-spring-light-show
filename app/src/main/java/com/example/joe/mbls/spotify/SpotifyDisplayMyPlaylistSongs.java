package com.example.joe.mbls.spotify;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.MainMenu;
import com.R;
import com.spotify.sdk.android.player.Player;

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.TrackSimple;

public class SpotifyDisplayMyPlaylistSongs extends AppCompatActivity {

    private SpotifyService spotifyService;
    private ListView listView;
    private SpotifyArrayAdapter adapter;
    private ArrayList<TrackSimple> tracks = new ArrayList<TrackSimple>();
    private Player mPlayer;
    private String userId;

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
        ab.setTitle(getIntent().getStringExtra("playlist_name"));
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        SpotifyApplication spotifyApplication = ((SpotifyApplication) getApplicationContext());
        spotifyService = spotifyApplication.getSpotifyService();
        mPlayer = spotifyApplication.getPlayer();
        userId = spotifyApplication.getUserId();


        listView = (ListView) findViewById(R.id.responseView);

        registerForContextMenu(listView);


        adapter = new SpotifyArrayAdapter<TrackSimple>(this, R.layout.track_row, tracks);
        listView.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        new RetrievePlaylistSongsTask(extras.getString("playlist"), extras.getString("playlist_owner")).execute();

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
            //    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
        TrackSimple track = (TrackSimple) adapter.getItem(info.position);
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





    class RetrievePlaylistSongsTask extends AsyncTask<Void, Void, String> {


        private String playlist;
        private String owner;

        public RetrievePlaylistSongsTask(String playlist, String owner) {
            this.playlist = playlist;
            this.owner = owner;
        }

        protected void onPreExecute() {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            adapter.clear();
        }

        protected String doInBackground(Void... urls) {

            HashMap<String, Object> map = new HashMap();
            int i = 0;
            map.put("limit", 50);
            map.put("offset", i);


            Pager<PlaylistTrack> tracksPager = spotifyService.getPlaylistTracks(owner, playlist);
            for (PlaylistTrack playlistTrack: tracksPager.items) {
                tracks.add(playlistTrack.track);
            }

            while (tracksPager.next != null) {
                i += 50;
                map.put("offset", i);
                tracksPager = spotifyService.getPlaylistTracks(owner, playlist, map);
                for (PlaylistTrack playlistTrack: tracksPager.items) {
                    tracks.add(playlistTrack.track);
                }
            }

            return "success";
        }


        protected void onPostExecute(String result) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
}