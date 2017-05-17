package tw.com.owllistener.unit.initialState;


import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import tw.com.owllistener.network.initialState.SendDataToInitialState;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestSendDataToInitialState {

    public static final String BUCKET_KEY = "THE_BUCKET";
    public static final String ACCESS_KEY = "ACCESS_KEY";
    private final int port = 8089;

    //String expectedURI = "https://groker.initialstate.com/api/events";
    private final String expectedURI = "http://localhost:8089";

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(port); // No-args constructor defaults to port 8080

    @Test
    public void shouldRequestCreationOfBucketOnInitDoesNotExist() {
        SendDataToInitialState sendDataToInitialState = new SendDataToInitialState(expectedURI, ACCESS_KEY, BUCKET_KEY);

        stubFor(post(urlEqualTo("/api/buckets")).willReturn(aResponse().withStatus(201)));

        sendDataToInitialState.createBucket();

        verify(postRequestedFor(urlEqualTo("/api/buckets")).
                withHeader("Content-Type", equalTo("application/json")).
                withHeader("X-IS-AccessKey", equalTo(ACCESS_KEY)).
                withHeader("Accept-Version", equalTo("~0")).
                withRequestBody(equalTo("{ \"bucketKey\": \"THE_BUCKET\", \"bucketName\": \"OWL Energy Reading\" }")));
    }

    @Test
    public void shouldSendEventToURI() {

        stubFor(post(urlEqualTo("/api/events"))
                .willReturn(aResponse().withStatus(200)));

        SendDataToInitialState sendDataToInitialState = new SendDataToInitialState(expectedURI, ACCESS_KEY, BUCKET_KEY);
        sendDataToInitialState.sendJson("payload");

        verify(postRequestedFor(urlEqualTo("/api/events")).
                withHeader("Content-Type", equalTo("application/json")).
                withHeader("X-IS-AccessKey", equalTo(ACCESS_KEY)).
                withHeader("X-IS-BucketKey", equalTo(BUCKET_KEY)).
                withHeader("Accept-Version", equalTo("~0")).
                withRequestBody(equalTo("payload")));

    }
}
