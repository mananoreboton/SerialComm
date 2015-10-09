package com.borabora.arduino.serialcomm;

/**
 * Created by mrbueno on 07/10/15.
 */

import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.UnsupportedEncodingException;

public class SerialConversation {

    private static final String MESG_NULL = "Message is null";
    private static final int MIN_PORT = 0;
    private Logger logger = LogManager.getLogger(SerialConversation.class);
    private SerialPort serialPort;

    public boolean talkAndOver(String msg) {
        String s = "UTF-8";
        boolean done = false;
        try {
            if (msg != null) {
                try {
                    Thread.sleep(3000l);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                done = serialPort.writeString(msg + "\n", s);
                logger.info("Sending " + msg);

            } else {
                logger.error(MESG_NULL);
            }
        } catch (SerialPortException e) {
            logger.error(e.getMessage());
        } catch (UnsupportedEncodingException e) {
            logger.error(e.getMessage());
        }
        return done;
    }

    public void openPort(int number) {
        if (number < 5) {
            serialPort = new SerialPort("/dev/ttyACM" + number);

            if (serialPort != null) {
                try {
                    serialPort.openPort();
                    serialPort.setParams(9600, 8, 1, 0);
                    int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;
                    serialPort.setEventsMask(mask);
                } catch (SerialPortException ex) {
                    logger.error(ex.getMessage());
                    openPort(number + 1);
                }
            }
        }
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public void start(SerialListener listener) {
        openPort(MIN_PORT);
        if (listener != null) {
            try {
                listener.setConversation(this);
                serialPort.addEventListener(listener);
            } catch (SerialPortException e) {
                e.printStackTrace();
            }
        }
    }
}
