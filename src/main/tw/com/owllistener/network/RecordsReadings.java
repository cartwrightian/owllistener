package tw.com.owllistener.network;

import tw.com.owllistener.network.initialState.MarshalToJson;

public interface RecordsReadings {
    void init();
    boolean record(MarshalToJson message);
}
