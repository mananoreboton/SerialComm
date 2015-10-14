package com.borabora.arduino.serialcomm;

/**
 * Created by mrbueno on 07/10/15.
 */

import jssc.SerialPort;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;

public class SerialConversation {

    private static final String MESG_NULL = "Message is null";
    private static final int MIN_PORT = 0;
    private Logger logger = LogManager.getLogger(SerialConversation.class);
    private SerialPort serialPort;
    private ArrayBlockingQueue<byte[]> lock = new ArrayBlockingQueue<>(1);
    public boolean sync;

    public SerialConversation(boolean sync) {
        this.sync = sync;
    }

    public boolean talkAndOver(SerialMessage serialMessage) {
        String s = "UTF-8";
        boolean done = false;
        try {
            if (serialMessage.getMsg() != null) {
/*                    try {
                        Thread.sleep(300l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/
                serialMessage.setMsg(clean(serialMessage.getMsg()));
                if (sync) {
                    lock.put(serialMessage.getMsg());
                }

                byte[] bytes = serialMessage.getMsg();
                byte[] msgAsBytes = new byte[bytes.length + 2];
                msgAsBytes[0] = (byte) serialMessage.getCommand();
                for (int i = 0; i < bytes.length; i++) {
                    byte aByte = bytes[i];
                    msgAsBytes[i + 1] = aByte;
                }
                msgAsBytes[bytes.length + 1] = '\n';
                logger.info("Sending " + new String(serialMessage.getMsg()) + "-");
                done = serialPort.writeBytes(msgAsBytes);
                //done = serialPort.writeString(serialMessage.getCommand() + serialMessage.getMsg() + "\n", s);

            } else {
                logger.error(MESG_NULL);
            }
        } catch (SerialPortException e) {
            logger.error(e.getMessage());
//        } catch (UnsupportedEncodingException e) {
//            logger.error(e.getMessage());
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        }
        return done;
    }

    private byte[] clean(byte[] msg) {
        return msg;
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

    public ArrayBlockingQueue<byte[]> getLock() {
        return lock;
    }

    public void setLock(ArrayBlockingQueue<byte[]> lock) {
        this.lock = lock;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }
}
