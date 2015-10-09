package com.borabora.arduino.serialcomm;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by mrbueno on 07/10/15.
 */
public class SerialListener implements SerialPortEventListener {
    private Logger logger = LogManager.getLogger(SerialConversation.class);
    private SerialConversation conversation;
    private SerialPort serialPort;
    private int byteCount = 9;

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.isRXCHAR()) {//If data is available
            if (event.getEventValue() > 0) {//Check bytes count in the input buffer
                try {
                    String valueAsString = serialPort.readString();
                    logger.info("Receiving " + valueAsString);
                } catch (SerialPortException ex) {
                    logger.error(ex.getMessage());
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

    public void setConversation(SerialConversation conversation) {
        this.conversation = conversation;
        this.serialPort = this.conversation.getSerialPort();
    }

    public SerialConversation getConversation() {
        return conversation;
    }
}
