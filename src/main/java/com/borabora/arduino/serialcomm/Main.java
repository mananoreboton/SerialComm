package com.borabora.arduino.serialcomm;

import com.borabora.arduino.serialcomm.piezo.MusicGenerator;

/**
 * Created by mrbueno on 07/10/15.
 */
public class Main {
    public static void main(String[] args) {
        int command = 1;
        int command2 = 2;
        int command3 = 3;
        SerialConversation conversation = new SerialConversation(true);
        SerialActions actions = new SerialActions();
        actions.put(command, x -> {
            System.out.println("x = " + new String(x));
            return new String(x);
        });
        SerialListener listener = new SerialListener(actions);
        conversation.start(listener);
        conversation.talkAndOver(new SerialMessage(command, "casa tomada".getBytes()));
        conversation.talkAndOver(MusicGenerator.generateMusic(13, 51, "ggagCbggagDCggGECbaffecdc ggagCbggagDCggGECbaffecdc", "112224112224112222211222441122241122241122222112224"));
        conversation.talkAndOver(new SerialMessage(command3, "tercera".getBytes()));
    }
}
