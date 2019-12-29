package bgu.spl.mics.application.messages;

import bgu.spl.mics.Event;

import java.util.List;

public class MissionReceivedEvent implements Event {
    private final String missionName;
    private final List<String> agentsNumbers;
    private final String gadget;
    private int time;
    private final int expiredTime;
    private final int duration;
    private int timeIssued;
    private String M;
    private String Moneypenny;


    public MissionReceivedEvent(String missionName, List<String> agentNumber, String gadget, int expiredTime, int duration, int timeIssued) {
        this.missionName = missionName;
        this.agentsNumbers = agentNumber;
        this.gadget = gadget;
        this.expiredTime = expiredTime;
        this.duration = duration;
        this.timeIssued = timeIssued;

    }

    public String getMissionName() { return missionName; }

    public List<String> getAgentsNumbers() { return agentsNumbers; }

    public String getGadget() { return gadget; }
    public int getTime() { return time; }
    public void setTime(int time) { this.time = time; }
    public int getExpiredTime() { return expiredTime; }
    public int getDuration() { return duration; }
    public String getM() { return M; }
    public void setM(String m) { M = m; }
    public String getMoneypenny() { return Moneypenny; }
    public void setMoneypenny(String moneypenny) { Moneypenny = moneypenny; }
    public int getTimeIssued() { return timeIssued; }
    public void setTimeIssued(int timeIssued) { this.timeIssued = timeIssued; }
}
