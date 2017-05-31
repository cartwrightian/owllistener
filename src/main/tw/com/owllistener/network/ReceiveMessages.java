package tw.com.owllistener.network;

import java.io.IOException;
import java.util.Optional;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import tw.com.owllistener.network.configuration.ListenerConfiguration;

public class ReceiveMessages {
    private static final Logger logger = LoggerFactory.getLogger(ReceiveMessages.class);
	
	private ReceiveMulticastMessages multicastReceiver;
	private ParseMessages parser;

	public ReceiveMessages(ListenerConfiguration configuration) {
		multicastReceiver = new ReceiveMulticastMessages(configuration);
		parser = new ParseMessages();
	}

	public void init() throws IOException {
		multicastReceiver.init();	
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
