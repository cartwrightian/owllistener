package tw.com.owllistener.network;

import java.io.IOException;
import java.io.StringReader;
import java.time.Instant;
import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import tw.com.owllistener.ProvidesDate;

public class ParseMessages {
	private static final Logger logger = LoggerFactory.getLogger(ParseMessages.class);
	private DocumentBuilderFactory dbf;
	private XPathFactory xpathFactory;
    private ProvidesDate providesDate;

    public ParseMessages(ProvidesDate providesDate) {
        this.providesDate = providesDate;
        dbf = DocumentBuilderFactory.newInstance();
		xpathFactory = XPathFactory.newInstance();
	}

	public Optional<EnergyMessage> parse(String xml) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException {
		InputSource source = new InputSource(new StringReader(xml));

		DocumentBuilder db = dbf.newDocumentBuilder();
		Document document = db.parse(source);
		
		XPath xpath = xpathFactory.newXPath();
		
		String nodeName = document.getDocumentElement().getNodeName();
		logger.info("Received message of type: " + nodeName);
		
		if (!"electricity".equals(nodeName)) {
			logger.info("Ignoring none electricity message of type " + nodeName);
			return Optional.empty();
		}
		
		String id = xpath.evaluate("/electricity/@id", document);
		logger.debug("id is: " +id);
		Instant instance = providesDate.getInstant();

		EnergyMessage message = new EnergyMessage(id, instance.getEpochSecond());
		
		String battery = xpath.evaluate("/electricity/battery/@level", document);
		logger.debug("battery level is: " +battery);
		battery = battery.replace("%", "");
		Integer batteryLevel = Integer.parseInt(battery);
		message.setBatteryLevel(batteryLevel);
		
		NodeList list = (NodeList) xpath.evaluate("/electricity/chan", document, XPathConstants.NODESET);
		
		logger.info("Received channel count " + list.getLength());
		for(int i = 0; i < list.getLength(); i++) {
			Node item = list.item(i);
			EnergyMessageChannel channel = parseChannel(item);
			message.addChannel(channel);
		}
		
		return Optional.of(message);
	}

	private EnergyMessageChannel parseChannel(Node item) throws XPathExpressionException {
		XPath xpath = xpathFactory.newXPath();
		String current = xpath.evaluate("curr", item);
		logger.info("Current reading for channel " + current);
		Double currentReading = Double.parseDouble(current);
		
		String day = xpath.evaluate("day", item);
		logger.info("Day (total) reading for channel is " + day);
		Double dayReading = Double.parseDouble(day);
			
		EnergyMessageChannel channel = new EnergyMessageChannel(currentReading, dayReading);
		
		return channel;
	}

}
