package tw.com.owllistener.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.com.owllistener.network.configuration.ActualConfiguration;
import tw.com.owllistener.network.configuration.ListenerConfiguration;
import tw.com.owllistener.network.initialState.BufferingRecorder;
import tw.com.owllistener.network.initialState.InitialStateRecorder;
import tw.com.owllistener.network.initialState.SendDataToInitialState;

public class Run {

	public static void main(String[] args) {
		final Logger logger = LoggerFactory.getLogger(Run.class);
		logger.info("Starting");

		ListenerConfiguration configuration = new ActualConfiguration();

		ReceiveMessages receiver = new ReceiveMessages(configuration,  new CurrentTime());
        SendDataToInitialState sender = new SendDataToInitialState(configuration);
        RecordsReadings recorder = new InitialStateRecorder(sender);
        RecordsReadings bufferedRecorder = new BufferingRecorder(recorder);

        new EnergyMonitor(receiver, bufferedRecorder).loop();
	}



}
