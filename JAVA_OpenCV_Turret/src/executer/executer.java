package executer;

import java.awt.Dimension;
import java.time.Duration;
import java.time.Instant;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

import sound.Sound_Player;
import arduino.Serial_Port;
import arduino.Turret;
import FaceDetection.Detector;
import OpenCV_JPanel.CV_JPanel;
import camera.Camera;
import display_manager.*;

public class executer {

	public static void main(String[] args) {
		
		// load Native / Dynamic library opencv.so
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		// Create display
		display_manager.CreateDisplay(640, 480, "JAVA_OpenCV_Turret");
		
		// Create timer - start
		Instant start = Instant.now();
		
		// Create CV_JPannel used for drawing frames given from Camera object
		CV_JPanel p = new CV_JPanel();
		p.setPreferredSize(new Dimension(640, 480));	// Bitno staviti jer inace bude 0,0 size
		display_manager.FRAME.getContentPane().add(p);
		display_manager.FRAME.pack();	// Bez toga je random ako loada i zove JPanel
		
		// Check for ports and then open one
		Serial_Port.getPorts();
		Serial_Port.Open();
		
		// Setup camera with the camera(index)
		Camera.Record(0);
		
		// Setup FaceDetection
		Detector detector = new Detector("cascade_fillters/faceAlt2.xml");
		
		Sound_Player.Deploy();
		
		while (display_manager.isRunning()) {
			Mat m = Camera.getMat();
			
			// find faces
			MatOfRect faces = detector.Detect(m);
			
			// add faces to the frame and draw it
			m = detector.DetectToFrame(m, faces);
			
			// draw frame
			p.Refresh(Camera.MatToBuffered(m));
			
			// Timer - end used for calculating deltaTime / elapsedTime
			Instant end = Instant.now();
			
			// send face coordinates to turret
			if (!faces.empty()) {
				Turret.sendTo(faces.toArray(), Duration.between(start, end).toMillis());
			}
			
			// Reset timer
			start = end;
		}
		
		Sound_Player.Shutdown();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}
}
