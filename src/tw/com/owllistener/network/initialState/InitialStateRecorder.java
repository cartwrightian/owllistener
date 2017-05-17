package tw.com.owllistener.network.initialState;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.com.owllistener.ProvidesDate;
import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.EnergyMessageChannel;
import tw.com.owllistener.network.RecordsReadings;

import javax.ws.rs.core.Response;
import java.io.IOException;

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
        sender.createBucket();
    }

    @Override
    public boolean record(EnergyMessage message) {
        long epoch = dateProvider.getDate().getTime();

        EnergyMessageChannel channel = message.getChannel(0);

        String json = String.format("[ { \"epoch\": %s, " +
                "\"key\": \"current\", \"value\": \"%s\"" +
                "\"key\": \"today\", \"value\": \"%s\"" +
                "}]", epoch, channel.getCurrent(), channel.getDayTotal());

        logger.info(String.format("Message: %s JSON: %s", message, json));
        Response response = sender.sendJson(json);
        if (response.getStatusInfo().getFamily().equals(Response.Status.Family.SUCCESSFUL)) {
            logger.info("Sent event ok");
            return true;
        } else {
            logger.error("Failure sending event " + response);
            // TODO need to implement retry strategy
            return false;
        }
    }
}
