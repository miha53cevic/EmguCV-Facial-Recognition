package camera;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Camera {
	
	public static VideoCapture CAPTURE;
	
	public static void Record(int index) {
		CAPTURE = new VideoCapture(index);
	}
	
	public static void Stop() {
		CAPTURE.release();
	}
	
	public static BufferedImage getFrame() {
		
		if (CAPTURE == null) {
			System.out.println("CAPTURE not started!");
			return null;
		} else {
			
			Mat m = new Mat();
			
			if (!CAPTURE.read(m)) {
				System.out.println("Can't read frame!");
				return null;
			}
			
			return MatToBuffered(m);
		}
	}
	
	public static Mat getMat() {
		
		if (CAPTURE == null) {
			System.out.println("CAPTURE not started!");
			return null;
		} else {
			
			Mat m = new Mat();
			
			if (!CAPTURE.read(m)) {
				System.out.println("Can't read frame!");
				return null;
			}
			
			return m;
		}	
	}
	
	public static BufferedImage MatToBuffered(Mat frame) {
		int type = 0;
		if (frame.channels() == 1) {
			type = BufferedImage.TYPE_BYTE_GRAY;
		} else if (frame.channels() == 3) {
			type = BufferedImage.TYPE_3BYTE_BGR;
		}
		BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
		WritableRaster raster = image.getRaster();
		DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
		byte[] data = dataBuffer.getData();
		frame.get(0, 0, data);
		
		return image;
	}
	
	public static BufferedImage Grayscale(BufferedImage img) {
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				Color c = new Color(img.getRGB(j, i));
				
				int red 	= (int)(c.getRed()	 * 0.299);
				int blue 	= (int)(c.getBlue()	 * 0.587);
				int green 	= (int)(c.getGreen() * 0.114);
				
				Color newColour = new Color(
					red + green + blue,
					red + green + blue,
					red + green + blue);
				
				img.setRGB(j, i, newColour.getRGB());
			}
		}
		
		return img;
	}
}
