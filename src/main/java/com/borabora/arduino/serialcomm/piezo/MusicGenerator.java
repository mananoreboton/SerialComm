package com.borabora.arduino.serialcomm.piezo;

import com.borabora.arduino.serialcomm.SerialMessage;

/**
 * Created by mrbueno on 11/10/15.
 */
public class MusicGenerator {
    public static SerialMessage generateMusic(int buzzerPin, int songLength, String beats, String notes) {
        byte[] msg = new byte[songLength * 2 + 2];
        msg[0] = (byte) buzzerPin;
        msg[1] = (byte) songLength;

        for (int i = 0; i < beats.length(); i++) {
            int beat = beats.charAt(i);
            msg[i + 2] = (byte) beat;
        }

        int length = notes.length();
        for (int i = 0; i < length; i++) {
            char note = notes.charAt(i);
            msg[length + i + 2] = (byte) note;
        }

        SerialMessage serialMessage = new SerialMessage(1, msg);
        return serialMessage;
    }
}
