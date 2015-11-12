package com.musicalgorithm;

import android.util.Log;

/**
 * Created by yvinogradov on 11/4/2015.
 */
public class MusicAlgorithm {

    public static int i = 0;

    public static float getAverageAmplitude(short[] inputStream) {
        float[] mag = new float[inputStream.length / 2];
        float average = 0f;
        int j = 0;
        for (int i = 0; i < inputStream.length - 1; i += 2) {
            mag[j] = (float) (Math.abs(inputStream[i]) + Math.abs(inputStream[i + 1])) / 2;
            average += mag[j++];
        }
        average /= mag.length;
        
        return average;
    }

    public static float getOpacity(short[] inputStream) {

        float[] mag = new float[inputStream.length / 2];
        float average = 0f;
        int j = 0;
        for (int i = 0; i < inputStream.length - 1; i += 2) {
            mag[j] = (float) (Math.abs(inputStream[i]) + Math.abs(inputStream[i + 1])) / 2;
            average += mag[j];
            j++;
        }
        average /= mag.length;
        float opacity = 10f;

        if (average < 0) opacity = 0f;
        else opacity = (average/16000) * 128;

        return opacity;
    }


}
