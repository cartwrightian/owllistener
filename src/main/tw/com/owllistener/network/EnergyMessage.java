package tw.com.owllistener.network;

import java.util.LinkedList;
import java.util.List;

public class EnergyMessage {

	private List<EnergyMessageChannel> channels;
	private String id;
	private Integer batteryLevel;
	
	public EnergyMessage(String id) {
		this.id = id;
		channels = new LinkedList<>();
	}

	public Integer batteryLevel() {
		return batteryLevel;
	}
	
	public void setBatteryLevel(Integer batteryLevel) {
		this.batteryLevel = batteryLevel;	
	}

	public String getUnitMac() {
		return id;
	}

	public EnergyMessageChannel getChannel(int index) {
		return channels.get(index);
	}

	public int getNumChannels() {
		return channels.size();
	}

	public void addChannel(EnergyMessageChannel channel) {
		channels.add(channel);		
	}

	@Override
	public String toString() {
		return "EnergyMessage{" +
				"channels=" + channels +
				", id='" + id + '\'' +
				", batteryLevel=" + batteryLevel +
				'}';
	}
}
