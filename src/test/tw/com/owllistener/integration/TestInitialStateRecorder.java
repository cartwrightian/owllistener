package tw.com.owllistener.integration;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import tw.com.owllistener.network.CurrentTime;
import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.EnergyMessageChannel;
import tw.com.owllistener.network.configuration.ActualConfiguration;
import tw.com.owllistener.network.initialState.InitialStateRecorder;
import tw.com.owllistener.network.initialState.SendDataToInitialState;

import java.time.Instant;

import static junit.framework.TestCase.assertTrue;
import static tw.com.owllistener.QueueHelper.asQueue;

public class TestInitialStateRecorder {

    private InitialStateRecorder recorder;

    @Before
    public void beforeEachTestRuns() {
        SendDataToInitialState sender = new SendDataToInitialState(new ActualConfiguration());
        recorder = new InitialStateRecorder(sender);
        recorder.init();
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldSendTestDataToInitialState() {

        EnergyMessage message = new EnergyMessage("msgId", Instant.now().getEpochSecond() );
        message.addChannel(new EnergyMessageChannel(92.42, 922.22));

        assertTrue(recorder.record(asQueue(message)));
    }

    @Test
    @Category(IntegrationTest.class)
    public void shouldSendQueuedMessages() {
        EnergyMessage messageA = new EnergyMessage("msgIdA", Instant.now().getEpochSecond());
        messageA.addChannel(new EnergyMessageChannel(142.42, 11022.22));

        EnergyMessage messageB = new EnergyMessage("msgIdA", Instant.now().getEpochSecond());
        messageB.addChannel(new EnergyMessageChannel(145.42, 11222.33));

        assertTrue(recorder.record(asQueue(messageA,messageB)));
    }
}

