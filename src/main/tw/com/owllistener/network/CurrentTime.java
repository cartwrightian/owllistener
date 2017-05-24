package tw.com.owllistener.network;

import tw.com.owllistener.ProvidesDate;

import java.time.Instant;

public class CurrentTime implements ProvidesDate {
    @Override
    public Instant getInstant() {
        return Instant.now();
    }

}
