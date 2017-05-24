package tw.com.owllistener.unit;

import org.junit.Test;
import tw.com.owllistener.network.configuration.ActualConfiguration;

import static junit.framework.TestCase.assertEquals;

public class TestActualConfiguration {

    @Test
    public void ShouldGetUDPPortOK() {
        ActualConfiguration configuration = new ActualConfiguration();

        // standard port
        assertEquals(22600, configuration.getOwlPort()) ;

        // the standard ip multicast address
        assertEquals("224.192.32.19", configuration.getOwlMulicastAddress());
    }
}
