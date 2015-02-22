package tw.com.owllistener;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import tw.com.owllistener.network.ReceiveMulticastMessages;

public class TestReceiveMulticastMessages {
	
	@Test
	public void shouldReceiveMulticastMessage() throws IOException {
		ReceiveMulticastMessages receiver = new ReceiveMulticastMessages();
		
		receiver.init();
		String message = receiver.getMessage();
		assertFalse(message.isEmpty());
	}
}
