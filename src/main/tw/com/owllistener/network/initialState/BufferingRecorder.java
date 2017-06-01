package tw.com.owllistener.network.initialState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.com.owllistener.network.RecordsReadings;

import java.util.LinkedList;
import java.util.Queue;

public class BufferingRecorder implements RecordsReadings {
    private static final Logger logger = LoggerFactory.getLogger(RecordsReadings.class);

    private final RecordsReadings contains;
    private final Queue<MarshalToJson> queue;

    public BufferingRecorder(RecordsReadings contains) {
        this.contains = contains;
        queue = new LinkedList<>();
    }

    @Override
    public void init() {
        contains.init();
    }

    @Override
    public boolean record(MarshalToJson message) {
        while(!queue.isEmpty()) {
            MarshalToJson candidate = queue.peek();
            if (contains.record(candidate)) {
                queue.remove();
            } else {
                queue.add(message);
                break;
            }
        }

        if (queue.isEmpty()) {
            if (contains.record(message)) {
                logger.debug("Send message ok");
            } else {
                queue.add(message);
            }
        }

        return true;
    }
}
