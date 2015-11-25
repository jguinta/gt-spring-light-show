package com;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.joe.mbls.spotify.MainActivity;
import com.ringdroid.RingdroidSelectActivity;

/**
 * Created by evgeniy on 9/22/15.
 */
public class MainMenu extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button spotifyPlayer = (Button) findViewById(R.id.spotifyPlayer);

        spotifyPlayer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        /*Button artnetButton = (Button) findViewById(R.id.artnet_connect_button);
        artnetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ConnectReceiverActivity.class);
                startActivity(intent);
            }
        });*/

        Button googlePlayer = (Button) findViewById(R.id.googlePlayer);
        googlePlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RingdroidSelectActivity.class);
                startActivity(intent);
            }
        });

    }



}
