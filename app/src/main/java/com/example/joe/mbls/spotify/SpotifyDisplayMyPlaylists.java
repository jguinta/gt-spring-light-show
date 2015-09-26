package com.example.joe.mbls.spotify;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
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
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.SavedTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TracksPager;

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