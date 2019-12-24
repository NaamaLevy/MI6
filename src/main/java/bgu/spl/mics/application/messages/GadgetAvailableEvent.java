package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

public class GadgetAvailableEvent<Boolean> implements Event {

    private Boolean isGadgetAvailable;

    public GadgetAvailableEvent(Boolean isGadgetAvailable) {
        this.isGadgetAvailable = isGadgetAvailable;
    }

    public Boolean getIsGadgetAvailable() {
        return isGadgetAvailable;
    }


}
