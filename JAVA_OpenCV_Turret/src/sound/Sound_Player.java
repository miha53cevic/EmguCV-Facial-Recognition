package sound;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sound_Player {
	
	public static void Deploy() {
		String sound = "TurretSounds/deploy.wav";
		
		playSound(sound);
	}
	
	public static void Shutdown() {
		int index = (int)(Math.floor(Math.random() * 3) + 1);
		String sound = "TurretSounds/shutdown_" + index + ".wav";
		
		playSound(sound);
	}
	
	private static void playSound(String path) {
		try {
			AudioInputStream inputStream = AudioSystem.getAudioInputStream(new File(path).toURI().toURL());
			Clip clip = AudioSystem.getClip();
			clip.open(inputStream);
			clip.start();
		} catch(Exception e) {
			e.getLocalizedMessage();
		}
	}
}
