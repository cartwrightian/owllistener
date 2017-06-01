package tw.com.owllistener.network.initialState;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.com.owllistener.network.configuration.ListenerConfiguration;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SendDataToInitialState {
    private static final Logger logger = LoggerFactory.getLogger(SendDataToInitialState.class);

    private final Client client;
    private String accessKey;
    private String bucketKey;
    private String baseURI;

    public SendDataToInitialState(ListenerConfiguration configuration) {
        this.baseURI = configuration.getInitialStateBaseURL();
        this.accessKey = configuration.getInitialStateAccessKey();
        this.bucketKey = configuration.getInitialStateBucketKey();
        client = ClientBuilder.newClient();
    }

    public Optional<Response> sendJson(String json) {
        String uri = baseURI + "/api/events";

        Invocation builder = createCommonHeaders(uri).
                header("X-IS-BucketKey", bucketKey).buildPost(Entity.json(json));

        logger.info(String.format("Send json '%s' to baseURI %s", json, uri));
        return getResponse(builder);
    }

    public Optional<Response> createBucket() {
        String json = String.format("{ \"bucketKey\": \"%s\", \"bucketName\": \"%s\" }", bucketKey, "OWL Energy Reading");
        String uri = baseURI + "/api/buckets";

        Invocation builder = createCommonHeaders(uri).buildPost(Entity.json(json));

        logger.info(String.format("Send json '%s' to baseURI %s", json, uri));
        return getResponse(builder);
    }

    private Optional<Response> getResponse(Invocation builder) {
        Future<Response> future = builder.submit();
        try {
            return Optional.of(future.get());
        } catch (InterruptedException e) {
            logger.error("InterruptedException sending message",e);
        } catch (ExecutionException e) {
            logger.error("ExecutionException  sending message",e);
        }
        return Optional.empty();

    }

    public Invocation.Builder createCommonHeaders(String uri) {
        WebTarget target = client.target(uri);
        return target.request(MediaType.APPLICATION_JSON).
                header("X-IS-AccessKey", accessKey).
                header("Accept-Version", "~0");
    }
}
