package tw.com.owllistener.network.initialState;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.com.owllistener.network.ListenerConfiguration;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SendDataToInitialState {
    private static final Logger logger = LoggerFactory.getLogger(SendDataToInitialState.class);

    private String accessKey;
    private String bucketKey;
    private String baseURI;

    public SendDataToInitialState(ListenerConfiguration configuration) {
        this.baseURI = configuration.getInitialStateBaseURL();
        this.accessKey = configuration.getInitialStateAccessKey();
        this.bucketKey = configuration.getInitialStateBucketKey();
    }

    public Response sendJson(String json) {
        Client client = ClientBuilder.newClient();

        String uri = baseURI + "/api/events";
        logger.info(String.format("Send json '%s' to baseURI %s", json, uri));
        WebTarget target = client.target(uri);
        Entity<?> entity = Entity.json(json);
        Invocation builder = target.request(MediaType.APPLICATION_JSON).
                header("X-IS-AccessKey", accessKey).
                header("X-IS-BucketKey", bucketKey).
                header("Accept-Version", "~0").buildPost(entity);

        Response result = builder.invoke();
        client.close();
        return result;
    }

    public Response createBucket() {
        Client client = ClientBuilder.newClient();

        String json = String.format("{ \"bucketKey\": \"%s\", \"bucketName\": \"%s\" }", bucketKey, "OWL Energy Reading");

        String uri = baseURI + "/api/buckets";
        logger.info(String.format("Send json '%s' to baseURI %s", json, uri));
        WebTarget target = client.target(uri);
        Entity<?> entity = Entity.json(json);
        Invocation builder = target.request(MediaType.APPLICATION_JSON).
                header("X-IS-AccessKey", accessKey).
                header("Accept-Version", "~0").buildPost(entity);

        Response result = builder.invoke();
        client.close();
        return result;

    }
}
