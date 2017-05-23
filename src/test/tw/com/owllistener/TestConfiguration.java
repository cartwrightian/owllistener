package tw.com.owllistener;

import tw.com.owllistener.network.ListenerConfiguration;

public class TestConfiguration implements ListenerConfiguration {
    private final String baseURI;
    private final String bucketKey;
    private final String accessKey;

    public TestConfiguration(String baseURI, String bucketKey, String accessKey) {
        this.baseURI = baseURI;
        this.bucketKey = bucketKey;
        this.accessKey = accessKey;
    }

    @Override
    public int getOwlPort() {
        return 22600;
    }

    @Override
    public String getOwlMulicastAddress() {
        return "224.0.0.118";
    }

    @Override
    public String getInitialStateBaseURL() {
        return baseURI;
    }

    @Override
    public String getInitialStateAccessKey() {
        return accessKey;
    }

    @Override
    public String getInitialStateBucketKey() {
        return bucketKey;
    }
}
