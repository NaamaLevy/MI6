package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;
import java.util.List;

public class ReleaseAgentsEvent implements Event {

    private List<String> agentsNumbers;

    public ReleaseAgentsEvent(List<String> agentsNumbers)
    {
        this.agentsNumbers = agentsNumbers;
    }


    public void setAgentsNumbers(List<String> agentsNumbers) {
        this.agentsNumbers = agentsNumbers;
    }
    public List<String> getAgentsNumbers() {
        return agentsNumbers;
    }

}
