#include <Servo.h>

Servo servo_X;
Servo servo_Y;

int angle = 90;

void setup() {
  servo_X.attach(5);
  servo_Y.attach(6);
  Serial.begin(9600);

  Serial.setTimeout(10);
}

void loop() {
  // put your main code here, to run repeatedly:

}

void serialEvent() {
  while (Serial.available())
  {
    String serialRead = Serial.readString();

    int x = angle + getX(serialRead);
    int y = angle + getY(serialRead);

    if (y > 90)
    {
      y = 90;
    }
    
    if (x != servo_X.read())
    {
      servo_X.write(180 - x);  
    }
    if (y != servo_Y.read())
    {
      servo_Y.write(y);
    }

    Serial.println(serialRead);
    Serial.print("X: ");
    Serial.println(x);
    Serial.print("Y: ");
    Serial.println(y);
    Serial.println();
  }
}

int getX(String input) {
  input.remove(input.indexOf(","));

  if (input.toInt() > 87)
  {
    return 87;
  }
  
  return input.toInt(); 
}

int getY(String input) {
  input.remove(0, input.indexOf(",") + 1);

  if (input.toInt() > 70)
  {
    return 70;
  }
  
  return input.toInt(); 
}
