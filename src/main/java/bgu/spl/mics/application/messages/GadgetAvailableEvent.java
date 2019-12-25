package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

public class GadgetAvailableEvent<Boolean> implements Event {

    private Boolean isGadgetAvailable;



    private String gadgetName;

    public GadgetAvailableEvent(String gadgetName) {
        this.isGadgetAvailable = null;
        this.gadgetName = gadgetName;
    }

    public Boolean getIsGadgetAvailable() {
        return isGadgetAvailable;
    }

    public String getGadgetName() { return gadgetName; }
}
