package tw.com.owllistener.unit.initialState;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.RecordsReadings;
import tw.com.owllistener.network.initialState.BufferingRecorder;
import tw.com.owllistener.network.initialState.MarshalToJson;

import java.util.Queue;

import static tw.com.owllistener.QueueHelper.asQueue;

public class TestBufferingRecorder extends EasyMockSupport {

    private RecordsReadings contained;
    private BufferingRecorder recorder;

    @Before
    public void beforeEachTestRuns() {
        contained = createMock(RecordsReadings.class);
        recorder = new BufferingRecorder(contained);
    }

    @Test
    public void shouldInvokeInitOnContained() {
        contained.init();
        EasyMock.expectLastCall();

        replayAll();
        recorder.init();
        verifyAll();
    }

    @Test
    public void shouldBufferSingleMessageIfUnableToSend() {
        EnergyMessage messageA = createMessage("42", 8842);
        EnergyMessage messageB = createMessage("43", 8843);

        EasyMock.expect(contained.record(asQueue(messageA))).andReturn(false);
        EasyMock.expect(contained.record(asQueue(messageA))).andReturn(true);
        EasyMock.expect(contained.record(asQueue(messageB))).andReturn(true);


        replayAll();
        recorder.record(asQueue(messageA));
        recorder.record(asQueue(messageB));
        verifyAll();
    }

    @Test
    public void shouldBufferMultipleMessageIfUnableToSend() {
        EnergyMessage messageA = createMessage("42", 8842);
        EnergyMessage messageB = createMessage("43", 8843);
        EnergyMessage messageC = createMessage("44", 8844);
        EnergyMessage messageD = createMessage("45", 8845);

        // try A, fail, add S
        EasyMock.expect(contained.record(asQueue(messageA))).andReturn(false);
        // try queue, fail, add B
        EasyMock.expect(contained.record(asQueue(messageA))).andReturn(false);
        // try queue, fail, add C
        EasyMock.expect(contained.record(asQueue(messageA, messageB))).andReturn(false);
        // try queue, ok, then send D
        EasyMock.expect(contained.record(asQueue(messageA, messageB, messageC))).andReturn(true);
        EasyMock.expect(contained.record(asQueue(messageD))).andReturn(true);

        replayAll();
        recorder.record(asQueue(messageA));
        recorder.record(asQueue(messageB));
        recorder.record(asQueue(messageC));
        recorder.record(asQueue(messageD));
        verifyAll();
    }

    private EnergyMessage createMessage(String id, int epoch) {
        EnergyMessage message = new EnergyMessage(id, epoch);
        message.setBatteryLevel(22);
        return message;
    }

    @Test
    public void shouldCallContaintedWithSuppliedMessage() {
        EnergyMessage message = new EnergyMessage("42", 8842);

        Queue<MarshalToJson> theQueue = asQueue(message);
        EasyMock.expect(contained.record(theQueue)).andReturn(true);

        replayAll();
        recorder.record(theQueue);
        verifyAll();
    }

}
