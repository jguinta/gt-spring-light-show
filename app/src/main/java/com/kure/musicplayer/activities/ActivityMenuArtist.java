package com.kure.musicplayer.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.R;
import com.kure.musicplayer.kMP;

import java.util.ArrayList;

/**
 * Shows a menu with all the artists of the songs
 * on SongList, allowing the user to choose one of
 * them and going to a specific artist menu.
 *
 */
public class ActivityMenuArtist extends ActivityMaster
implements OnItemClickListener {

	/**
	 * All the possible items the user can select on this menu.
	 *
	 * Will be initialized with default values on `onCreate`.
	 */
	public static ArrayList<String> items;

	/**
	 * List that will be populated with all the items.
	 *
	 * Look for it inside the res/layout xml files.
	 */
	ListView listView;

	/**
	 * Called when the activity is created for the first time.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu_artists);

		// This enables the "Up" button on the top Action Bar
		// Note that it returns to the parent Activity, specified
		// on `AndroidManifest`
		ActionBar actionBar = getActionBar();
		if (actionBar != null)
			actionBar.setDisplayHomeAsUpEnabled(true);

		// List to be populated with items
		listView = (ListView)findViewById(R.id.activity_menu_artists_list);

		items = kMP.songs.getArtists();

		// Adapter that will convert from Strings to List Items
		final ArrayAdapter<String> adapter = new ArrayAdapter<String>
				(this, android.R.layout.simple_list_item_1, items);

		// Filling teh list with all the items
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(this);
	}

	/**
	 * Will react to the user selecting an item.
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		// We can only handle the user choice from now on
		// if we've successfully scanned the songs from the
		// device.
		if (! kMP.songs.isInitialized())
			return;

		String selectedArtist = items.get(position);

		// Now we'll decide between going to two screens:
		// - If the artist has only one album, show all his songs already!
		// - If has more than one, show list of albums first.
		ArrayList<String> albumsByArtist = kMP.songs.getAlbumsByArtist(selectedArtist);

		if (albumsByArtist.size() == 1) {

			kMP.musicList = kMP.songs.getSongsByArtist(selectedArtist);

			Intent intent = new Intent(this, ActivityListSongs.class);

			intent.putExtra("title", selectedArtist);

			startActivity(intent);
		}
		else {
			// We'll send the artist name to display his albums
			// as an extra to this new Activity
			Intent intent = new Intent(this, ActivityListAlbums.class);

			intent.putExtra("artist", selectedArtist);

			startActivity(intent);
		}
	}

	/**
	 * When destroying the Activity.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();

		// Need to clear all the items otherwise
		// they'll keep adding up.
		items.clear();
	}
}
