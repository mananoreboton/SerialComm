package com.borabora.arduino.serialcomm;

import com.borabora.arduino.serialcomm.piezo.MusicGenerator;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

/**
 * Created by mrbueno on 07/10/15.
 */
public class Main {
    public static void main(String[] args) {
        int command = 2;
        SerialConversation conversation = new SerialConversation(true);
        SerialActions actions = new SerialActions();
        actions.put(command, x -> {
            System.out.println("x = " + new String(x) + "-");
            return new String(x);
        });
        SerialListener listener = new SerialListener(actions);
        conversation.start(listener);

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String notes = "";
        while (!"ya".equals(notes)) {
            try {
                System.out.println("Notas:");
                notes = bufferedReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //conversation.talkAndOver(new SerialMessage(command, "casa tomada".getBytes()));
            try {
                //conversation.talkAndOver(MusicGenerator.generateMusic(5, "ggagCbggagDCggGECbaffecdc ggagCbggagDCggGECbaffecdc", "112224112224112222211222441122241122241122222112224"));
                //String notes = "ggagCbggagDCggGECbaffecdc ggagCbggagDCggGECbaffecdc";
                String beats = "44444444444444444444444444444444";
                //String beats = "112224112224112222211222441122";
                conversation.talkAndOver(MusicGenerator.generateMusic(5, notes, beats));
            } catch (SerialInvalidValueException e) {
                e.printStackTrace();
            }
        }
        //conversation.talkAndOver(new SerialMessage(command, "tercera".getBytes()));
    }
}
