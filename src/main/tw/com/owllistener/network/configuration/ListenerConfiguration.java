package tw.com.owllistener.network.configuration;

public interface ListenerConfiguration {
    int getOwlPort();
    String getOwlMulicastAddress();
    String getInitialStateBaseURL();
    String getInitialStateAccessKey();
    String getInitialStateBucketKey();
}
