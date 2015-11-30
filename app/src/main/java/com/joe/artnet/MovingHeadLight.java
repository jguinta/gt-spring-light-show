package com.joe.artnet;

/**
 * Created by Joey on 11/30/2015.
 */
public class MovingHeadLight implements DmxLight {

    private byte[] channels = new byte[5];

    private int numChannels = 5;
    private boolean isFlickering;



    public void setAxes(byte x, byte y) {
        channels[1] = x;
        channels[2] = y;
    }

    public void setStrobe(byte x) {
        channels[4] = x;
    }

    public void setColor(byte x) {
        channels[3] = x;
    }

    public void setChannel(int channel, byte value) {
        channels[channel] = value;
    }
    public byte getBrightness() {
        throw new UnsupportedOperationException();
    }
    public void setBrightness(byte value) {

    }
    public byte getRed() {
        throw new UnsupportedOperationException();
    }
    public void setRed(byte value) {
    }
    public byte getGreen() {
        throw new UnsupportedOperationException();
    }
    public void setGreen(byte value) {
    }
    public byte getBlue() {
        throw new UnsupportedOperationException();
    }
    public void setBlue(byte value) {
    }

    public boolean isFlickering() {
        return isFlickering;
    }
    public byte getChannel(int channel) {
        return channels[channel];
    }
    public int getNumChannels() {
        return numChannels;
    }

}
