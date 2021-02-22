import cv2

# Get the camera and initialize cascade (face-detection)
capture = cv2.VideoCapture(0);
cascade = cv2.CascadeClassifier("../faceAlt2.xml");

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
    #gray = cv2.resize(gray, (20, 20));

    faces = cascade.detectMultiScale(gray, 1.1, 3);
    for (x,y,w,h) in faces:
        img = cv2.rectangle(frame, (x, y), (x+w, y+h), (255,0,0), 2);

    # Show frame in window called 'Face detection'
    cv2.imshow('Face detection', img);
    if (cv2.waitKey(1) & 0xFF == ord('q')):
        break;

# Release from memory
capture.release();
cv2.destroyAllWindows();