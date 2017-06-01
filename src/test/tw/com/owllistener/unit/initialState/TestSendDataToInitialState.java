package tw.com.owllistener.unit.initialState;


import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import tw.com.owllistener.TestConfiguration;
import tw.com.owllistener.network.initialState.SendDataToInitialState;

import javax.ws.rs.core.Response;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

public class TestSendDataToInitialState {

    public static final String BUCKET_KEY = "THE_BUCKET";
    public static final String ACCESS_KEY = "ACCESS_KEY";
    private final String expectedURI = "http://localhost:8089";

    private final int port = 8089;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(port);

    private SendDataToInitialState sendDataToInitialState;

    @Before
    public void beforeEachTestRuns() {
        sendDataToInitialState = new SendDataToInitialState(
                new TestConfiguration(expectedURI, BUCKET_KEY, ACCESS_KEY));
    }

    @Test
    public void shouldRequestCreationOfBucketOnInitDoesNotExist() {

        stubFor(post(urlEqualTo("/api/buckets")).willReturn(aResponse().withStatus(201)));

        Response response = sendDataToInitialState.createBucket().get();

        verify(postRequestedFor(urlEqualTo("/api/buckets")).
                withHeader("Content-Type", equalTo("application/json")).
                withHeader("X-IS-AccessKey", equalTo(ACCESS_KEY)).
                withHeader("Accept-Version", equalTo("~0")).
                withRequestBody(equalTo("{ \"bucketKey\": \"THE_BUCKET\", \"bucketName\": \"OWL Energy Reading\" }")));

        assertEquals(201,response.getStatus());
    }

    @Test
    public void shouldSendEventToURI() {

        stubFor(post(urlEqualTo("/api/events")).willReturn(aResponse().withStatus(200)));

        Response response = sendDataToInitialState.sendJson("payload").get();

        verify(postRequestedFor(urlEqualTo("/api/events")).
                withHeader("Content-Type", equalTo("application/json")).
                withHeader("X-IS-AccessKey", equalTo(ACCESS_KEY)).
                withHeader("X-IS-BucketKey", equalTo(BUCKET_KEY)).
                withHeader("Accept-Version", equalTo("~0")).
                withRequestBody(equalTo("payload")));

        assertEquals(200,response.getStatus());
    }

    @Test
    public void shouldHandleServerError() {
        TestConfiguration badConfig = new TestConfiguration("http://no.such.server:8089", BUCKET_KEY, ACCESS_KEY);
        SendDataToInitialState noSuchServer = new SendDataToInitialState(badConfig);

        Optional<Response> response = noSuchServer.sendJson("payload");

        assertFalse(response.isPresent());
    }
}
