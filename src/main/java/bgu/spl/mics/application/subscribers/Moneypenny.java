package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.MissionReceivedEvent;
import bgu.spl.mics.application.messages.TerminateBroadCast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	int time;
	Squad squad = Squad.getInstance();
	boolean agentAvailableHandler;

	public Moneypenny(String name, boolean isEven) {
		super(name);
		this.agentAvailableHandler = isEven;
	}

	@Override
	protected void initialize() {
		//subscribe MP to terminate BroadCast
		subscribeBroadcast(TerminateBroadCast.class, (TerminateBroadCast terBC) -> terminate());
		//subscribe MP to Tick BroadCast
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> time = tick.getTick());
		//subscribe MP to MissionReceivedEvent
		subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent agentsAvailableEvent) -> {
			agentsAvailableEvent.setMonneypenny(getName());
			complete(agentsAvailableEvent, squad.getAgents(agentsAvailableEvent.getAgentsNumbers())); // return, using complete, the availability of askedAgents
			System.out.println("MoneyPenny: Agents are available for the mission" + " time:" + time );
			boolean b = agentsAvailableEvent.getShouldSendAgents();
			if(b){ //waits for M to set shouldSendAgents to true
				agentsAvailableEvent.setAgentsName(squad.getAgentsNames(agentsAvailableEvent.getAgentsNumbers())); //set agentsName field in the event (for the report)
				squad.sendAgents(agentsAvailableEvent.getAgentsNumbers(), agentsAvailableEvent.getDuration()); //sends the agents to the mission
				System.out.println("MoneyPenny: Agents has been sent to the mission"  + time );
			}
			else squad.releaseAgents(agentsAvailableEvent.getAgentsNumbers()); //release the agents if mission was cancelled
					System.out.println("MoneyPenny: Agents has been released from the mission"  + " time:" + time );

		}

		);
	}
}
















