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
    parseCommand_playMusic(inputString);
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


/************************************/
void parseCommand_playMusic(String msg) {
    int buzzerPin = msg.charAt(1);
    int songLength = msg.charAt(2);
    int beats[songLength];
    int maxi = songLength + 3;
    int i = 0;
    for (i = 3; i < maxi; i++) {
        beats[i] = msg.charAt(i);
    }
    char notes[songLength];
    maxi = songLength * 2 + 3;
    for (i = songLength + 3; i < maxi; i++) {
        notes[i] = msg.charAt(i);
    }
    playMusic(buzzerPin, songLength, beats, notes);
}

void playMusic(int buzzerPin, int songLength, int beats[], char notes[])
{
  int tempo = 100;
  int i, duration;
  for (i = 0; i < songLength; i++) // step through the song arrays
  {
    duration = beats[i] * tempo;  // length of note/rest in ms
    if (notes[i] == ' ')          // is this a rest?
    {
      delay(duration);            // then pause for a moment
    }
    else                          // otherwise, play the note
    {
      tone(buzzerPin, frequency(notes[i]), duration);
      delay(duration);            // wait for tone to finish
    }
    delay(tempo/10);              // brief pause between notes
  }
  delay(tempo);
}

int frequency(char note)
{
  int i;
  const int numNotes = 14;  // number of notes we're storing

  char names[] = { 'c', 'd', 'e', 'f', 'g', 'a', 'b', 'C', 'D', 'E', 'G' };
  int frequencies[] = {262, 294, 330, 349, 392, 440, 494, 523, 587, 659, 784};

  for (i = 0; i < numNotes; i++)  // Step through the notes
  {
    if (names[i] == note)         // Is this the one?
    {
      return(frequencies[i]);     // Yes! Return the frequency
    }
  }
  return(0);  // We looked through everything and didn't find it,
}