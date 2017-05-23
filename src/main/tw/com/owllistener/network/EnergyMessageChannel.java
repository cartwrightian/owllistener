package tw.com.owllistener.network;

public class EnergyMessageChannel {

	private Double currentReading;
	private Double dayReading;

	public EnergyMessageChannel(Double currentReading, Double dayReading) {
		this.currentReading = currentReading;
		this.dayReading = dayReading;
	}

	public Double getCurrent() {
		return currentReading;
	}

	public Double getDayTotal() {
		return dayReading;
	}

}
