package com.joe.artnet;

/**
 * Created by Joey on 11/5/2015.
 */
public interface DmxLight {

    public void setChannel(int channel, byte value);
    public byte getRed();
    public void setRed(byte value);
    public byte getBlue();
    public void setBlue(byte value);
    public byte getGreen();
    public void setGreen(byte value);
    public byte getBrightness();
    public void setBrightness(byte value);
    public boolean isFlickering();
    public byte getChannel(int channel);
    public int getNumChannels();
}
