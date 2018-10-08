package com.argetgames.roadtofive.sound;

import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class SoundAPI {

	public SoundAPI() {
		
	}
	
	public static void testPlaySound(String filePath, float value){
		SoundAPI api = new SoundAPI();
		try {
	         // Open an audio input stream.
	         URL url = api.getClass().getClassLoader().getResource("sound/" + filePath);
	         AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
	         // Get a sound clip resource.
	         Clip clip = AudioSystem.getClip();
	         clip.open(audioIn);
	         FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
	         float range = gainControl.getMaximum() - gainControl.getMinimum();
	         float gain = (range * value) + gainControl.getMinimum();
	         gainControl.setValue(gain);
	         // Open audio clip and load samples from the audio input stream.
	         clip.start();
	      } catch (UnsupportedAudioFileException e) {
	         e.printStackTrace();
	      } catch (IOException e) {
	         e.printStackTrace();
	      } catch (LineUnavailableException e) {
	         e.printStackTrace();
	      }
	}
	
	

}
