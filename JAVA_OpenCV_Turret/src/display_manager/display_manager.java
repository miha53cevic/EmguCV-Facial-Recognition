package display_manager;

import javax.swing.JFrame;

public class display_manager {

	public static int SCRN_WIDTH;
	public static int SCRN_HEIGHT;
	public static JFrame FRAME;
	
	public static void CreateDisplay(int ScreenWidth, int ScreenHeight, String Title) {
		SCRN_WIDTH 	= ScreenWidth;
		SCRN_HEIGHT = ScreenHeight;
		
		FRAME = new JFrame(Title);
		FRAME.setSize(ScreenWidth, ScreenHeight);
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setVisible(true);
	}
}
