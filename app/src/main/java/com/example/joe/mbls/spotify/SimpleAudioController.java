package com.example.joe.mbls.spotify;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.Build;
import android.util.Log;

import com.musicalgorithm.MusicAlgorithm;
import com.spotify.sdk.android.player.AudioController;
import com.spotify.sdk.android.player.AudioRingBuffer;

public final class SimpleAudioController implements AudioController {

    private final Object mMutex = new Object();

    private final AudioRingBuffer mAudioBuffer = new AudioRingBuffer(81920);

    private volatile boolean mActive = true;

    private volatile boolean mSuspended = true;

    private AudioTrack mAudioTrack;


    @Override
    public void start() {
        final Thread nThread = new Thread() {
            public void run() {
                setName("MyAudioController");
                pfxThread();
            }
        };
        nThread.start();
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

            float[] metrics = MusicAlgorithm.getMetrics(pendingFrames);
            Log.d("Metrics", metrics[0] + " , " + metrics[1] + " , " + metrics[2] + " , " + metrics[3]+ ","+ metrics[4]);
            //here
            if (itemsRead > 0) {
                synchronized (mMutex) {
                    pfWriteToTrack(pendingFrames, itemsRead);
                }
                mAudioBuffer.remove(itemsRead);
            }
        }
    }



}