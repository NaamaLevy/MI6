package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

public class AgentsAvailableEvent implements Event<Boolean> {

    private Boolean isAgentsAvailable;

    public AgentsAvailableEvent(Boolean isAgentsAvailable) {
        this.isAgentsAvailable = isAgentsAvailable;
    }

    public Boolean getIsAgentsAvailable() { return isAgentsAvailable; }



}
