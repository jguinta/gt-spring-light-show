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
             opacity = 0.1f;
         }
         else if(opacity > 1) {
             opacity = 1f;
         }
         return opacity;    
    }

/** THIS IS SKELETON CODE/WILL NOT WORK */
/** 1. Get kick beat.
*   2. Set marker.
*   3. Get kick beat.
*   4. Set marker.
*   5. Subtract 2 markers for sample size.
*   6. Divide size by 44100 to get seconds per beat.
*   7. 60 / seconds per beat to get bpm. */

    public void setTempo(AudioBuffer buffer) {
    	detect(buffer);
    	flag = isKick();
    	if(flag) {
    		s1 = s2;
    		s2 = setMarkerAtSample();
    		tempo = 60 / ((s2 - s1) / 44100);
    	}
    }
    
    public static void setColors() {
    	bool lightFlags[4] = {true, true, true, true};
    	int index;
    	if(tempo < 60) { // dark greens, navy blues, dark violets, dark reds
    		index = rand(0, 3)
    		setR(0, index); 				// GREEN
    		setG(70 + rand(70), index);
    		setB(5 + rand(10), index);
    		lightFlags[index] = false;
    
    		while(lightFlags[index] = false) {
    			index = rand(0, 3);
    		}
    		setR(5 + rand(10), index); 				// NAVY BLUES
    		setG(0, index);
    		setB(45 + rand(100), index);
    		lightFlags[index] = false;
    
    		while(lightFlags[index] = false) {
    			index = rand(0, 3);
    		}
    		setR(50 + rand(100), index); 				// DARK VIOLETS
    		setG(0, index);
    		setB(50 + rand(100), index);
    		lightFlags[index] = false;
    
    		while(lightFlags[index] = false) {
    			index = rand(0, 3);
    		}
    		setR(25 + rand(100), index); 				// DARK RED
    		setG(0, index);
    		setB(0, index);
    	}
    
    	if(tempo > 60 && tempo < 120) { // light blues, light greens, yellows, pinks
    		index = rand(0, 3)
    		setR(100 + rand(100), index); 				// LIGHT BLUE
    		setG(170 + rand(80), index);
    		setB(255, index);
    		lightFlags[index] = false;
    
    		while(lightFlags[index] = false) {
    			index = rand(0, 3);
    		}
    		setR(70 + rand(100), index); 				// LIGHT GREEN
    		setG(255, index);
    		setB(70 + rand(100), index);
    		lightFlags[index] = false;
    
    		while(lightFlags[index] = false) {
    			index = rand(0, 3);
    		}
    		setR(255, index); 				// YELLOW
    		setG(255, index);
    		setB(0 + rand(100), index);
    		lightFlags[index] = false;
    
    		while(lightFlags[index] = false) {
    			index = rand(0, 3);
    		}
    		setR(255, index); 				// PINK
    		setG(0 + rand(100), index);
    		setB(188 + rand(50), index);
    	}
    
    	if(tempo > 120) { // reds, oranges, yellows, pinks
    		index = rand(0, 3)
    		setR(255, index); 				// LIGHT RED
    		setG(55 + rand(100), index);
    		setB(55 + rand(100), index);
    		lightFlags[index] = false;
    
    		while(lightFlags[index] = false) {
    			index = rand(0, 3);
    		}
    		setR(255, index); 				// LIGHT ORANGE
    		setG(180 + rand(40), index);
    		setB(0 + rand(125), index);
    		lightFlags[index] = false;
    
    		while(lightFlags[index] = false) {
    			index = rand(0, 3);
    		}
    		setR(255, index); 				// LIGHT YELLOW
    		setG(255, index);
    		setB(50 + rand(100), index);
    		lightFlags[index] = false;
    
    		while(lightFlags[index] = false) {
    			index = rand(0, 3);
    		}
    		setR(255, index); 				// PINK
    		setG(0 + rand(100), index);
    		setB(188 + rand(50), index);
    	}
    }
}
