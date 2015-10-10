package com.borabora.arduino.serialcomm;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by mrbueno on 07/10/15.
 */
public class SerialListener implements SerialPortEventListener {
    private Logger logger = LogManager.getLogger(SerialConversation.class);
    private SerialConversation conversation;
    private SerialPort serialPort;
    private List<Character> buffer = new ArrayList<Character>(16);


    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR()) {//If data is available
            if (event.getEventValue() > 0) {//Check bytes count in the input buffer
                try {
                        byte[] bytes = serialPort.readBytes();
                        logger.info("Receiving " + Arrays.toString(bytes));
                        for (int i = 0; i < bytes.length; i++) {
                            byte b = bytes[i];
                            if(b != '\n') {
                                buffer.add((char) b);
                            } else {
                                String msg = createMsg(buffer);
                                logger.info("Procesing " + msg);
                                buffer = new ArrayList<Character>(16);
                                this.conversation.getLock().take();
                            }
                        }
                } catch (SerialPortException ex) {
                    logger.error(ex.getMessage());
                } catch (InterruptedException e) {
                    logger.error(e.getMessage());
                }
            }
            try {
                serialPort.purgePort(SerialPort.PURGE_RXCLEAR);
            } catch (SerialPortException ex) {
                logger.error(ex.getMessage());
            }


        } else if (event.isCTS()) {//If CTS line has changed state
            if (event.getEventValue() == 1) {//If line is ON
                logger.error("CTS - ON");
            } else {
                logger.error("CTS - OFF");
            }
        } else if (event.isDSR()) {///If DSR line has changed state
            if (event.getEventValue() == 1) {//If line is ON
                logger.error("DSR - ON");
            } else {
                logger.error("DSR - OFF");
            }
        }
    }

    private String createMsg(List<Character> chars) {
        StringBuilder sb = new StringBuilder(chars.size());
        for (Character c : chars)
            sb.append(c.charValue());

        return sb.toString();
    }

    public void setConversation(SerialConversation conversation) {
        this.conversation = conversation;
        this.serialPort = this.conversation.getSerialPort();
    }

    public SerialConversation getConversation() {
        return conversation;
    }
}
