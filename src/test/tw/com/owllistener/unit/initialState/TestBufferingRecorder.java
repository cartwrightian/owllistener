package tw.com.owllistener.unit.initialState;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.EnergyMessageChannel;
import tw.com.owllistener.network.RecordsReadings;
import tw.com.owllistener.network.initialState.BufferingRecorder;

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

        EasyMock.expect(contained.record(messageA)).andReturn(false);
        EasyMock.expect(contained.record(messageA)).andReturn(true);
        EasyMock.expect(contained.record(messageB)).andReturn(true);

        replayAll();
        recorder.record(messageA);
        recorder.record(messageB);
        verifyAll();
    }

    @Test
    public void shouldBufferMultipleMessageIfUnableToSend() {
        EnergyMessage messageA = createMessage("42", 8842);
        EnergyMessage messageB = createMessage("43", 8843);
        EnergyMessage messageC = createMessage("44", 8844);
        EnergyMessage messageD = createMessage("45", 8845);

        EasyMock.expect(contained.record(messageA)).andReturn(false);
        EasyMock.expect(contained.record(messageA)).andReturn(false);
        EasyMock.expect(contained.record(messageA)).andReturn(false);

        EasyMock.expect(contained.record(messageA)).andReturn(true);
        EasyMock.expect(contained.record(messageB)).andReturn(true);
        EasyMock.expect(contained.record(messageC)).andReturn(true);
        EasyMock.expect(contained.record(messageD)).andReturn(true);

        replayAll();
        recorder.record(messageA);
        recorder.record(messageB);
        recorder.record(messageC);
        recorder.record(messageD);
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
        EasyMock.expect(contained.record(message)).andReturn(true);

        replayAll();
        recorder.record(message);
        verifyAll();
    }

}
