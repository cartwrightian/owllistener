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
import java.util.Date;

public class TestInitialStateRecorder extends EasyMockSupport {

    private SendDataToInitialState sender;
    private ProvidesDate providesDate;
    private InitialStateRecorder recorder;

    @Before
    public void beforeEachTestRuns() {
        sender = createMock(SendDataToInitialState.class);
        providesDate = createMock(ProvidesDate.class);
        recorder = new InitialStateRecorder(sender, providesDate);
    }

    @Test
    public void testShouldRequestBucketCreationOnInit() {
        EasyMock.expect(sender.createBucket()).andReturn(Response.status(201).build());
        replayAll();
        recorder.init();
        verifyAll();
    }

    @Test
    public void testShouldRequestBucketExistedOnInit() {
        EasyMock.expect(sender.createBucket()).andReturn(Response.status(204).build());
        replayAll();
        recorder.init();
        verifyAll();
    }

    @Test
    public void testShouldInvokeSenderWithCorrectJSON() throws IOException {
        Date now = new Date();

        String expectedPayload = String.format("[ { \"epoch\": %s, " +
                "\"key\": \"current\", \"value\": \"4.56\"" +
                "\"key\": \"today\", \"value\": \"12.8\"" +
                "}]", now.getTime());

        EnergyMessage message = new EnergyMessage("id");
        EnergyMessageChannel channel = new EnergyMessageChannel(4.56, 12.8);
        message.addChannel(channel);
        EasyMock.expect(providesDate.getDate()).andReturn(now);

        EasyMock.expect(sender.sendJson(expectedPayload)).andReturn(Response.ok().build());

        replayAll();
        recorder.record(message);
        verifyAll();
    }
}
