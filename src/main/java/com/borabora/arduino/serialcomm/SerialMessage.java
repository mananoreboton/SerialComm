package com.borabora.arduino.serialcomm;

public class SerialMessage {
    private int command;
    private String msg;

    public SerialMessage() {
    }

    public SerialMessage(int command, String msg) {
        this.command = command;
        this.msg = msg;
    }

    public int getCommand() {
        return command;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setCommand(int command) {
        this.command = command;
    }
}
