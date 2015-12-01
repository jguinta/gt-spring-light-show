package com.musicalgorithm;



import java.util.Random;

/**
 * Created by yvinogradov on 11/4/2015.
 */
public class MusicAlgorithm {
    static int X = 0;
    static int Y = 0;
    static Random randomGenerator = new Random();


    public static float getAverage(short[] inputStream) {
        float[] mag = new float[inputStream.length / 2];
        float average = 0f;
        int j = 0;
        for (int i = 0; i < inputStream.length - 1; i += 2) {
            mag[j] = (float) (Math.abs(inputStream[i]) + Math.abs(inputStream[i + 1])) / 2;
            average += mag[j];
            j++;
        }
        average /= mag.length;
        return average;
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
            colors[0] = 0;
            colors[1] = amplitude /2000 *13;
            colors[2] = amplitude / 2000 * 94;
        } else if (amplitude <= 3000) {
            colors[0] = 0;
            colors[1] = 94 * amplitude / 3000;
            colors[2] = 2 * amplitude / 3000;
        } else if (amplitude <= 4000) {
            colors[0] = 255 * amplitude / 4000;
            colors[1] = 105 * amplitude / 4000;
            colors[2] = 252 * amplitude / 4000;
        } else if (amplitude <= 5000) {
            colors[0] = 112 * amplitude / 5000 ;
            colors[1] = 94 * amplitude / 5000;
            colors[2] = 255 *amplitude / 5000;
        } else if (amplitude <= 6000) {
            colors[0] = 105 * amplitude / 6000;
            colors[1] = 255 * amplitude / 6000;
            colors[2] = 110 * amplitude / 6000;
        } else if (amplitude <= 7000) {
            colors[0] = 185 * amplitude / 7000;
            colors[1] = 140 * amplitude/7000;
            colors[2] = 180 * amplitude/7000;
        } else if (amplitude <= 8000) {
            colors[0] = 150 * amplitude / 8000;
            colors[1] = 100 * amplitude / 8000;
            colors[2] =  100 * amplitude/8000;
        } else if (amplitude <= 9000) {
            colors[0] = 246 * amplitude/9000;
            colors[1] = 255 *amplitude/9000;
            colors[2] = 0;
        } else if (amplitude <= 10000) {
            colors[0] = 255 * amplitude/10000;
            colors[1] = 208 * amplitude/10000;
            colors[2] = 0;
        } else if (amplitude <= 11000) {
            colors[0] = 255 * amplitude/11000;
            colors[1] = 4 * amplitude/11000;
            colors[2] = 0;
        } else if (amplitude <= 12000) {
            colors[0] = 255 * amplitude/12000;
            colors[1] = 102 * amplitude/12000;
            colors[2] = 102 * amplitude/12000;
        } else if (amplitude <= 13000) {
            colors[0] = 255 * amplitude / 13000;
            colors[1] = 255 * amplitude / 13000;
            colors[2] =  105 * amplitude / 13000;
        } else if (amplitude <= 14000) {
            colors[0] = 255 * amplitude / 14000;
            colors[1] = 228 * amplitude / 14000;
            colors[2] = 140 * amplitude / 14000;
        } else if (amplitude <= 15000) {
            colors[0] = 255 * amplitude / 15000;
            colors[1] = 140 * amplitude / 15000;
            colors[2] = 140 * amplitude / 15000;
        } else {
            int r = randomGenerator.nextInt(4);
            if (r == 1) {
                colors[0] = 255 * amplitude/11000;
                colors[1] = 4 * amplitude/11000;
                colors[2] = 0;
            } else if (r == 2) {
                colors[0] = 255 * amplitude/12000;
                colors[1] = 102 * amplitude/12000;
                colors[2] = 102 * amplitude/12000;
            } else if (r == 3) {
                colors[0] = 255 * amplitude / 15000;
                colors[1] = 140 * amplitude / 15000;
                colors[2] = 140 * amplitude/15000;
            } else {
                colors[0] = 255;
                colors[1] = 255;
                colors[2] = 255;
            }

            colors[3] = (127 + (amplitude / 16000) * 128) > 255 ? 255 : (127 + (amplitude / 16000) * 128);
        }
        colors[3] = colors[3] > 128 ? colors[3] : amplitude / 16000 * 128;//too hacky bubble it up if it works better
        return colors;

    }

    public static int[] getAxis(float amplitude) {
        int[] axis = new int[2];

        if (amplitude > 15000) {

            int scale = (int) ((amplitude / 16000) * 25);

            int randomInt = randomGenerator.nextInt(4);
            if (randomInt == 0) {
                X += scale;
                Y += scale;
            } else if (randomInt == 1) {
                X += scale;
                Y -= scale;
            } else if (randomInt == 2) {
                X -= scale;
                Y += scale;
            } else {
                X -= scale;
                Y -= scale;
            }
            if (X < 0)
                X = 0;
            if (X > 60)
                X = 60;
            if (Y > 120)
                Y = 120;
            if (Y < 70)
                Y = 70;
        }
        axis[0] = X;
        axis[1] = Y;


        return axis;
    }

    public static void flush() {
        X = 0;
        Y = 0;
    }


}
