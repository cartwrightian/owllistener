package tw.com.owllistener.integration;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.xml.sax.SAXException;

import tw.com.owllistener.TestConfiguration;
import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.ReceiveMessages;

public class TestReceiveMessages {

	@Test
	@Category(IntegrationTest.class)
	public void shouldReceiveAndParseMessage() throws IOException, XPathExpressionException, SAXException, ParserConfigurationException {
		ReceiveMessages receiver = new ReceiveMessages(new TestConfiguration("url","bucketKey", "accessKey"));
		
		receiver.init();
		EnergyMessage message = receiver.receiveNextMessage();
		assertEquals(3, message.getNumChannels());
		assertNotEquals(new Double(0), message.getChannel(0).getCurrent());
		assertNotEquals(new Double(0), message.getChannel(0).getDayTotal());
	}

}
