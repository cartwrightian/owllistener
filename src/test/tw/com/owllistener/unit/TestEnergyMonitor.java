package tw.com.owllistener.unit;


import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;
import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.EnergyMonitor;
import tw.com.owllistener.network.ReceiveMessages;
import tw.com.owllistener.network.RecordsReadings;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.util.Optional;

public class TestEnergyMonitor extends EasyMockSupport {

    private RecordsReadings recorder;
    private ReceiveMessages receiver;

    @Before
    public void beforeEachTestRuns() {
        recorder = createMock(RecordsReadings.class);
        receiver = createMock(ReceiveMessages.class);
    }

    @Test
    public void shouldRecordMessageAndStopIfRequested() throws SAXException, ParserConfigurationException, XPathExpressionException, IOException {

        EnergyMessage messageA = new EnergyMessage("idA", 1223);
        EnergyMessage messageB = new EnergyMessage("idB", 1224);

        recorder.init();
        EasyMock.expectLastCall();
        receiver.init();
        EasyMock.expectLastCall();

        EasyMock.expect(receiver.receiveNextMessage()).andReturn(Optional.of(messageA));
        EasyMock.expect(recorder.record(messageA)).andReturn(true);
        EasyMock.expect(receiver.receiveNextMessage()).andReturn(Optional.of(messageB));
        EasyMock.expect(recorder.record(messageB)).andReturn(false);

        replayAll();
        EnergyMonitor energyMonitor = new EnergyMonitor(receiver, recorder);
        energyMonitor.loop();
        verifyAll();
    }
}
