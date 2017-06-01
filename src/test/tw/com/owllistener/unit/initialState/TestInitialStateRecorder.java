package tw.com.owllistener.unit.initialState;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import tw.com.owllistener.ProvidesDate;
import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.EnergyMessageChannel;
import tw.com.owllistener.network.initialState.InitialStateRecorder;
import tw.com.owllistener.network.initialState.SendDataToInitialState;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.time.Instant;
import java.util.Optional;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestInitialStateRecorder extends EasyMockSupport {

    private SendDataToInitialState sender;
    private InitialStateRecorder recorder;

    @Before
    public void beforeEachTestRuns() {
        sender = createMock(SendDataToInitialState.class);
        recorder = new InitialStateRecorder(sender);
    }

    @Test
    public void shouldRequestBucketCreationOnInit() {
        EasyMock.expect(sender.createBucket()).andReturn(Optional.of(Response.status(201).build()));
        replayAll();
        recorder.init();
        verifyAll();
    }

    @Test
    public void shouldRequestBucketExistedOnInit() {
        EasyMock.expect(sender.createBucket()).andReturn(Optional.of(Response.status(204).build()));
        replayAll();
        recorder.init();
        verifyAll();
    }

    @Test
    public void shouldReturnFalseIfConnectionIssue() {
        Instant now = Instant.now();

        String expectedPayload = String.format("[ { \"epoch\": %s, " +
                "\"key\": \"current\", \"value\": \"4.56\"} , { \"epoch\": %s, " +
                "\"key\": \"today\", \"value\": \"12.8\"" +
                "} ]", now.getEpochSecond(), now.getEpochSecond());

        EnergyMessage message = new EnergyMessage("id", now.getEpochSecond());
        EnergyMessageChannel channel = new EnergyMessageChannel(4.56, 12.8);
        message.addChannel(channel);

        EasyMock.expect(sender.sendJson(expectedPayload)).andReturn(Optional.empty());

        replayAll();
        boolean result = recorder.record(message);
        verifyAll();
        assertFalse(result);
    }

    @Test
    public void testShouldInvokeSenderWithCorrectJSON() throws IOException {
        Instant now = Instant.now();

        String expectedPayload = String.format("[ { \"epoch\": %s, " +
                "\"key\": \"current\", \"value\": \"4.56\"} , { \"epoch\": %s, " +
                "\"key\": \"today\", \"value\": \"12.8\"" +
                "} ]", now.getEpochSecond(), now.getEpochSecond());

        EnergyMessage message = new EnergyMessage("id", now.getEpochSecond());
        EnergyMessageChannel channel = new EnergyMessageChannel(4.56, 12.8);
        message.addChannel(channel);

        EasyMock.expect(sender.sendJson(expectedPayload)).andReturn(Optional.of(Response.ok().build()));

        replayAll();
        boolean result = recorder.record(message);
        verifyAll();
        assertTrue(result);
    }
}
