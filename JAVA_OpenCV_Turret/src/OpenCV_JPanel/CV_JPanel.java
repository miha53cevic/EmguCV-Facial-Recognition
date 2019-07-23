package OpenCV_JPanel;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class CV_JPanel extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.drawImage(image, 0, 0, this);
	 }
	
	public void Refresh(BufferedImage img) {
		image = img;
		repaint();
	}
}
