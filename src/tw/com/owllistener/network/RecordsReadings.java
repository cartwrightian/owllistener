package tw.com.owllistener.network;

import java.io.IOException;

public interface RecordsReadings {
    void record(EnergyMessage message) throws IOException;
}
