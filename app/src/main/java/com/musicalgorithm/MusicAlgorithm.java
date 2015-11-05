package com.musicalgorithm;

/**
 * Created by yvinogradov on 11/4/2015.
 */
public class MusicAlgorithm {

    public static float getAverageAmplitude(short[] inputStream) {
        float[] mag = new float[inputStream.length / 2];
        float average = 0f;
        for (int i = 0; i < inputStream.length - 1; i += 2) {
            mag[i] = (float) (Math.abs(inputStream[i]) + Math.abs(inputStream[i + 1])) / 2;
            average += mag[i];
        }
        average /= mag.length;
        
        return average;
    }
    
    public static float setOpacity(short[] inputStream, float opacity, float average) {
        /** Opacity should be a single number, not an array
         * Opacity gets set to 0.5.
         *  If the magnitude is greater than average, add some amount of opacity (the magnitude value?).
         *  If the magnitude is less than average, remove some amount of opacity (the magnitude value?).
         * If opacity goes above 1, set to 1.
         * If opacity goes below 0, set to 0.1 (to avoid blackout). */
         float mag = (Math.abs(inputStream[0]) + Math.abs(inputStream[1])) / 2;
         mag -= average;
         opacity += mag;
         if(opacity <= 0) {
             opacity = 0.1;
         }
         else if(opacity > 1) {
             opacity = 1;
         }
         return opacity;    
    }


}
