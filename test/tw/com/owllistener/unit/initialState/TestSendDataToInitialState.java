package tw.com.owllistener.unit.initialState;


import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import tw.com.owllistener.network.initialState.SendDataToInitialState;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class TestSendDataToInitialState {

    private final int port = 8089;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(port); // No-args constructor defaults to port 8080

    @Test
    public void shouldSendEventToURI() {

        //String expectedURI = "https://groker.initialstate.com/api/events";
        String expectedURI = "http://localhost:8089/api/events";

        stubFor(post(urlEqualTo("/api/events"))
                .willReturn(aResponse().withStatus(200)));

        SendDataToInitialState sendDataToInitialState = new SendDataToInitialState(expectedURI,"ACCESS_KEY", "THE_BUCKET");
        sendDataToInitialState.sendJson("payload");

        verify(postRequestedFor(urlEqualTo("/api/events")).
                withHeader("Content-Type", equalTo("application/json")).
                withHeader("X-IS-AccessKey", equalTo("ACCESS_KEY")).
                withHeader("X-IS-BucketKey", equalTo("THE_BUCKET")).
                withHeader("Accept-Version", equalTo("~0")).
                withRequestBody(equalTo("payload")));

    }
}
