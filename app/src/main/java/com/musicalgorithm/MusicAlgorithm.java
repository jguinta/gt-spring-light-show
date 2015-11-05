package com.musicalgorithm;

/**
 * Created by yvinogradov on 11/4/2015.
 */
public class MusicAlgorithm {

    public static float[] getAmplitude(short[] inputStream) {
        float[] mag = new float[inputStream.length / 2];
        float average = 0f;
        for (int i = 0; i < inputStream.length - 1; i += 2) {
            mag[i] = (float) (Math.abs(inputStream[i]) + Math.abs(inputStream[i + 1])) / 2;
            average += mag[i];
        }
        average /= mag.length;
        float[] opac = new float[mag.length];
        for(int i = 0; i < mag.length; i++){
            opac[i]=0.5f;
        }
        for (int i = 0; i < mag.length; i++) {
            mag[i] = mag[i] - average;
            if(!((mag[i]+0.5f  > 1) || (mag[i]+0.5f  < 0))){
                opac[i]=mag[i];
            }
        }
        return opac;
    }


}
