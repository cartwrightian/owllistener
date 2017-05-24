package tw.com.owllistener.network.configuration;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;

public class ActualConfiguration implements ListenerConfiguration{

    private final Config conf;

    public ActualConfiguration() {
        conf = ConfigFactory.load();
    }

    @Override
    public int getOwlPort() {
        return conf.getInt("owlListener.owlUDPPort");
    }

    @Override
    public String getOwlMulicastAddress() {
        return conf.getString("owlListener.owlMulticastAddress");
    }

    @Override
    public String getInitialStateBaseURL() {
        return "https://groker.initialstate.com/";
    }

    @Override
    public String getInitialStateAccessKey() {
        return conf.getString("owlListener.accessKey");
    }

    @Override
    public String getInitialStateBucketKey() {
        return conf.getString("owlListener.bucketKey");
    }
}
