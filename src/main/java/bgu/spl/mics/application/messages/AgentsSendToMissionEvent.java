package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;
import java.util.List;

public class AgentsSendToMissionEvent implements Event {
    private List<String> list;
    private  int duration;

    public AgentsSendToMissionEvent(List<String> list, int duration) {
        this.list = list;
        this.duration = duration;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}