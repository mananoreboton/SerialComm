package com.borabora.arduino.serialcomm;

/**
 * Created by mrbueno on 07/10/15.
 */
public class Main {
    public static void main(String[] args) {
        SerialConversation conversation = new SerialConversation();
        byte sec = 0;
        SerialListener listener = new SerialListener();
        conversation.start(listener);
        conversation.talkAndOver("casa\ntomada");
        conversation.talkAndOver("otra");
        conversation.talkAndOver("tercera");
    }
}
