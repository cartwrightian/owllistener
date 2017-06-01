package tw.com.owllistener.network;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import jersey.repackaged.com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import tw.com.owllistener.network.initialState.MarshalToJson;

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
            Optional<EnergyMessage> possible = receiver.receiveNextMessage();
			if (possible.isPresent()) {
				logger.debug("Record energy message");
                EnergyMessage message = possible.get();
                Queue<MarshalToJson> toSend = new LinkedList<>();
                toSend.add(message);
                if (recorder.record(toSend)) {
					logger.info(String.format("Send possible %s ok", message));
				} else {
					logger.error("Error recording possible, stopping");
					running = false;
				}
			}
		}
		logger.warn("Stopped");
	}


}
