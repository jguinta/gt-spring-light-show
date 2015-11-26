package com.joe.artnet;

/**
 * Created by Joey on 11/24/2015.
 */
public class StripLight implements DmxLight {

    private byte[] channels = new byte[4];
    private int numChannels = 4;
    private boolean isFlickering;

    public StripLight() {
        channels[0] = (byte) 210;
    }

    public void scaleChannels(double scalar) {
        channels[1] = (byte) (channels[1] * scalar);
        channels[2] = (byte) (channels[2] * scalar);
        channels[3] = (byte) (channels[3] * scalar);
    }
    public void setChannel(int channel, byte value) {
        channels[channel] = value;
    }
    public byte getBrightness() {
        throw new UnsupportedOperationException();
    }
    public void setBrightness(byte value) {
        throw new UnsupportedOperationException();
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

    public void setStaticColor(byte value) {
        channels[0] = value;
    }

    public void setRGBMixMode() {
        channels[0] =  (byte) 210;
    }

    public void setStaticStrobeSpeed(byte value) {
        channels[2] = value;
    }

}
