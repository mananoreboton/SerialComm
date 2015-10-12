package com.borabora.arduino.serialcomm;

public class SerialMessage {
    private int command;
    private byte[] msg;

    public SerialMessage() {
    }

    public SerialMessage(int command, byte[] msg) {
        this.command = command;
        this.msg = msg;
    }

    public int getCommand() {
        return command;
    }

    public byte[] getMsg() {
        return msg;
    }

    public void setMsg(byte[] msg) {
        this.msg = msg;
    }

    public void setCommand(int command) {
        this.command = command;
    }
}
