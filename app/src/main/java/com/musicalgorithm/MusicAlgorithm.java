package com.musicalgorithm;


import java.util.Random;

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
        float[] colors = new float[4];
        float opacity = 10f;
        /*if (!(amplitude == 0f)) {
            opacity = (amplitude / 5000) * 128;
        }*/

        if (amplitude <= 5000) {
            colors[0] = 0;
            colors[1] = amplitude / 5000 * 128;
            colors[2] = 127 + amplitude / 5000 * 128;
            colors[3] = (amplitude / 5000) * 128;
        } else if (amplitude <= 10000) {
            colors[0] = amplitude / 10000 * 128;
            colors[1] = 127 + amplitude / 10000 * 128;
            colors[2] = 128 - amplitude / 10000 * 128;
            colors[3] = (amplitude / 10000) * 128;
        } else if (amplitude <= 15000) {
            colors[0] = 127 + amplitude / 15000 * 128;
            colors[1] = 255 - amplitude / 15000 * 128;
            colors[2] = 64 - amplitude / 15000 * 64;
            colors[3] = (amplitude / 15000) * 128;
        } else {
            colors[0] = 255;
            colors[1] = 255;
            colors[2] = 255;
            colors[3] = (127 + (amplitude / 16000) * 128) > 255 ? 255 : (127 + (amplitude / 16000) * 128);
        }
        colors[3] = colors[3]>128? colors[3]: amplitude / 16000 * 128;//too hacky bubble it up if it works better
        return colors;

    }

   /* public void setTempo(AudioRingBuffer buffer) {
        BeatDetect beat = new BeatDetect();
        beat.detect(buffer);
        boolean flag = beat.isKick();
        if (flag) {
            s1 = s2;
            s2 = setMarkerAtSample();
            tempo = 60 / ((s2 - s1) / 44100)
        }
    }

    public static int[] setColors(int brightness) {
        boolean[] lightFlags = {true, true, true, true};
        int index;
        int[][] colors = new int[4][4];
        Random rand = new Random();

        for(int i = 0; i < 4; i++) {
            colors[i][0] = brightness;
        }

        if(tempo < 60) { // dark greens, navy blues, dark violets
            index = rand.nextInt(4);
            colors[index][1] = 0; 				// GREEN
            colors[index][2] = 70 + rand.nextInt(70);
            colors[index][3] = 5 + rand.nextInt(10);
            lightFlags[index] = false;

            while(lightFlags[index] == false) {
                index = rand.nextInt(4);
            }
            colors[index][1] = 5 + rand.nextInt(10); 				// NAVY BLUES
            colors[index][2] = 0;
            colors[index][3] = 45 + rand.nextInt(100);
            lightFlags[index] = false;

            while(lightFlags[index] == false) {
                index = rand.nextInt(4);
            }
            colors[index][1] = 50 + rand.nextInt(100); 				// DARK VIOLETS
            colors[index][2] = 0;
            colors[index][3] = 50 + rand.nextInt(100);
            lightFlags[index] = false;

            while(lightFlags[index] == false) {
                index = rand.nextInt(4);
            }
            colors[index][1] = 25 + rand.nextInt(100); 				// DARK RED
            colors[index][2] = 0;
            colors[index][3] = 0;
        }

        if(tempo > 60 && tempo < 120) { // light blues, light greens, yellows
            index = rand.nextInt(4);
            colors[index][1] = 100 + rand.nextInt(100); 				// LIGHT BLUE
            colors[index][2] = 170 + rand.nextInt(80);
            colors[index][3] = 255;
            lightFlags[index] = false;

            while(lightFlags[index] == false) {
                index = rand.nextInt(4);
            }
            colors[index][1] = 70 + rand.nextInt(100); 				// LIGHT GREEN
            colors[index][2] = 255;
            colors[index][3] = 70 + rand.nextInt(100);
            lightFlags[index] = false;

            while(lightFlags[index] == false) {
                index = rand.nextInt(4);
            }
            colors[index][1] = 255; 				// YELLOW
            colors[index][2] = 255;
            colors[index][3] = 0 + rand.nextInt(100);
            lightFlags[index] = false;

            while(lightFlags[index] = false) {
                index = rand.nextInt(4);
            }
            colors[index][1] = 255; 				// PINK
            colors[index][2] = 0 + rand.nextInt(100);
            colors[index][3] = 188 + rand.nextInt(50);
        }

        if(tempo > 120) { // reds, oranges, yellows
            index = rand.nextInt(4);
            colors[index][1] = 255; 				// LIGHT RED
            colors[index][2] = 55 + rand.nextInt(100);
            colors[index][3] = 55 + rand.nextInt(100);
            lightFlags[index] = false;

            while(lightFlags[index] == false) {
                index = rand.nextInt(4);
            }
            colors[index][1] = 255; 				// LIGHT ORANGE
            colors[index][2] = 180 + rand.nextInt(40);
            colors[index][3] = 0 + rand.nextInt(125);
            lightFlags[index] = false;

            while(lightFlags[index] == false) {
                index = rand.nextInt(4);
            }
            colors[index][1] = 255; 				// LIGHT YELLOW
            colors[index][2] = 255;
            colors[index][3] = 50 + rand.nextInt(100);
            lightFlags[index] = false;

            while(lightFlags[index] = false) {
                index = rand.nextInt(4);
            }
            colors[index][1] = 255; 				// PINK
            colors[index][2] = 0 + rand.nextInt(100);
            colors[index][3] = 188 + rand.nextInt(50);
        }
        return colors;
    }

*/
}