package display_manager;

import javax.swing.JFrame;

import sound.Sound_Player;

public class display_manager {

	public static int SCRN_WIDTH;
	public static int SCRN_HEIGHT;
	public static JFrame FRAME;
	
	public static void CreateDisplay(int ScreenWidth, int ScreenHeight, String Title) {
		SCRN_WIDTH 	= ScreenWidth;
		SCRN_HEIGHT = ScreenHeight;
		
		FRAME = new JFrame(Title);
		FRAME.setSize(ScreenWidth, ScreenHeight);
		FRAME.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		FRAME.setVisible(true);
		
		RUNNING = true;
		
		// add eventListener for close event
		FRAME.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override 
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				RUNNING = false;
			}
		});
	}
	
	public static Boolean isRunning() {
		return RUNNING;
	}
	
	private static Boolean RUNNING;
}
