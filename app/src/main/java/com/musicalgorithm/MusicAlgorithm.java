package com.musicalgorithm;

/**
 * Created by yvinogradov on 11/4/2015.
 */
public class MusicAlgorithm {

    public static float[] getMetrics(short[] inputStream) {
        float[] mag = new float[inputStream.length / 2];
        float average = 0f;
        int j=0;
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
        float[] colors = new float[4];
        float opacity = 10f;
        /*if (!(amplitude == 0f)) {
            opacity = (amplitude / 5000) * 128;
        }*/
        if (amplitude <= 5000) {
            colors[0] = 0;
            colors[1] = amplitude/5000 * 128;
            colors[2] = 127 + amplitude/5000 * 128;
            colors[3] = (amplitude / 5000) * 128;
        } else if (amplitude <= 10000) {
            colors[0] = amplitude/10000 * 128;
            colors[1] =127 + amplitude/10000 * 128;
            colors[2] = 255-amplitude/10000 * 128;
            colors[3] = (amplitude / 10000) * 128;
        } else if (amplitude <= 15000) {
            colors[0] = 127 + amplitude/15000 * 128;
            colors[1] =255 - amplitude/15000 * 128;
            colors[2] = 128-amplitude/15000 * 128;
            colors[3] = (amplitude / 15000) * 128;
        } else {
            colors[0] = 255;
            colors[1] = 255;
            colors[2] = 255;
            colors[3] = (127 + (amplitude / 16000) * 128) > 255 ? 255 : (127 + (amplitude / 16000) * 128);
        }

        return colors;

    }


}
