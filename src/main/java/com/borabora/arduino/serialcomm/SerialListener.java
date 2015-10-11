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
import java.util.Map;
import java.util.function.Function;

/**
 * Created by mrbueno on 07/10/15.
 */
public class SerialListener implements SerialPortEventListener {
    private Logger logger = LogManager.getLogger(SerialConversation.class);
    private SerialConversation conversation;
    private SerialPort serialPort;
    private List<Character> buffer = new ArrayList<Character>(16);
    private SerialActions actions;

    public SerialListener(SerialActions actions) {

        if (actions == null) {
            actions = new SerialActions();
        }
        this.actions = actions;
    }


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
                                SerialMessage msg = createMsg(buffer);
                                processMsg(msg);
                                buffer = new ArrayList<Character>(16);
                                if (this.conversation.isSync()) {
                                    this.conversation.getLock().take();
                                }
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

    private String processMsg(SerialMessage s) {
        String apply = null;
        if (actions.containsKey(s.getCommand())) {
            Function<String, String> action = actions.get(s.getCommand());
            apply = action.apply(s.getMsg());
        } else {
            logger.info("No action for command " + s.getCommand() + " and msg " + s.getMsg());
        }
        return apply;
    }

    private SerialMessage createMsg(List<Character> chars) {
        SerialMessage serialMessage = new SerialMessage();
        StringBuilder sb = new StringBuilder(chars.size());
        for (Character c : chars)
            sb.append(c.charValue());

        String msg = sb.toString();
        serialMessage.setMsg(msg.substring(1));
        serialMessage.setCommand(Integer.valueOf(msg.substring(0,1)));
        return serialMessage;

    }

    public void setConversation(SerialConversation conversation) {
        this.conversation = conversation;
        this.serialPort = this.conversation.getSerialPort();
    }

    public SerialConversation getConversation() {
        return conversation;
    }
}
