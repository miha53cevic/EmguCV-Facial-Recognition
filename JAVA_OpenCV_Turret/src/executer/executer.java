package executer;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.*;

import OpenCV_JPanel.CV_JPanel;
import display_manager.*;

public class executer {

	public static void main(String[] args) {
		
		// load Native / Dynamic library opencv.so
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		// Create display
		display_manager.CreateDisplay(640, 480, "JAVA_OpenCV_Turret");
		
		// Create CV_JPannel used for drawing frames given from Camera object
		CV_JPanel p = new CV_JPanel();
		display_manager.FRAME.getContentPane().add(p);
		
		// Setup camera with the camera(index)
		Camera c = new Camera(0);
		
		CascadeClassifier classifier = new CascadeClassifier("cascade_fillters/faceAlt2.xml");
		MatOfRect faceDetections = new MatOfRect();
		
		while (true) {
			Mat m = c.getMat();
			
			classifier.detectMultiScale(m, faceDetections, 1.1, 3, 0, new org.opencv.core.Size(20, 20), new org.opencv.core.Size(1000, 1000));
			
			Rect[] facesArray = faceDetections.toArray();
			for (int i = 0; i < facesArray.length; i++) {
				Imgproc.rectangle(m, facesArray[i].tl(), facesArray[i].br(), new Scalar(100), 3);
			}
			
			p.Refresh(Camera.MatToBuffered(m));
		}
	}
}
