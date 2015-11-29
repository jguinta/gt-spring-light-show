package com.example.joe.mbls.spotify;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.joe.artnet.DmxPacket;
import com.joe.artnet.ShortWrapper;
import com.musicalgorithm.MusicAlgorithm;
import com.spotify.sdk.android.player.AudioController;
import com.spotify.sdk.android.player.AudioRingBuffer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public final class SimpleAudioController implements AudioController, AudioTrack.OnPlaybackPositionUpdateListener {
    private final DmxPacket defaultPacket;
    private final Object mMutex = new Object();

    private BlockingQueue<DmxPacket> dmxPackets = new LinkedBlockingQueue<>();
    private BlockingQueue<ShortWrapper> raw = new LinkedBlockingQueue<>();
    private DatagramSocket socket;
    private InetAddress inetAddress;
    private Random r = new Random();
    private final AudioRingBuffer mAudioBuffer = new AudioRingBuffer(81920);
    private volatile boolean mActive = true;

    private volatile boolean mSuspended = true;

    private AudioTrack mAudioTrack;

    private int framesProcessed = 0;

    public SimpleAudioController(DmxPacket dmxPacket) {
        this.defaultPacket = dmxPacket;
    }

    public void establishConnection() {
        try {

            socket = new DatagramSocket(null);
            socket.setReuseAddress(true);
            socket.bind(new InetSocketAddress(6454));
            inetAddress = InetAddress.getByName("255.255.255.255");
        } catch (Exception e) {
            System.out.print(e);
        }
    }

    @Override
    public void start() {
        final Thread nThread = new Thread() {
            public void run() {
                setName("MyAudioController");
                pfxThread();
            }
        };
        nThread.start();

        //final AnalysisThread analysisThread = new AnalysisThread();
        //analysisThread.start();

    }

    @Override
    public void stop() {
        if (mSuspended) {
            notifyThreadResume();
        }
        mActive = false;
    }

    @Override
    public int onAudioDataDelivered(short[] frames, int numberOfFrames, int rate, int channel) {
        if (mAudioTrack != null &&
                (rate != mAudioTrack.getSampleRate() || channel != mAudioTrack.getChannelCount())) {
            synchronized (mMutex) {
                mAudioTrack.release();
                mAudioTrack = null;
            }
        }
        if (mAudioTrack == null) {
            pfCreateAudioTrack(AudioManager.STREAM_MUSIC, rate, channel);
        }


       // raw.add(new ShortWrapper(frames, numberOfFrames));



        return mAudioBuffer.write(frames, numberOfFrames);

    }

    @Override
    public void onAudioFlush() {
        if (mAudioTrack != null) {
            synchronized (mMutex) {
                mAudioTrack.pause();
                mAudioTrack.flush();
                mAudioTrack.release();
                mAudioTrack = null;
            }
        }
        mAudioBuffer.clear();
        dmxPackets.clear();
        notifyThreadResume();
    }

    @Override
    public void onAudioPaused() {
        if (mAudioTrack != null) {
            mAudioTrack.pause();
        }
        notifyThreadPause();
    }

    @Override
    public void onAudioResumed() {
        if (mAudioTrack != null) {
            mAudioTrack.play();
        }
        notifyThreadResume();
    }

    @Override
    public void onPeriodicNotification(AudioTrack track) {
       // Log.d("onPeriodicNotification", "Queue size = " + dmxPackets.size());
       // new SendDmxPacket().execute();
    }

    @Override
    public void onMarkerReached(AudioTrack track) {
    }


    private synchronized void notifyThreadResume() {
        mSuspended = false;
        notifyAll();
    }

    private synchronized void notifyThreadPause() {
        mSuspended = true;
        notifyAll();
    }

    private void pfCreateAudioTrack(int type, int rate, int channel) {
        final int outChannel = (channel == 1
                ? AudioFormat.CHANNEL_OUT_MONO
                : AudioFormat.CHANNEL_OUT_STEREO);
        final int size = AudioFormat.ENCODING_PCM_16BIT;
        final int length =
                AudioTrack.getMinBufferSize(rate, outChannel, size) * 2;

        synchronized (mMutex) {
            mAudioTrack = new AudioTrack(type, rate, outChannel, size, length,
                    AudioTrack.MODE_STREAM);
            mAudioTrack.setPositionNotificationPeriod(4096);
            mAudioTrack.setPlaybackPositionUpdateListener(this);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mAudioTrack.setVolume(AudioTrack.getMaxVolume());
            } else {
                mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
            }
            mAudioTrack.play();
        }
    }

    private int pfWriteToTrack(short[] frames, int numberOfFrames) {
        if (mAudioTrack != null && (mAudioTrack.getPlayState() == AudioTrack.PLAYSTATE_PLAYING)) {
            mAudioTrack.write(frames, 0, numberOfFrames);
        }
        return 0;
    }



    private void pfxThread() {
        final short[] pendingFrames = new short[4096];
        while (mActive) {
            synchronized (this) {
                while (mSuspended) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        mSuspended = mActive = false;
                    }
                }
            }
            final int itemsRead = mAudioBuffer.peek(pendingFrames);
            if (itemsRead != 0 && itemsRead != 4096) Log.d("SAC", "ITEMS READ: " + itemsRead);
            if (itemsRead > 0) {
                float[] x = MusicAlgorithm.getMetrics(pendingFrames);

                DmxPacket result = new DmxPacket(defaultPacket);
                result.setRed((byte) x[0]);
                result.setGreen((byte) x[1]);
                result.setBlue((byte) x[2]);
                result.setBrightness((byte) x[3]);
                try {
                   dmxPackets.put(result);
               } catch (Exception e) {
                   System.out.print("interrupt");
               }
                new SendDmxPacket().execute();
                synchronized (mMutex) {
                    pfWriteToTrack(pendingFrames, itemsRead);
                }
                mAudioBuffer.remove(itemsRead);
            }
        }
    }

    private class AnalysisThread extends Thread {


        @Override
        public void run() {
            // Computations here
            while (true) {
                try {
                    ShortWrapper packet = raw.take();

                    // Computations here
                    float[] x = MusicAlgorithm.getMetrics(packet.data);

                    DmxPacket result = new DmxPacket(defaultPacket);
                    result.setRed((byte) x[0]);
                    result.setGreen((byte) x[1]);
                    result.setBlue((byte) x[2]);
                    result.setBrightness((byte) x[3]);
                    dmxPackets.put(result);
                    framesProcessed += packet.numFrames;
                } catch (InterruptedException e) {
                    System.out.println("interrupt");
                }
            }
        }
    }

    private class SendDmxPacket extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... packet) {
            try {
             //   Log.d("sndDmxPacket", "Sending DmxPacket ");
                byte[] bytes = dmxPackets.take().buildDmxPacket();
             //  Log.d("sndDmxPacket", "values = " + bytes[4] + " " + bytes[5] + " " + bytes[6] + " " + bytes[7]);
                DatagramPacket udpSendPacket = new DatagramPacket(bytes, bytes.length, inetAddress, 6454);
                socket.send(udpSendPacket);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
            return null;
        }
    }
}