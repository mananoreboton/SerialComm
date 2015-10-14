String inputString = "";
boolean stringComplete = false;
int l = 50;

void setup() {
  pinMode(8, OUTPUT);
  pinMode(9, OUTPUT);
  pinMode(10, OUTPUT);
  pinMode(12, OUTPUT);


  Serial.begin(9600);
  inputString.reserve(400);

  // Music play
  pinMode(5, OUTPUT);

}

void loop() {
  serialEvent();

  if (stringComplete) {
    luz(l, 10);
    selectCommand(inputString);
    Serial.print(inputString);
    inputString = "";
    stringComplete = false;
  }
}

void selectCommand(String inputString) {
  if(inputString.length() > 0) {
    if((byte) inputString.charAt(0) == 1) {
      parseCommand_playMusic(inputString);
    }
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


/******************* Music Play *****************/

void parseCommand_playMusic(String msg) {
  const int buzzerPin = msg.charAt(1);
  const int songLength = msg.charAt(2);

  //char notes[] = "ggagCbggagDCggGECbaffecdc ggagCbggagDCggGECbaffecdc"; // a space represents a rest
  //int beats[] = {1,1,2,2,2,4,1,1,2,2,2,4,1,1,2,2,2,2,2,1,1,2,2,2,4,4,1,1,2,2,2,4,1,1,2,2,2,4,1,1,2,2,2,2,2,1,1,2,2,2,4};
  int beats[songLength];
  int maxi = songLength + 3;
  int i = 0;
  int j = 0;

  char notes[songLength];
  for (i = 3; i < maxi; i++) {
      notes[j] = msg.charAt(i);
      j++;
  }
  j = 0;

  maxi = songLength * 2 + 3;
  for (i = songLength + 3; i < maxi; i++) {
      beats[j] = msg.charAt(i);
      j++;
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