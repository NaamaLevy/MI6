package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import bgu.spl.mics.Future;
import bgu.spl.mics.application.passiveObjects.Agent;

import java.util.List;

public class AgentsAvailableEvent<Integer> implements Event {


    /* private Future<Boolean> isAgentsAvailable;*/
    private final Object lock = new Object();
    private final List<String> agentsNumbers;
    private int time;
    private String Monneypenny;
    private List<String> agentsName;


    private int duration;


    public AgentsAvailableEvent(List<String> agentsNumbers) {

        this.agentsNumbers = agentsNumbers;

    }


    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<String> getAgentsNumbers() {
        return agentsNumbers;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getMonneypenny() {
        return Monneypenny;
    }

    public void setMonneypenny(String monneypenny) {
        Monneypenny = monneypenny;
    }

    public List<String> getAgentsName() {
        return agentsName;
    }

    public void setAgentsName(List<String> agentsName) {
        this.agentsName = agentsName;
    }


}


