package tw.com.owllistener.network;

import java.io.IOException;

public interface RecordsReadings {
    void init();
    boolean record(EnergyMessage message);
}
