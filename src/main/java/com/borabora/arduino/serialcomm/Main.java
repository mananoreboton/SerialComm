package com.borabora.arduino.serialcomm;

/**
 * Created by mrbueno on 07/10/15.
 */
public class Main {
    public static void main(String[] args) {
        int command = 1;
        int command2 = 2;
        int command3 = 3;
        SerialConversation conversation = new SerialConversation(false);
        SerialActions actions = new SerialActions();
        actions.put(command, x -> {
            System.out.println("x = " + x + x);
            return x;
        });
        SerialListener listener = new SerialListener(actions);
        conversation.start(listener);
        conversation.talkAndOver(new SerialMessage(command, "casa tomada"));
        conversation.talkAndOver(new SerialMessage(command2, "otra"));
        conversation.talkAndOver(new SerialMessage(command3, "tercera"));
    }
}
