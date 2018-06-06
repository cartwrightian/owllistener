package tw.com.owllistener.network;

import java.io.IOException;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import tw.com.owllistener.ProvidesDate;
import tw.com.owllistener.network.configuration.ListenerConfiguration;

public class ReceiveMessages {
    private static final Logger logger = LoggerFactory.getLogger(ReceiveMessages.class);
    private static final int TIMEOUT_SECS = 15;

    private ReceiveMulticastMessages multicastReceiver;
	private ParseMessages parser;
	private boolean running;

	public ReceiveMessages(ListenerConfiguration configuration, ProvidesDate providesDate) {
		multicastReceiver = new ReceiveMulticastMessages(configuration);
		parser = new ParseMessages(providesDate);
		running = false;
	}

	public void init() throws InterruptedException {
	    while (!running) {
	        logger.info("Try to init multicast reception");
            try {
                multicastReceiver.init();
                running = true;
            } catch (IOException exception) {
                logger.error("Failed to start multicast reception ",exception);
                Thread.sleep(TIMEOUT_SECS * 1000);
            }
        }
	}

	public Optional<EnergyMessage> receiveNextMessage() {
        try {
            String message = multicastReceiver.getMessage();
            return parser.parse(message);
        } catch (XPathExpressionException | SAXException | ParserConfigurationException | IOException exception) {
            logger.error("Caught exception during possible reception", exception);
            return Optional.empty();
        }
    }

}
