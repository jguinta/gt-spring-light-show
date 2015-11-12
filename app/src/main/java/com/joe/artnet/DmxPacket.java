package com.joe.artnet;

import java.util.ArrayList;

/**
 * Created by Joey on 11/5/2015.
 */
public class DmxPacket {
    byte[] dmx;
    ArrayList<DmxLight> lights;
    int currentLightIndex = 0;
    int currentPacketIndex = 4;

    public DmxPacket() {
        dmx = new byte[41];
        dmx[0] = 52;
        dmx[1] = 115;
        dmx[2] = 112;
        dmx[3] = 112;
        dmx[40] = 32;

        lights = new ArrayList<>();
    }

    public DmxPacket(DmxPacket clone) {
        this.dmx = clone.dmx;
        this.lights = clone.lights;
        this.currentPacketIndex = clone.currentPacketIndex;
        this.currentLightIndex = clone.currentLightIndex;

    }


    public int addLight(DmxLight light) {
        lights.add(light);
        return currentLightIndex++;
    }


    public void setChannelValue(int channel, int value) {
        if (channel > 35 || channel < 0) throw new IndexOutOfBoundsException();
        dmx[channel] = (byte) value;
    }

    public void setBrightness(byte value) {
        for (DmxLight light : lights) {
            light.setBrightness(value);
        }
    }
    public void setBrightness(int light, byte value) {
        lights.get(light).setBrightness(value);
    }

    public void setRed(byte value) {
        for (DmxLight light : lights) {
            light.setRed(value);
        }
    }
    public void setRed(int light, byte value) {
        lights.get(light).setRed(value);
    }

    public void setGreen(byte value) {
        for (DmxLight light : lights) {
            light.setGreen(value);
        }
    }
    public void setGreen(int light, byte value) {
        lights.get(light).setGreen(value);
    }

    public void setBlue(byte value) {
        for (DmxLight light : lights) {
            light.setBlue(value);
        }
    }
    public void setBlue(int light, byte value) {
        lights.get(light).setBlue(value);
    }



    public byte[] getDmxPacket() {
        return dmx;
    }

    public byte[] buildDmxPacket() {
        for (DmxLight light : lights) {
            for (int i = 0; i < light.getNumChannels(); i++) {
                dmx[currentPacketIndex++] = light.getChannel(i);
            }
        }
        currentPacketIndex = 4;
        return dmx;
    }

    public void setDmxPacket(byte[] dmx) {
        this.dmx = dmx;
    }


}
