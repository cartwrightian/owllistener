package tw.com.owllistener.integration;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import tw.com.owllistener.network.CurrentTime;
import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.EnergyMessageChannel;
import tw.com.owllistener.network.configuration.ActualConfiguration;
import tw.com.owllistener.network.initialState.InitialStateRecorder;
import tw.com.owllistener.network.initialState.SendDataToInitialState;

import static junit.framework.TestCase.assertTrue;

public class TestInitialStateRecorder {

    @Test
    @Category(IntegrationTest.class)
    public void shouldSendTestDataToInitialState() {
        SendDataToInitialState sender = new SendDataToInitialState(new ActualConfiguration());
        InitialStateRecorder recorder = new InitialStateRecorder(sender, new CurrentTime());
        recorder.init();
        EnergyMessage message = new EnergyMessage("msgId");
        message.addChannel(new EnergyMessageChannel(42.42, 1022.22));
        assertTrue(recorder.record(message));
    }
}

