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

            /*ArtNet artnet = new ArtNet();

            try {
                artnet.start();
                artnet.getNodeDiscovery().addListener(ConnectReceiverActivity.this);
                artnet.startNodeDiscovery();

            } catch (SocketException e) {
                throw new AssertionError(e);
            } catch (ArtNetException e) {
                throw new AssertionError(e);
            }*/
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
                    //socket.receive((datagrampacket1));
                    final String IP = String.valueOf(datagrampacket1.getAddress()).substring(1);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            nodeIds.add(IP);
                            adapter.notifyDataSetChanged();
                        }
                    });



                    byte abyte0[] = new byte[41];
                    abyte0[0] = 52;
                    abyte0[1] = 115;
                    abyte0[2] = 112;
                    abyte0[3] = 112;
                    abyte0[4] = (byte) 200;
                    abyte0[5] = (byte) 0;
                    abyte0[6] = (byte) 255;
                    abyte0[7] = (byte)0;
                    abyte0[8] = (byte)0;
                    abyte0[9] = (byte)0;
                    abyte0[10] = (byte)0;
                    abyte0[11] = (byte)0;
                    abyte0[12] = (byte)0;
                    abyte0[13] = (byte)0;
                    abyte0[14] = (byte)0;
                    abyte0[15] = (byte)0;
                    abyte0[16] = (byte)0;
                    abyte0[17] = (byte)0;
                    abyte0[18] = (byte)0;
                    abyte0[19] = (byte)0;
                    abyte0[20] = (byte)0;
                    abyte0[21] = (byte)0;
                    abyte0[22] = (byte)0;
                    abyte0[23] = (byte)0;
                    abyte0[24] = (byte)0;
                    abyte0[25] = (byte)0;
                    abyte0[26] = (byte)0;
                    abyte0[27] = (byte)0;
                    abyte0[28] = (byte)0;
                    abyte0[29] = (byte)0;
                    abyte0[30] = (byte)0;
            abyte0[31] = (byte)0;
            abyte0[32] = (byte)0;
            abyte0[33] = (byte)0;
            abyte0[34] = (byte)0;
            abyte0[35] = (byte)0;
            abyte0[36] = (byte)0;
            abyte0[37] = (byte)0;
            abyte0[38] = (byte)0;
            abyte0[39] = (byte)0;
            abyte0[40] = 32;

            DatagramPacket udpSendPacket = new DatagramPacket(abyte0, abyte0.length, datagrampacket.getAddress(), 6454);
            ArtNetPacket artPollReplyPacket = new ArtNetPacketParser().parse(datagrampacket1);
            ArtNetNode artNetNode = new ArtNetNode();
            artNetNode.extractConfig((ArtPollReplyPacket) artPollReplyPacket);

                    while (true) {
                        ArtDmxPacket dmx = new ArtDmxPacket();
                        dmx.setUniverse(artNetNode.getSubNet(), artNetNode.getDmxOuts()[0]);
                        dmx.setSequenceID(sequenceID % 255);
                        byte[] buffer = new byte[510];
                        for (int i = 0; i < buffer.length; i++) {
                            buffer[i] =
                                    (byte) (Math.sin(sequenceID * 0.05 + i * 0.8) * 127 + 128);
                        }
                        dmx.setDMX(buffer, buffer.length);
                       // artnet.unicastPacket(dmx, netLynx.getIPAddress());
                        DatagramPacket packet = new DatagramPacket(dmx.getData(), dmx
                                .getLength(), artNetNode.getIPAddress(), 6454);
                        socket.send(packet);
                        dmx.setUniverse(netLynx.getSubNet(),
                                netLynx.getDmxOuts()[1]);
                      //  artnet.unicastPacket(dmx, netLynx.getIPAddress());
                        packet = new DatagramPacket(dmx.getData(), dmx
                                .getLength(), artNetNode.getIPAddress(), 6454);
                        socket.send(packet);
                    }
            //socket.send(udpSendPacket);

        } catch (Exception e) {
            System.out.println(e);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    nodeIds.add("error2");
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

