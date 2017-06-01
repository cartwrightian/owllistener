package tw.com.owllistener;

import tw.com.owllistener.network.EnergyMessage;
import tw.com.owllistener.network.initialState.MarshalToJson;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class QueueHelper {

    public static Queue<MarshalToJson> asQueue(EnergyMessage... messages) {
        Queue<MarshalToJson> theQueue = new LinkedList<>();
        theQueue.addAll(Arrays.asList(messages));
        return theQueue;
    }
}
