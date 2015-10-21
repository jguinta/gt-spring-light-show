package com.joe.artnet;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.R;
import com.example.joe.mbls.spotify.SpotifyArrayAdapter;

import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import artnet4j.ArtNet;
import artnet4j.ArtNetException;
import artnet4j.ArtNetNode;
import artnet4j.events.ArtNetDiscoveryListener;
import artnet4j.packets.ArtDmxPacket;

/**
 * Created by JOE on 10/8/2015.
 */

public class ConnectReceiverActivity extends Activity implements ArtNetDiscoveryListener {


    private ArtNetNode netLynx;

    private int sequenceID;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> nodeIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_connect_receiver);


        listView = (ListView) findViewById(R.id.artnet_nodes);

        nodeIds.add("hi");

        adapter = new ArrayAdapter<String>(this, R.layout.track_row, R.id.rowTextView, nodeIds);
        listView.setAdapter(adapter);

        new SearchAndSendArtnet().execute();
    }

    @Override
    public void discoveredNewNode(ArtNetNode node) {
        if (netLynx == null) {
            netLynx = node;
            System.out.println("found net lynx");
            nodeIds.add(node.getLongName() + node.getIPAddress());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        }
    }

    @Override
    public void discoveredNodeDisconnected(ArtNetNode node) {
        System.out.println("node disconnected: " + node);
        if (node == netLynx) {
            netLynx = null;
        }

    }

    @Override
    public void discoveryCompleted(List<ArtNetNode> nodes) {
        System.out.println(nodes.size() + " nodes found:");
        for (ArtNetNode n : nodes) {
            System.out.println(n);
            nodeIds.add(n.getLongName() + " " + n.getIPAddress());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void discoveryFailed(Throwable t) {
        System.out.println("discovery failed");
    }



    class SearchAndSendArtnet extends AsyncTask<Void, Void, String> {

        protected void onPreExecute() {
            //adapter.clear();
        }

        protected String doInBackground(Void... urls) {

            ArtNet artnet = new ArtNet();


            try {
                artnet.start();
                artnet.getNodeDiscovery().addListener(ConnectReceiverActivity.this);
                artnet.startNodeDiscovery();

            } catch (SocketException e) {
                throw new AssertionError(e);
            } catch (ArtNetException e) {
                throw new AssertionError(e);
            }
            return "ok";
        }


        protected void onPostExecute(String result) {
           // adapter.notifyDataSetChanged();
        }
    }
}

