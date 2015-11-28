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
import android.widget.AdapterView;
import android.widget.ListView;

import com.MainMenu;
import com.R;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;
import java.util.HashMap;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SavedTrack;

public class SpotifyDisplayMyPlaylists extends AppCompatActivity {

    private SpotifyService spotifyService;
    private ListView listView;
    private SpotifyArrayAdapter adapter;
    private ArrayList<PlaylistSimple> items = new ArrayList();
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
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);
        SpotifyApplication spotifyApplication = ((SpotifyApplication) getApplicationContext());
        spotifyService = spotifyApplication.getSpotifyService();
        mPlayer = spotifyApplication.getPlayer();
        userId = spotifyApplication.getUserId();

        listView = (ListView) findViewById(R.id.responseView);
        registerForContextMenu(listView);


        adapter = new SpotifyArrayAdapter<PlaylistSimple>(this, R.layout.track_row, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaylistSimple playlist = (PlaylistSimple) adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), SpotifyDisplayMyPlaylistSongs.class);
                intent.putExtra("playlist_owner", playlist.owner.id);
                intent.putExtra("playlist", playlist.id);
                startActivity(intent);
            }
        });


        new RetrieveMyPlaylistsTask().execute();
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
             //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                return true;
            case R.id.spotify_go_home:
                startActivity(new Intent(this, MainActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    class RetrieveMyPlaylistsTask extends AsyncTask<Void, Void, String> {
        private String API_URL = "https://api.spotify.com/v1/search";
        private Exception exception;

        protected void onPreExecute() {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            adapter.clear();
        }

        protected String doInBackground(Void... urls) {

            HashMap<String, Object> map = new HashMap();
            int i = 0;
            map.put("limit", 50);
            map.put("offset", i);


            Pager<PlaylistSimple> playlists = spotifyService.getPlaylists(userId);
            items.addAll(playlists.items);

            while (playlists.next != null) {
                i += 50;
                map.put("offset", i);
                playlists = spotifyService.getPlaylists(userId, map);
                items.addAll(playlists.items);
            }

            return "success";
        }


        protected void onPostExecute(String result) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
}