package bgu.spl.mics.example.messages;

import bgu.spl.mics.Event;

public class ExampleEvent<String> implements Event<String>{

    private String senderName;

    public ExampleEvent(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderName() {
        return senderName;
    }
}