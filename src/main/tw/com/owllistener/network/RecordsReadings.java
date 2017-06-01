package tw.com.owllistener.network;

import tw.com.owllistener.network.initialState.MarshalToJson;

import java.util.Queue;

public interface RecordsReadings {
    void init();
    boolean record(Queue<MarshalToJson> message);
}
