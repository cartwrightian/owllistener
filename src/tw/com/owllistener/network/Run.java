package tw.com.owllistener.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Run {

	public static void main(String[] args) {
		final Logger logger = LoggerFactory.getLogger(Run.class);
		logger.info("Starting");
		new EnergyMonitor().loop(args[0]);
	}

}
