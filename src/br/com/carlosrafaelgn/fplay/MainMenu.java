package br.com.carlosrafaelgn.fplay;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import br.com.carlosrafaelgn.fplay.activity.ActivityHost;

/**
 * Created by evgeniy on 9/22/15.
 */
public class MainMenu extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

        Button button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityHost.class);
                startActivity(intent);
            }
        });

    }



}
