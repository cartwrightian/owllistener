package tw.com.owllistener.network;

import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import tw.com.owllistener.ProvidesDate;

public class EnergyMonitor implements ProvidesDate {
	private static final Logger logger = LoggerFactory.getLogger(EnergyMonitor.class);
	private ReceiveMessages receiver;
	private RecordsReadings recorder;
	
	public void loop(String filename) {
		logger.info("Beginning processing, filename is " +filename);
		receiver = new ReceiveMessages();
		try {
			receiver.init();
		} catch (IOException e) {
			logger.error("Unable to start listening", e);
			return;
		}
		recorder = new SavesReadingsToCSV(filename, this);
		
		//
		boolean running = true;
		while (running) {
			EnergyMessage message;
			try {
				logger.info("Waiting for message");
				message = receiver.receiveNextMessage();
				if (message!=null) {
					logger.info("Save message");
					recorder.record(message);
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

	@Override
	public Date getDate() {
		return new Date();
	}

}
