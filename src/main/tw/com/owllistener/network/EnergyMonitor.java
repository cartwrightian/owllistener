package tw.com.owllistener.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.com.owllistener.network.initialState.MarshalToJson;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

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
		    recorder.init();
			receiver.init();
		} catch (InterruptedException e) {
			logger.error("Unable to start", e);
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
