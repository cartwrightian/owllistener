package tw.com.owllistener.network;

public interface RecordsReadings {
    void init();
    boolean record(EnergyMessage message);
}
