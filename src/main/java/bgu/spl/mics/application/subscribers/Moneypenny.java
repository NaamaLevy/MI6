package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.Squad;

import java.util.List;

/**
 * Only this type of Subscriber can access the squad.
 * Three are several Moneypenny-instances - each of them holds a unique serial number that will later be printed on the report.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Moneypenny extends Subscriber {
	int time;
	Squad squad;
	boolean agentAvailableHandler;
	private List<String> squadNumbers;

	public Moneypenny(String name,List<String> squadNumbers,  boolean isEven) {
		super(name);
		agentAvailableHandler = isEven;
		squad = Squad.getInstance();
	}

	@Override
	protected void initialize() {
		//subscribe MP to terminate BroadCast
		subscribeBroadcast(MTerminateBroadCast.class, (MTerminateBroadCast terBC) -> {
			squad.releaseAgents(squadNumbers);
			terminate();
		});
		//subscribe MP to Tick BroadCast
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> time = tick.getTick());


		//subscribe MP to MissionReceivedEvent
		if(agentAvailableHandler){
		subscribeEvent(AgentsAvailableEvent.class, (AgentsAvailableEvent agentsAvailableEvent) -> {
					agentsAvailableEvent.setMonneypenny(getName()); //update MP id that handle the event for the report
			int ans = 0;
			if (squad.getAgents(agentsAvailableEvent.getAgentsNumbers()))
						ans = 1;
			else
				ans = -1;
					complete(agentsAvailableEvent, ans); // return, using complete, the availability of askedAgents
					System.out.println("MoneyPenny" +this.getName()+": Agents are available for the mission   " + " time:" +		time);
					agentsAvailableEvent.setAgentsName(squad.getAgentsNames(agentsAvailableEvent.getAgentsNumbers())); //set agentsName field in the event (for the report)
			});

		}
		else {
			subscribeEvent(AgentsSendToMissionEvent.class, (AgentsSendToMissionEvent agentsSendToMissionEvent) -> {
						squad.sendAgents(agentsSendToMissionEvent.getAgentsNumbers(), agentsSendToMissionEvent.getDuration()); //sends the agents to the mission
						complete(agentsSendToMissionEvent, null);
					});
			subscribeEvent(ReleaseAgentsEvent.class, (ReleaseAgentsEvent releaseAgentsEvent) -> {
					squad.releaseAgents(releaseAgentsEvent.getAgentsNumbers()); //release the agents if mission was cancelled
				complete(releaseAgentsEvent, null);
			System.out.println("MoneyPenny"+this.getName()+": Agents has been released from the mission" + " time:" +		time);
			});

			}
		}
	}