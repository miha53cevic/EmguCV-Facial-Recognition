package arduino;

import org.opencv.core.Rect;

import display_manager.display_manager;

public class Turret {

	public static void sendTo(Rect[] faces, float elapsedTime) {
		
		// Delay for 500ms and send to turret
		if (time >= 500) {
			
			time = 0;
			
			// Translate coordinate system so that 0,0 is in the middle
			// of the screen and translate them to 0, 180
			float x, y;
			x = (faces[0].x + faces[0].width  / 2) - (display_manager.SCRN_WIDTH  / 2);
			y = (faces[0].y + faces[0].height / 2) - (display_manager.SCRN_HEIGHT / 2);
			
			x = Util.map(x, -display_manager.SCRN_WIDTH   / 2, display_manager.SCRN_WIDTH   / 2, -90, 90);
			y = Util.map(y, -display_manager.SCRN_HEIGHT  / 2, display_manager.SCRN_HEIGHT  / 2, -90, 90);
			
			String COORD = String.format("%d,%d", (int)x, (int)y);
			//System.out.println(COORD);
			
			if (Serial_Port.serial_port != null && Serial_Port.serial_port.isOpen()) {
				Serial_Port.Write(COORD);
			}
			
		} else {
			time += elapsedTime;
		}
	}
	
	public static float time;
}
