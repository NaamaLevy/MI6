package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class AgentsAvailableEvent<Boolean> implements Event {

    private Boolean isAgentsAvailable;

    public AgentsAvailableEvent() {
        this.isAgentsAvailable = isAgentsAvailable;
    }

    public Boolean getIsAgentsAvailable() { return isAgentsAvailable; }

}
