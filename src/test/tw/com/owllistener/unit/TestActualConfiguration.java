package tw.com.owllistener.unit;

import org.junit.Test;
import tw.com.owllistener.network.initialState.ActualConfiguration;

import static junit.framework.TestCase.assertEquals;

public class TestActualConfiguration {

    @Test
    public void ShouldGetUDPPortOK() {
        ActualConfiguration configuration = new ActualConfiguration();

        // standard port
        assertEquals(22600, configuration.getOwlPort()) ;
    }
}
