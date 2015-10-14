package com.borabora.arduino.serialcomm.piezo;

import com.borabora.arduino.serialcomm.SerialInvalidValueException;
import com.borabora.arduino.serialcomm.SerialMessage;

/**
 * Created by mrbueno on 11/10/15.
 */
public class MusicGenerator {
    public static SerialMessage generateMusic(int buzzerPin, String notes, String beats) throws SerialInvalidValueException {

        int songLength = beats.length() < notes.length() ? beats.length() : notes.length();

        byte[] msg = new byte[songLength * 2 + 2];
        if(buzzerPin == 10) {
            throw new SerialInvalidValueException();
        }
        msg[0] = (byte) buzzerPin;
        msg[1] = (byte) songLength;

        for (int i = 0; i < songLength; i++) {
            char note = notes.charAt(i);
            msg[i + 2] = (byte) note;
        }

        //int length = notes.length();
        for (int i = 0; i < songLength; i++) {
            String beat = beats.substring(i, i + 1);
            int parseInt = Integer.parseInt(beat);
            if(parseInt == 10) {
                throw new SerialInvalidValueException();
            }
            msg[songLength + i + 2] = (byte) parseInt;
        }

        SerialMessage serialMessage = new SerialMessage(1, msg);
        return serialMessage;
    }
}
