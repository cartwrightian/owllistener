package tw.com.owllistener.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Run {

	public static void main(String[] args) {
		final Logger logger = LoggerFactory.getLogger(Run.class);
		logger.info("Starting");

        ReceiveMessages receiver = new ReceiveMessages();
        RecordsReadings recorder = new SavesReadingsToCSV(args[0], new CurrentTime());

        new EnergyMonitor(receiver, recorder).loop();
	}



}
