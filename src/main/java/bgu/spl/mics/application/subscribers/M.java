package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

    private int time;
    MessageBroker instanceOfMB = MessageBrokerImpl.getInstance();

    public M(String name) {
        super(name);
        this.time = -1;

    }

    @Override
    protected void initialize() {


        //subscribe M to terminate BroadCast
        subscribeBroadcast(TerminateBroadCast.class, (TerminateBroadCast terBC) -> terminate());
        //subscribe M to Tick BroadCast
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> time = tick.getTick());
        //subscribe M to MissionReceivedEvent
        subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEvent missionReceivedEvent) -> {
            int processTick = time;
            boolean completed = false;
		// checks if agents are available (isAgentsAvailableFuture will hold the answer)
            Future<Boolean> isAgentsAvailableFuture = instanceOfMB.sendEvent(new AgentsAvailableEvent<Boolean>(missionReceivedEvent.getAgentNumber()));
			if (isAgentsAvailableFuture != null) {
				boolean isAgentAvailable = isAgentsAvailableFuture.get();
				if(isAgentAvailable){
					Future<Boolean> isGadgetAvailableFuture = instanceOfMB.sendEvent((new GadgetAvailableEvent<Boolean>(missionReceivedEvent.getGadget())));
					if(isGadgetAvailableFuture != null){
						boolean isGadgetAvailable = isGadgetAvailableFuture.get();
						if()
							//TODO continue...

					}
				}






        };


    }

}
