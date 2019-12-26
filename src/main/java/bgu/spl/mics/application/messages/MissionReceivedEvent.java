package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class MissionReceivedEvent implements Event {
    private String missionName;
    private List<String> agentNumber;
    private String gadget;
    private int time;


    public MissionReceivedEvent(String missionName, List<String> agentNumber, String gadget) {
        this.missionName = missionName;
        this.agentNumber = agentNumber;
        this.gadget = gadget;
    }

    public String getMissionName() { return missionName; }

    public List<String> getAgentNumber() { return agentNumber; }

    public String getGadget() { return gadget; }
    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }

}
