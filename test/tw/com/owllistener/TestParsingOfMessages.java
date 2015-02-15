package tw.com.owllistener;

import static org.junit.Assert.*;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.junit.Test;
import org.xml.sax.SAXException;

import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.EnergyMessageChannel;
import tw.com.owllistener.network.ParseMessages;

public class TestParsingOfMessages {
	private String example = "<electricity id='4437190010E7'>"+
					"<signal rssi='0' lqi='0'/>"+
					"<battery level='10%'/>"+
					"<chan id='0'><curr units='w'>354.00</curr><day units='wh'>3971.56</day></chan>"+
					"<chan id='1'><curr units='w'>0.00</curr><day units='wh'>0.00</day></chan>"+
					"<chan id='2'><curr units='w'>0.00</curr>"+"<day units='wh'>0.00</day></chan>"+
				"</electricity>";

	@Test
	public void shouldParseXmlMessage() throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		ParseMessages parser = new ParseMessages();
		
		EnergyMessage message = parser.parse(example);
		assertEquals("4437190010E7", message.getUnitMac());

		assertEquals(new Integer(10), message.batteryLevel());
		assertEquals(3, message.getNumChannels());
		EnergyMessageChannel channel = message.getChannel(0);
		assertEquals(new Double(354.00), channel.getCurrent());
		assertEquals(new Double(3971.56), channel.getDayTotal());
	}

}
