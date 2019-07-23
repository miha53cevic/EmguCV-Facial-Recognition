package FaceDetection;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.*;

public class Detector {
	
	public Detector(String XML_Path) {
		classifier = new CascadeClassifier(XML_Path);
	}
	
	public MatOfRect Detect(Mat m) {
		
		// gray scale image before detecting faces
		Mat gray_scaled = new Mat();
		Imgproc.cvtColor(m, gray_scaled, Imgproc.COLOR_BGR2GRAY);
		
		// find faces and save them as rectangles
		MatOfRect faceDetections = new MatOfRect();
		classifier.detectMultiScale(gray_scaled, faceDetections, 1.1, 3, 0, new org.opencv.core.Size(20, 20), new org.opencv.core.Size(1000, 1000));
		
		return faceDetections;
	}
	
	public Mat DetectToFrame(Mat frame, MatOfRect faces) {
		
		// draw the rectangles on the original full colour frame
		Rect[] facesArray = faces.toArray();
		for (int i = 0; i < facesArray.length; i++) {
			
			// draw main / first face in red
			if (i == 0) {
				Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 0, 255), 3);
			} else {
				Imgproc.rectangle(frame, facesArray[i].tl(), facesArray[i].br(), new Scalar(0, 0, 0), 3);
			}
		}
		
		return frame;
	}
	
	private CascadeClassifier classifier;
}
