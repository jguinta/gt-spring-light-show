package com.musicalgorithm;

/**
 * Created by yvinogradov on 11/4/2015.
 */
public class MusicAlgorithm {

    public static float[] getMetrics(short[] inputStream) {
        float[] mag = new float[inputStream.length / 2];
        float average = 0f;
        int j = 0;
        for (int i = 0; i < inputStream.length - 1; i += 2) {
            mag[j] = (float) (Math.abs(inputStream[i]) + Math.abs(inputStream[i + 1])) / 2;
            average += mag[j];
            j++;
        }
        average /= mag.length;
        float[] metrics = getColorsAndOpacity(average);

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
            colors[0] = 82 * amplitude / 1000;
            colors[1] = 0;
            colors[2] = amplitude / 1000 * 94;
        } else if (amplitude <= 2000) {
            colors[0] = amplitude / 2000 * 128;
            colors[1] = 0;
            colors[2] = amplitude / 2000 * 128;
        } else if (amplitude <= 3000) {
            colors[0] = 0;
            colors[1] = 13 * amplitude/2000;
            colors[2] = 94 * amplitude / 3000;
        } else if (amplitude <= 4000) {
            colors[0] = 0;
            colors[1] = 94 * amplitude / 4000;
            colors[2] = 2 * amplitude / 4000;
        } else if (amplitude <= 5000) {
            colors[0] = 246 * amplitude/5000;
            colors[1] = 255 * amplitude / 5000;
            colors[2] = 0;
        } else if (amplitude <= 6000) {
            colors[0] = 255 * amplitude / 6000;
            colors[1] = 208 * amplitude / 6000;
            colors[2] = 0;
        } else if (amplitude <= 7000) {
            colors[0] = 255 * amplitude / 7000;
            colors[1] =4*amplitude/7000;
            colors[2] = 0;
        } else if (amplitude <= 8000) {
            colors[0] = 255 * amplitude / 8000;
            colors[1] = 105 * amplitude / 8000;
            colors[2] = 252 * amplitude / 8000;
        } else if (amplitude <= 9000) {
            colors[0] = 112 * amplitude/9000;
            colors[1] = 105 *amplitude/9000;
            colors[2] = 255 * amplitude/9000;
        } else if (amplitude <= 10000) {
            colors[0] = 105 * amplitude/10000;
            colors[1] = 255 * amplitude/10000;
            colors[2] = 110* amplitude/10000;
        } else if (amplitude <= 11000) {
            colors[0] = 255 * amplitude/11000;
            colors[1] = 255 * amplitude/11000;
            colors[2] = 105 * amplitude/11000;
        } else if (amplitude <= 12000) {
            colors[0] = 255 * amplitude/12000;
            colors[1] = 228 * amplitude/12000;
            colors[2] = 140 * amplitude/12000;
        } else if (amplitude <= 13000) {
            colors[0] = 255 * amplitude / 13000;
            colors[1] = 140 * amplitude / 13000;
            colors[2] =  140 * amplitude / 13000;
        } else if (amplitude <= 14000) {
            colors[0] = 159 * amplitude / 14000;
            colors[1] = 255 * amplitude / 14000;
            colors[2] = 140 * amplitude / 14000;
        } else if (amplitude <= 15000) {
            colors[0] = 140 * amplitude / 15000;
            colors[1] = 255 * amplitude / 15000;
            colors[2] = 247 * amplitude/15000;
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
