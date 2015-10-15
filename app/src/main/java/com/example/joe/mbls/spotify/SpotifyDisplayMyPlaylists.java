package com.example.joe.mbls.spotify;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.R;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.PlaylistSimple;

public class SpotifyDisplayMyPlaylists extends Activity {

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
                Intent intent = new Intent(getApplicationContext(), SpotifyDisplaySongs.class);
                intent.putExtra("playlist_owner", playlist.owner.id);
                intent.putExtra("playlist", playlist.id);
                startActivity(intent);
            }
        });


        new RetrieveMyPlaylistsTask().execute();
    }



    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }


    class RetrieveMyPlaylistsTask extends AsyncTask<Void, Void, String> {
        private String API_URL = "https://api.spotify.com/v1/search";
        private Exception exception;

        protected void onPreExecute() {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            adapter.clear();
        }

        protected String doInBackground(Void... urls) {

            Pager<PlaylistSimple> playlists = spotifyService.getPlaylists(userId);
            items.addAll(playlists.items);
            return "success";
        }


        protected void onPostExecute(String result) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
}