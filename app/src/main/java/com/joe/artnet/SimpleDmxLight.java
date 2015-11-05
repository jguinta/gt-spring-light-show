package com.joe.artnet;

/**
 * Created by Joey on 11/5/2015.
 */
public class SimpleDmxLight implements DmxLight {
    private byte[] channels = new byte[4];
    private int numChannels = 4;
    private boolean isFlickering;

    public SimpleDmxLight() {}

    public void setChannel(int channel, byte value) {
        channels[channel] = value;
    }
    public byte getBrightness() {
        return channels[0];
    }
    public void setBrightness(byte value) {
        channels[0] = value;
    }
    public byte getRed() {
        return channels[1];
    }
    public void setRed(byte value) {
        channels[1] = value;
    }
    public byte getGreen() {
        return channels[2];
    }
    public void setGreen(byte value) {
        channels[2] = value;
    }
    public byte getBlue() {
        return channels[3];
    }
    public void setBlue(byte value) {
        channels[3] = value;
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
