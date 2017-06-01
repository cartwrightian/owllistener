package tw.com.owllistener.network.initialState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.com.owllistener.ProvidesDate;
import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.EnergyMessageChannel;
import tw.com.owllistener.network.RecordsReadings;

import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import static java.lang.String.format;

public class InitialStateRecorder implements RecordsReadings {
    private static final Logger logger = LoggerFactory.getLogger(InitialStateRecorder.class);

    private final SendDataToInitialState sender;

    public InitialStateRecorder(SendDataToInitialState sender) {
        this.sender = sender;
    }

    @Override
    public void init() {
        Optional<Response> optional = sender.createBucket();
        if (optional.isPresent()) {
            Response response = optional.get();
            int status = response.getStatus();
            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                if (status == 201) {
                    logger.info("Bucket was created");
                } else if (status == 204) {
                    logger.info("Bucket existed");
                } else {
                    logger.warn("Successful but unexpected status for Bucket creation " + status);
                }
            } else {
                logger.error(format("Failure create bucket %s and %s ", status, response));
            }
        } else {
            logger.warn("Unable to make create bucket call during init");
        }
    }

    @Override
    public boolean record(Queue<MarshalToJson> queue) {
        String json = formJsonArray(queue);
        logger.info(format("Message queue size: %s JSON: %s", queue.size(), json));

        Optional<Response> optional = sender.sendJson(json);
        if (optional.isPresent()) {
            Response response = optional.get();
            if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
                logger.info("Sent event ok");
                return true;
            } else {
                logger.error(format("Failure sending event %s and %s", response.getStatus(), response));
                return false;
            }
        } else {
            logger.warn("Unable to send energy message to initial state");
            return false;
        }
    }

    private String formJsonArray(Queue<MarshalToJson> queue) {
        StringBuilder builder = new StringBuilder();

        while (!queue.isEmpty()) {
            List<String> elements = queue.remove().toJson();
            elements.forEach(part -> {
                if (builder.length()>0) {
                    builder.append(",");
                }
                builder.append(format(" %s ", part));
            });

        }
        return format("[%s]", builder.toString());
    }

}
