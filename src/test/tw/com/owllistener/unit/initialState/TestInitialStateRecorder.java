package tw.com.owllistener.unit.initialState;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
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
import static tw.com.owllistener.QueueHelper.asQueue;

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

        EnergyMessage message = createTestMessage(now, 4.56, 12.8);

        EasyMock.expect(sender.sendJson(expectedPayload)).andReturn(Optional.empty());

        replayAll();
        boolean result = recorder.record(asQueue(message));
        verifyAll();
        assertFalse(result);
    }

    @Test
    public void testShouldInvokeSenderWithCorrectJSONSingleMessage() throws IOException {
        Instant now = Instant.now();

        String expectedPayload = String.format("[ { \"epoch\": %s, " +
                "\"key\": \"current\", \"value\": \"4.56\"} , { \"epoch\": %s, " +
                "\"key\": \"today\", \"value\": \"12.8\"" +
                "} ]", now.getEpochSecond(), now.getEpochSecond());

        EnergyMessage message = createTestMessage(now, 4.56, 12.8);

        EasyMock.expect(sender.sendJson(expectedPayload)).andReturn(Optional.of(Response.ok().build()));

        replayAll();
        boolean result = recorder.record(asQueue(message));
        verifyAll();
        assertTrue(result);
    }

    @Test
    public void testShouldInvokeSenderWithCorrectJSONQueueOfMessage() throws IOException {
        Instant now = Instant.now();

        long epoch = now.getEpochSecond();
        String expectedPayload = String.format("[ " +
                "{ \"epoch\": %s, \"key\": \"current\", \"value\": \"3.33\"} , " +
                "{ \"epoch\": %s, \"key\": \"today\", \"value\": \"103.33\"} , " +
                "{ \"epoch\": %s, \"key\": \"current\", \"value\": \"4.44\"} , " +
                "{ \"epoch\": %s, \"key\": \"today\", \"value\": \"104.44\"} , " +
                "{ \"epoch\": %s, \"key\": \"current\", \"value\": \"5.55\"} , " +
                "{ \"epoch\": %s, \"key\": \"today\", \"value\": \"105.55\"}" +

                " ]", epoch, epoch, epoch, epoch, epoch, epoch);

        EnergyMessage messageA = createTestMessage(now, 3.33, 103.33);
        EnergyMessage messageB = createTestMessage(now, 4.44, 104.44);
        EnergyMessage messageC = createTestMessage(now, 5.55, 105.55);

        EasyMock.expect(sender.sendJson(expectedPayload)).andReturn(Optional.of(Response.ok().build()));

        replayAll();
        boolean result = recorder.record(asQueue(messageA, messageB, messageC));
        verifyAll();
        assertTrue(result);
    }

    private EnergyMessage createTestMessage(Instant now, double currentReading, double dayReading) {
        EnergyMessage message = new EnergyMessage("id", now.getEpochSecond());
        EnergyMessageChannel channel = new EnergyMessageChannel(currentReading, dayReading);
        message.addChannel(channel);
        return message;
    }
}
