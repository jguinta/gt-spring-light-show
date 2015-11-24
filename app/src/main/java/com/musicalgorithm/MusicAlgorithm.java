package com.musicalgorithm;

import ddf.minim.analysis.BeatDetect;

/**
 * Created by yvinogradov on 11/4/2015.
 */
public class MusicAlgorithm {

    public static float[] getMetrics(short[] inputStream) {
        float[] mag = new float[inputStream.length / 2];
        float[] floatStream = new float[inputStream.length];
        float average = 0f;
        int j = 0;
        for (int i = 0; i < inputStream.length - 1; i += 2) {
            mag[j] = (float) (Math.abs(inputStream[i]) + Math.abs(inputStream[i + 1])) / 2;
            floatStream[i] = (float) inputStream[i];
            floatStream[i + 1] = (float) inputStream[i + 1];
            average += mag[j];
            j++;
        }
        BeatDetect beatDetect = new BeatDetect();
        beatDetect.detect(floatStream);

        average /= mag.length;
        float[] metrics = getColorsAndOpacity(average);
        metrics[4] = beatDetect.isOnset() ? 1f : 0f;
        return metrics;
    }

    public static float[] getColorsAndOpacity(float amplitude) {
        float[] colors = new float[5];
        float opacity = 10f;
        /*if (!(amplitude == 0f)) {
            opacity = (amplitude / 5000) * 128;
        }*/
        colors[3] = (amplitude / 16000) * 128;
        if (amplitude <= 1000) {
            colors[0] = 75 * amplitude / 1000;
            colors[1] = 0;
            colors[2] = amplitude / 1000 * 130;
        } else if (amplitude <= 2000) {
            colors[0] = amplitude / 2000 * 128;
            colors[1] = 0;
            colors[2] = amplitude / 2000 * 128;
        } else if (amplitude <= 3000) {
            colors[0] = 255 * amplitude / 3000;
            colors[1] = 0;
            colors[2] = 255 * amplitude / 3000;
        } else if (amplitude <= 4000) {
            colors[0] = 0;
            colors[1] = 255 * amplitude / 4000;
            colors[2] = 255 * amplitude / 4000;
        } else if (amplitude <= 5000) {
            colors[0] = 0;
            colors[1] = 153 * amplitude / 5000;
            colors[2] = 0;
        } else if (amplitude <= 6000) {
            colors[0] = 102 * amplitude / 6000;
            colors[1] = 255 * amplitude / 6000;
            colors[2] = 178 * amplitude / 6000;
        } else if (amplitude <= 7000) {
            colors[0] = 255 * amplitude / 7000;
            colors[1] = 51 * amplitude / 7000;
            colors[2] = 153 * amplitude / 7000;
        } else if (amplitude <= 8000) {
            colors[0] = 102 * amplitude / 8000;
            colors[1] = 102 * amplitude / 8000;
            colors[2] = 255 * amplitude / 8000;
        } else if (amplitude <= 9000) {
            colors[0] = 255 * amplitude/9000;
            colors[1] = 51 *amplitude/9000;
            colors[2] = 255 * amplitude/9000;
        } else if (amplitude <= 10000) {
            colors[0] = 0;
            colors[1] = 204 * amplitude/10000;
            colors[2] = 102* amplitude/10000;
        } else if (amplitude <= 11000) {
            colors[0] = 153 * amplitude/11000;
            colors[1] = 204 * amplitude/11000;
            colors[2] = 255 * amplitude/11000;
        } else if (amplitude <= 12000) {
            colors[0] = 153 * amplitude/12000;
            colors[1] = 0;
            colors[2] = 0;
        } else if (amplitude <= 13000) {
            colors[0] = 255 * amplitude / 13000;
            colors[1] = 128 * amplitude / 13000;
            colors[2] = 0;
        } else if (amplitude <= 14000) {
            colors[0] = 255 * amplitude / 14000;
            colors[1] = 51 * amplitude / 14000;
            colors[2] = 51 * amplitude / 14000;
        } else if (amplitude <= 15000) {
            colors[0] = 255 * amplitude / 15000;
            colors[1] = 255 * amplitude / 15000;
            colors[2] = 0;
        } else {
            colors[0] = 255;
            colors[1] = 255;
            colors[2] = 255;
            colors[3] = (127 + (amplitude / 16000) * 128) > 255 ? 255 : (127 + (amplitude / 16000) * 128);
        }
        colors[3] = colors[3] > 128 ? colors[3] : amplitude / 16000 * 128;//too hacky bubble it up if it works better
        return colors;

    }


}
