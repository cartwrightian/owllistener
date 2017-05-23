package tw.com.owllistener.network;

public interface ListenerConfiguration {
    int getOwlPort();
    String getOwlMulicastAddress();
    String getInitialStateBaseURL();
    String getInitialStateAccessKey();
    String getInitialStateBucketKey();
}
