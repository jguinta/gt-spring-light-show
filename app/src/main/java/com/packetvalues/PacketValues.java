package com.packetvalues;

/**
 * Created by michael-leon on 11/5/2015.
 */

public class PacketValues {

	private int brightness;
	private int r;
	private int g;
	private int b;


	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness*127;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}
}
