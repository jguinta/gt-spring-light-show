package com.example.joe.mbls.spotify;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

import java.util.ArrayList;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;

public class SpotifySearchArtistActivity extends AppCompatActivity implements OnClickListener {

    private SpotifyService spotifyService;
    private ListView listView;
    private SpotifyArrayAdapter adapter;
    private ArrayList<Artist> items = new ArrayList<Artist>();
    private Player mPlayer;

    // Request code that will be used to verify if the result comes from correct activity
    // Can be any integer
    private static final int REQUEST_CODE = 1337;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_artist);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        ab.setTitle("Search Artists");
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true);

        SpotifyApplication spotifyApplication = ((SpotifyApplication) getApplicationContext());
        spotifyService = spotifyApplication.getSpotifyService();
        mPlayer = spotifyApplication.getPlayer();

        listView = (ListView) findViewById(R.id.responseView);


        adapter = new SpotifyArrayAdapter<Artist>(this, R.layout.track_row, items);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist artist = (Artist) parent.getItemAtPosition(position);
                Intent artistIntent = new Intent(getApplicationContext(), SpotifyDisplayAlbums.class);
                artistIntent.putExtra("artist_name", artist.name);
                artistIntent.putExtra("artist", artist.id);
                startActivity(artistIntent);

            }
        });

        Button queryButton = (Button) findViewById(R.id.queryButton);
        // because we implement OnClickListener we only have to pass "this"
        // (much easier)
        queryButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        // detect the view that was "clicked"
        switch (view.getId()) {
            case R.id.queryButton:
                new RetrieveArtistTask(((EditText) findViewById(R.id.artist)).getText().toString()).execute();
                break;
        }
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
    protected void onDestroy() {
        super.onDestroy();
    }


    class RetrieveArtistTask extends AsyncTask<Void, Void, String> {

        private String artist;

        public RetrieveArtistTask(String artist) {
            this.artist = artist;
        }

        protected void onPreExecute() {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            adapter.clear();
        }

        protected String doInBackground(Void... urls) {


            ArtistsPager artistsPager = spotifyService.searchArtists(artist);
            //tracks = new ArrayList<Track>();
            items.addAll(artistsPager.artists.items);
            return "success";
        }


        protected void onPostExecute(String result) {
            findViewById(R.id.progressBar).setVisibility(View.GONE);
            adapter.notifyDataSetChanged();
        }
    }


}