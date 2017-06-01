package tw.com.owllistener.network;

import tw.com.owllistener.network.initialState.MarshalToJson;

import java.util.LinkedList;
import java.util.List;

public class EnergyMessage implements MarshalToJson {

	private List<EnergyMessageChannel> channels;
	private String id;
    private long epoch;
    private Integer batteryLevel;

    public EnergyMessage(String id, long epoch) {
		this.id = id;
        this.epoch = epoch;
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
                ", epoch=" + epoch +
                ", batteryLevel=" + batteryLevel +
                '}';
    }

    @Override
	public List<String> toJson() {
		EnergyMessageChannel channel = getChannel(0);
		List<String> readings = new LinkedList<>();
		String current = formJason("current", channel.getCurrent());
		String today = formJason("today", channel.getDayTotal());
		readings.add(current);
		readings.add(today);
		return readings;
	}

	private String formJason(String key, Double value) {
		return String.format("{ \"epoch\": %s, \"key\": \"%s\", \"value\": \"%s\"}", epoch, key, value);
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnergyMessage message = (EnergyMessage) o;

        if (epoch != message.epoch) return false;
        if (channels != null ? !channels.equals(message.channels) : message.channels != null) return false;
        if (id != null ? !id.equals(message.id) : message.id != null) return false;
        return batteryLevel.equals(message.batteryLevel);
    }

    @Override
    public int hashCode() {
        int result = channels != null ? channels.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        result = 31 * result + (int) (epoch ^ (epoch >>> 32));
        result = 31 * result + batteryLevel.hashCode();
        return result;
    }
}
