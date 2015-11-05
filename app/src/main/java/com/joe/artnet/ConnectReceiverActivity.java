package com.joe.artnet;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.R;
import com.example.joe.mbls.spotify.SpotifyArrayAdapter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import artnet4j.ArtNet;
import artnet4j.ArtNetException;
import artnet4j.ArtNetNode;
import artnet4j.ArtNetServer;
import artnet4j.events.ArtNetDiscoveryListener;
import artnet4j.packets.ArtDmxPacket;
import artnet4j.packets.ArtNetPacket;
import artnet4j.packets.ArtNetPacketParser;
import artnet4j.packets.ArtPollReplyPacket;

/**
 * Created by JOE on 10/8/2015.
 */

public class ConnectReceiverActivity extends Activity implements ArtNetDiscoveryListener {


    private ArtNetNode netLynx;

    private int sequenceID;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> nodeIds = new ArrayList<>();
    private DatagramSocket socket;

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
        nodeIds.add("hello: " + System.currentTimeMillis());
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




            int j = 0;
            while (j < 20) {
                j++;
                if (socket == null) {
                    try {

                        socket = new DatagramSocket(null);
                        socket.setReuseAddress(true);
                        socket.bind(new InetSocketAddress(6454));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nodeIds.add("created socket");
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (Exception e) {
                        System.out.print(e);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                nodeIds.add("error 1");
                                adapter.notifyDataSetChanged();
                            }
                        });

                    }

                }



                try {

                    InetAddress inetAddress = InetAddress.getByName("255.255.255.255");
                    byte abyte[] = "1sch \r\n".getBytes();
                    DatagramPacket datagrampacket = new DatagramPacket(abyte, abyte.length, inetAddress, 6454);
                    socket.send(datagrampacket);
                    DatagramPacket datagrampacket1;
                    byte abyte1[] = new byte[1024];
                    datagrampacket1 = new DatagramPacket(abyte1, abyte1.length, inetAddress, 6454);
                    socket.setSoTimeout(100);
                    socket.receive(datagrampacket1);
                    socket.receive((datagrampacket1));

                    DmxPacket dmxPacket = new DmxPacket();
                    DmxLight dmxLight = new SimpleDmxLight();
                    dmxPacket.addLight(dmxLight);
                    dmxPacket.setBlue((byte) 200);
                    dmxPacket.setBrightness((byte) 100);

                    byte[] abyte0 = dmxPacket.buildDmxPacket();
                    DatagramPacket udpSendPacket = new DatagramPacket(abyte0, abyte0.length, datagrampacket.getAddress(), 6454);

                    socket.send(udpSendPacket);

                    Random r = new Random();

                    while (true) {
                        dmxPacket.setBlue((byte) r.nextInt(255));
                        dmxPacket.setRed((byte) r.nextInt(255));
                        dmxPacket.setGreen((byte) r.nextInt(255));
                        dmxPacket.setBrightness((byte) r.nextInt(255));
                        abyte0 = dmxPacket.buildDmxPacket();

                        udpSendPacket = new DatagramPacket(abyte0, abyte0.length, datagrampacket.getAddress(), 6454);
                        socket.send(udpSendPacket);
                    }


                } catch (final Exception e) {
                    System.out.println(e);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nodeIds.add(e.getMessage());
                            nodeIds.add(e.toString());
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

            }
         return "ok";
    }


    protected void onPostExecute(String result) {
           // adapter.notifyDataSetChanged();
        }
    }
}

