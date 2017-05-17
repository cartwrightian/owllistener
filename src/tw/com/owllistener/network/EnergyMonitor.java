package tw.com.owllistener.network;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public class EnergyMonitor  {
	private static final Logger logger = LoggerFactory.getLogger(EnergyMonitor.class);
	private ReceiveMessages receiver;
    private RecordsReadings recorder;

    public EnergyMonitor(ReceiveMessages receiver, RecordsReadings recorder) {
        this.receiver = receiver;
        this.recorder = recorder;
    }

    public void loop() {
		logger.info("Beginning processing");
		try {
			// TODO check init start, push exception handling down on level
		    recorder.init();
			receiver.init();
		} catch (IOException e) {
			logger.error("Unable to start", e);
			return;
		}

		boolean running = true;
		while (running) {
			EnergyMessage message;
			try {
				logger.debug("Waiting for message");
				message = receiver.receiveNextMessage();
				if (message!=null) {
					logger.debug("Save message");
					if (recorder.record(message)) {
					    logger.info(String.format("Send message %s ok",message));
                    } else {
					    logger.error("Error recording message, stopping");
					    running = false;
                    }
				} else {
					logger.info("Message ignored");
				}
			} catch (XPathExpressionException | SAXException
					| ParserConfigurationException | IOException e) {
				logger.error("Caught exception ",e);
				logger.error("Will stop");
				running = false;
			} 
		}
		logger.warn("Stopped");
	}


}
