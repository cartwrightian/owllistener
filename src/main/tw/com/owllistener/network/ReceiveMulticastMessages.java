package tw.com.owllistener.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tw.com.owllistener.network.configuration.ListenerConfiguration;

public class ReceiveMulticastMessages {
	private static final Logger logger = LoggerFactory.getLogger(ReceiveMulticastMessages.class);
    private ListenerConfiguration configuration;
    private MulticastSocket socket;

    public ReceiveMulticastMessages(ListenerConfiguration configuration) {
        this.configuration = configuration;
    }

	public void init() throws IOException {
		int owlPort = configuration.getOwlPort();
		logger.info(String.format("Listening on port %s address %s", owlPort, configuration.getOwlMulicastAddress()));
		socket = new MulticastSocket(owlPort);
		InetAddress address = InetAddress.getByName(configuration.getOwlMulicastAddress());
		logger.info(String.format("Attempt join Mulicast Group for port:%s address:%s", owlPort, address));
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
