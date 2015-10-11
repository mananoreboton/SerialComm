String inputString = "";
boolean stringComplete = false;
int l = 100;

void setup() {
  pinMode(8, OUTPUT);
  pinMode(9, OUTPUT);
  pinMode(10, OUTPUT);
  pinMode(12, OUTPUT);
  pinMode(13, OUTPUT);

  Serial.begin(9600);
  inputString.reserve(200);
}

void loop() {
  serialEvent();

  if (stringComplete) {
    luz(l, 10);
    Serial.print(inputString);
    inputString = "";
    stringComplete = false;
  }
}

void serialEvent() {
  while (Serial.available()) {
    luz(l, 8);

    char inChar = (char)Serial.read();
    inputString += inChar;

    if (inChar == '\n') {
      luz(l, 9);
      stringComplete = true;
    }
  }
}

void luz(int d, int p) {
  d = d/2;
  digitalWrite(p, HIGH);
  delay(d);
  digitalWrite(p, LOW);
  delay(d);
}