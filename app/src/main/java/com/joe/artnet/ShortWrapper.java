package com.joe.artnet;

/**
 * Created by Joey on 11/11/2015.
 */
public class ShortWrapper {
    public short[] data;
    public int numFrames;

    public ShortWrapper(short[] data, int numFrames) {
        this.data = data;
        this.numFrames = numFrames;
    }
}
