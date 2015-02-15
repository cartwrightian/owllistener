package tw.com.owllistener.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReceiveMulticastMessages {
	private static final Logger logger = LoggerFactory.getLogger(ReceiveMulticastMessages.class);

	//
	private static final String OWL_MULTICAST_ADDRESS = "224.192.32.19";
	private static final int OWL_PORT = 22600;
	MulticastSocket socket;

	public void init() throws IOException {
		logger.info(String.format("Listening on port %s address %s", OWL_PORT, OWL_MULTICAST_ADDRESS));
		socket = new MulticastSocket(OWL_PORT);
		InetAddress address = InetAddress.getByName(OWL_MULTICAST_ADDRESS);
		socket.joinGroup(address);
	}

	public String getMessage() throws IOException {
		byte[] inBuf = new byte[2048];
		DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
        socket.receive(inPacket);
        String text = new String(inBuf, 0, inPacket.getLength());
        logger.info("Received packet " + text);
        return text;
	}

}
