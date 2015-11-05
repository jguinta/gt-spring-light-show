package com.packetvalues

/**
 * Created by michael-leon on 11/5/2015.
 */

public class PacketValues {

	private int brightness;
	private int r;
	private int g;
	private int b;


	public static void setBrightness(float opacity) {
		brightness = opacity*127;
	}

	public static void setR() {

	}

	public static void setG() {

	}

	public static void setB() {

	}

	public static void getBrightness() {
		return brightness;
	}

	public static void getR() {
		return r;
	}

	public static void getG() {
		return g;
	}

	public static void getB() {
		return b;
	}

}
