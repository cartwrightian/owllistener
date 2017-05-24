package tw.com.owllistener.network.initialState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.com.owllistener.ProvidesDate;
import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.EnergyMessageChannel;
import tw.com.owllistener.network.RecordsReadings;

import javax.ws.rs.core.Response;
import java.time.Instant;

public class InitialStateRecorder implements RecordsReadings {
    private static final Logger logger = LoggerFactory.getLogger(InitialStateRecorder.class);

    private final SendDataToInitialState sender;
    private ProvidesDate dateProvider;

    public InitialStateRecorder(SendDataToInitialState sender, ProvidesDate dateProvider) {
        this.sender = sender;
        this.dateProvider = dateProvider;
    }

    @Override
    public void init() {
        Response response = sender.createBucket();
        if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            if (response.getStatus()==201) {
                logger.info("Bucket was created");
            } else if (response.getStatus()==204) {
                logger.info("Bucket existed");
            }
        } else {
            logger.error(String.format("Failure create bucket %s and %s ", response.getStatus(), response));
        }
    }

    @Override
    public boolean record(EnergyMessage message) {
        Instant instance = dateProvider.getInstant();

        EnergyMessageChannel channel = message.getChannel(0);

        long epoch = instance.getEpochSecond();
        String current = formJason(epoch, "current", channel.getCurrent());
        String today = formJason(epoch, "today", channel.getDayTotal());
        String json = String.format("[ %s , %s ]", current, today);

        logger.info(String.format("Message: %s JSON: %s", message, json));
        Response response = sender.sendJson(json);
        if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            logger.info("Sent event ok");
            return true;
        } else {
            logger.error(String.format("Failure sending event %s and %s", response.getStatus(), response));
            // TODO need to implement retry strategy
            return false;
        }
    }

    private String formJason(long epoch, String key, Double value) {
        return String.format("{ \"epoch\": %s, \"key\": \"%s\", \"value\": \"%s\"}", epoch, key, value);
    }
}
