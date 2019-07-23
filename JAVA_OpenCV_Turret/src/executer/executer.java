package executer;

import java.time.Duration;
import java.time.Instant;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;

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
		display_manager.FRAME.getContentPane().add(p);
		
		// Check for ports and then open one
		Serial_Port.getPorts();
		
		// Setup camera with the camera(index)
		Camera.Record(0);
		
		// Setup FaceDetection
		Detector detector = new Detector("cascade_fillters/faceAlt2.xml");
		
		while (true) {
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
	}
}
