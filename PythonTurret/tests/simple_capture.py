import cv2

# Get the camera
capture = cv2.VideoCapture(0);

while(True):
    # Get if camera exists - ret and get the frame from the capture device
    ret, frame = capture.read();

    # No camera check
    if (not(ret)):
        print("No camera detected!");
        break;

    # Show frame in window called 'frame'
    cv2.imshow('frame', frame);
    if (cv2.waitKey(1) & 0xFF == ord('q')):
        break;

# Release from memory
capture.release();
cv2.destroyAllWindows();