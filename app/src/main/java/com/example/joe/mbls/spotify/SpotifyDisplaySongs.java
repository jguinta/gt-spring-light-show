package com.example.joe.mbls.spotify;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.spotify.sdk.android.player.Player;

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;

public class SpotifyDisplaySongs extends Activity  {

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

        SpotifyApplication spotifyApplication = ((SpotifyApplication) getApplicationContext());
        spotifyService = spotifyApplication.getSpotifyService();
        mPlayer = spotifyApplication.getPlayer();
        userId = spotifyApplication.getUserId();


        listView = (ListView) findViewById(R.id.responseView);

        registerForContextMenu(listView);


        adapter = new SpotifyArrayAdapter<TrackSimple>(this, R.layout.track_row, tracks);
        listView.setAdapter(adapter);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("album")) {
                Log.d("displaysongs", "contains album");
                new RetrieveAlbumSongsTask(extras.getString("album")).execute();
            } else if (extras.containsKey("playlist")) {
                new RetrievePlaylistSongsTask(extras.getString("playlist"), extras.getString("playlist_owner")).execute();
            }
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
                mPlayer.play(track.uri);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    class RetrieveAlbumSongsTask extends AsyncTask<Void, Void, String> {


        private String album;

        public RetrieveAlbumSongsTask(String album) {
            this.album = album;
        }

        protected void onPreExecute() {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            adapter.clear();
        }

        protected String doInBackground(Void... urls) {

            Log.d("display songs", "getting album tracks for " + album);
            Pager<Track> tracksPager = spotifyService.getAlbumTracks(album);
            //tracks = new ArrayList<Track>();
            tracks.addAll(tracksPager.items);
            return "success";
        }


        protected void onPostExecute(String result) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
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


            Pager<PlaylistTrack> tracksPager = spotifyService.getPlaylistTracks(owner, playlist);
            for (PlaylistTrack playlistTrack: tracksPager.items) {
                tracks.add(playlistTrack.track);
            }
            return "success";
        }


        protected void onPostExecute(String result) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }
}