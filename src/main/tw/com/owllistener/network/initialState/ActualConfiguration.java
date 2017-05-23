package tw.com.owllistener.network.initialState;

import com.typesafe.config.Config;
import tw.com.owllistener.network.ListenerConfiguration;
import com.typesafe.config.ConfigFactory;

public class ActualConfiguration implements ListenerConfiguration{

    private final Config conf;

    private static final String OWL_MULTICAST_ADDRESS = "224.192.32.19";
    //private static final int OWL_PORT = 22600;

    public ActualConfiguration() {
        conf = ConfigFactory.load();
    }

    @Override
    public int getOwlPort() {
        return conf.getInt("owlListener.owlUDPPort");
        //return OWL_PORT;
    }

    @Override
    public String getOwlMulicastAddress() {
        return OWL_MULTICAST_ADDRESS;
    }

    @Override
    public String getInitialStateBaseURL() {
        return "https://groker.initialstate.com/api/events";
    }

    @Override
    public String getInitialStateAccessKey() {
        return "tbc";
    }

    @Override
    public String getInitialStateBucketKey() {
        return "tbc";
    }
}
