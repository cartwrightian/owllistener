package tw.com.owllistener.network.initialState;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SendDataToInitialState {
    private static final Logger logger = LoggerFactory.getLogger(SendDataToInitialState.class);

    private String accessKey;
    private String bucket;
    private String uri;

    public SendDataToInitialState(String uri, String accessKey, String bucket) {
        this.uri = uri;
        this.accessKey = accessKey;
        this.bucket = bucket;
    }

    public Response sendJson(String json) {
        Client client = ClientBuilder.newClient();

        logger.info(String.format("Send json '%s' to uri %s", json,uri));
        WebTarget target = client.target(uri);
        Entity<?> entity = Entity.json(json);
        Invocation builder = target.request(MediaType.APPLICATION_JSON).
                header("X-IS-AccessKey", accessKey).
                header("X-IS-BucketKey", bucket).
                header("Accept-Version", "~0").buildPost(entity);

        Response result = builder.invoke();
        client.close();
        return result;
    }

    public void createBucket() {

    }
}
