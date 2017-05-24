package tw.com.owllistener.network;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;
import tw.com.owllistener.network.configuration.ListenerConfiguration;

public class ReceiveMessages {
	
	private ReceiveMulticastMessages multicastReceiver;
	private ParseMessages parser;

	public ReceiveMessages(ListenerConfiguration configuration) {
		multicastReceiver = new ReceiveMulticastMessages(configuration);
		parser = new ParseMessages();
	}

	public void init() throws IOException {
		multicastReceiver.init();	
	}

	public EnergyMessage receiveNextMessage() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
		String message = multicastReceiver.getMessage();
		return parser.parse(message);
	}

}
