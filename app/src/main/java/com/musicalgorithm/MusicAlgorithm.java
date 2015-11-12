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
        float[] retMetrics = new float[3];
        float[] inputFloat = new float[inputStream.length];
        float[] mag = new float[inputStream.length / 2];
        float average = 0f;
        int j = 0;
        for (int i = 0; i < inputStream.length - 1; i += 2) {
            mag[j] = (float) (Math.abs(inputStream[i]) + Math.abs(inputStream[i + 1])) / 2;
            inputFloat[i] = (float)inputStream[i];
            inputFloat[i+1] = (float)inputStream[i+1];
            average += mag[j];
            j++;
        }
        average /= mag.length;
        float opacity = 10f;
        if(!(average==0f)){
            opacity = (average/1000) * 8;
        }
        return opacity;
    }


}
