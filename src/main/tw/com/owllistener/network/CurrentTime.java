package tw.com.owllistener.network;

import tw.com.owllistener.ProvidesDate;

import java.util.Date;

public class CurrentTime implements ProvidesDate {
    @Override
    public Date getDate() {
        return new Date();
    }

}
