import sys
import time
import random
from threading import Thread
import serial # pyserial
import cv2 # opencv-python
import playsound # playsound
import simplegui # PySimpleGUI

class StopWatch:
    def __init__(self):
        self.m_start = time.time();
        self.m_total = 0;

    def check(self):
        self.m_end = time.time();
        self.m_total += self.m_end - self.m_start;
        self.m_start = self.m_end;
        return self.m_total;
    
    def reset(self):
        self.m_start = time.time();
        self.m_total = 0;

# Play glados turret sounds
def turretSound(text: str):
    sounds = {
        "searching1": "./turret_sounds/searching1.wav",
        "searching2": "./turret_sounds/searching2.wav",

        "deploy": "./turret_sounds/deploy.wav"
    };

    def play(text: str):
        playsound.playsound(sounds[text]);

    if (sounds.get(text, False)): # Second arg is value to return if key doesn't exist
        # Withouth a thread the program is blocked
        T = Thread(target=play, args=(text, ));
        T.start();
    else:
        print(f"The sound {text} does not exist!\n");

# Map function for mapping from one range to another
def map(s :float, a1 :float, a2 :float, b1 :float, b2 :float) -> float: # return type float
    return b1 + ((s - a1) * (b2 - b1)) / (a2 - a1);

def main():
    gui_inputs = simplegui.createWindow();

    # Exit if gui window is closed by user
    if (gui_inputs is None):
        print('Exit successful!');
        sys.exit();

    # Get the capture index (check if it can be parsed from the string)
    capture_index = gui_inputs.get('capture_index');
    try:
        capture_index = int(capture_index);
    except ValueError:
        print("The capture_index must be an int");
        sys.exit();

    # Get the COM port
    com_port = gui_inputs.get('COM');

    # serial port
    try:
        serial_port = serial.Serial(com_port); # "/dev/ttyUSB0" for linux
        time.sleep(1); # must add sleep otherwise it won't send because the arduino is in reset mode on port open and can't read in values
        serial_port.write(b'{0},{0}');
    except serial.SerialException:
        print(f"Serial port {com_port} could not be accessed!");
        sys.exit();

    # Deploy sound
    turretSound("deploy");

    # Get the camera and initialize cascade (face-detection)
    capture = cv2.VideoCapture(capture_index);
    cascade = cv2.CascadeClassifier("faceAlt2.xml");

    print('Exit with q');

    stopwatch = StopWatch();
    while(True):
        # Get if camera exists - ret and get the frame from the capture device
        ret, frame = capture.read();

        # No camera check
        if (not(ret)):
            print("No camera detected!");
            break;
        
        # Grayscale image
        gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY);

        img = frame;

        faces = cascade.detectMultiScale(gray, 1.1, 3);
        for (x,y,w,h) in faces:
            img = cv2.rectangle(frame, (x, y), (x+w, y+h), (255,0,0), 2);
            sx = int(x) + int(w/2);
            sy = int(y) + int(h/2);
            sx = int(map(sx, 0, int(img.shape[1]), -90, 90 ));
            sy = int(map(sy, 0, int(img.shape[0]), -45, 90 ));
            print(sx, sy);
            serial_port.write('{},{}'.format(sx,sy).encode());

        # Show frame in window called 'Face detection'
        cv2.imshow('Face detection', img);
        if (cv2.waitKey(1) & 0xFF == ord('q')):
            break;

        # Play searching turret sound
        if (len(faces) == 0):
            # Wait 30 seconds before asking if anyone is there
            if (stopwatch.check() >= 30):
                turretSound(f"searching{random.randint(1, 2)}");
                stopwatch.reset();
        else:
            stopwatch.reset();

    # Release from memory
    capture.release();
    cv2.destroyAllWindows();
    serial_port.close();

if (__name__ == '__main__'):
    main();