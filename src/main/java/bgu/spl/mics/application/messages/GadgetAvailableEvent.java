package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

public class GadgetAvailableEvent<Integer> implements Event {

    private Boolean isGadgetAvailable;
    private int time;
    private String gadgetName;

    public GadgetAvailableEvent(String gadgetName) {
        this.isGadgetAvailable = null;
        this.gadgetName = gadgetName;
        time = -1;
    }

    public Boolean getIsGadgetAvailable() {
        return isGadgetAvailable;
    }

    public String getGadgetName() { return gadgetName; }

    public int getTime() { return time; }

    public void setTime(int time) { this.time = time; }
}
