package arduino;

import com.fazecast.jSerialComm.*;

public class Serial_Port {
	
	public static void getPorts() {
		System.out.println("Avaiable Comm ports:");
		
		SerialPort[] ports = SerialPort.getCommPorts();
		
		for (var port : ports) {
			System.out.println(port.toString());
		}
	}
	
	public static void Open() {
		
		if (SerialPort.getCommPorts().length > 0) {
			serial_port = SerialPort.getCommPorts()[0];
			serial_port.openPort();
		} else {
			System.out.println("No Comm ports found!");
		}
			
	}
	
	public static void Close() {
		serial_port.closePort();
	}
	
	public static void Write(String data) {
		serial_port.writeBytes(data.getBytes(), data.getBytes().length);
	}
	
	public static SerialPort serial_port = null;
}
