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
	
	public static void Open(String PortName) {
		serial_port = SerialPort.getCommPort(PortName);
		serial_port.openPort();
	}
	
	public static void Close() {
		serial_port.closePort();
	}
	
	public static void Write(String data) {
		serial_port.writeBytes(data.getBytes(), data.getBytes().length);
	}
	
	public static SerialPort serial_port = null;
}
