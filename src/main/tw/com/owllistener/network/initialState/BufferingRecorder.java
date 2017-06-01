package tw.com.owllistener.network.initialState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.com.owllistener.network.RecordsReadings;

import java.util.LinkedList;
import java.util.Queue;

public class BufferingRecorder implements RecordsReadings {
    private static final Logger logger = LoggerFactory.getLogger(BufferingRecorder.class);

    private final RecordsReadings sender;
    private final Queue<MarshalToJson> buffer;

    public BufferingRecorder(RecordsReadings sender) {
        this.sender = sender;
        buffer = new LinkedList<>();
    }

    @Override
    public void init() {
        sender.init();
    }

    @Override
    public boolean record(Queue<MarshalToJson> messages) {
        if (!buffer.isEmpty()) {
            logger.warn("Buffer has {} messages", buffer.size());
            if (sender.record(buffer)) {
                logger.warn("Buffer sent ok, clearing buffer of {} messages", buffer.size());
                buffer.clear();
            } else {
                logger.warn("Unable to send buffered messages");
                buffer.addAll(messages);
            }
        }

        if (buffer.isEmpty()) {
            if (sender.record(messages)) {
                logger.debug("Sent message {} ok", messages);
            } else {
                logger.warn("Unable to send messages " + messages);
                buffer.addAll(messages);
            }
        }

        return true;
    }
}
