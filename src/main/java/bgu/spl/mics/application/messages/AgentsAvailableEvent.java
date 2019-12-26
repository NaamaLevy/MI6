package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class AgentsAvailableEvent<Boolean> implements Event {

    private Boolean isAgentsAvailable;


    private int time;

    public AgentsAvailableEvent(List<String> agentsName) {
        this.isAgentsAvailable = isAgentsAvailable;
    }
    public Boolean getIsAgentsAvailable() { return isAgentsAvailable; }
    public int getTime() { return time;}
    public void setTime(int time) { this.time = time; }

}


