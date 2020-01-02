package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import java.util.List;

public class AgentsSendToMissionEvent implements Event {

    private List<String> agentsNumbers;
    private  int duration;

    public AgentsSendToMissionEvent(List<String> agentsNumbers, int duration) {
        this.agentsNumbers = agentsNumbers;
        this.duration = duration;
    }

    public List<String> getAgentsNumbers() {
        return agentsNumbers;
    }

    public void setAgentsNumbers(List<String> list) {
        this.agentsNumbers = agentsNumbers;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}