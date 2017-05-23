package tw.com.owllistener.integration;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import tw.com.owllistener.network.ListenerConfiguration;
import tw.com.owllistener.network.ReceiveMulticastMessages;
import tw.com.owllistener.TestConfiguration;

public class TestReceiveMulticastMessages {

	private ListenerConfiguration testConfig = new TestConfiguration("url","bucketKey", "accessKey");

	@Test
	public void shouldReceiveMulticastMessage() throws IOException {
		ReceiveMulticastMessages receiver = new ReceiveMulticastMessages(testConfig);
		
		receiver.init();
		String message = receiver.getMessage();
		assertFalse(message.isEmpty());
	}
}
