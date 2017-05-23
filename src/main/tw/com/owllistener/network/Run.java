package tw.com.owllistener.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.com.owllistener.network.initialState.ActualConfiguration;
import tw.com.owllistener.network.initialState.InitialStateRecorder;
import tw.com.owllistener.network.initialState.SendDataToInitialState;

public class Run {

	public static void main(String[] args) {
		final Logger logger = LoggerFactory.getLogger(Run.class);
		logger.info("Starting");

		ListenerConfiguration configuration = new ActualConfiguration();

		ReceiveMessages receiver = new ReceiveMessages(configuration);
        SendDataToInitialState sender = new SendDataToInitialState(configuration);
        RecordsReadings recorder = new InitialStateRecorder(sender, new CurrentTime());

        new EnergyMonitor(receiver, recorder).loop();
	}



}