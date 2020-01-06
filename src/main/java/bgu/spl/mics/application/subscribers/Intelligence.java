package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.*;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.MissionInfo;

/**
 * A Publisher\Subscriber.
 * Holds a list of Info objects and sends them
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Intelligence extends Subscriber {

	private MissionInfo[] missions;
	int id;
	int time;
	MessageBroker MB;


	public Intelligence(MissionInfo[] sourceMissions) {
		super("intelligence");
		missions = sourceMissions;
		this.time = -1;
		MB = MessageBrokerImpl.getInstance();
	}
	public void setId(int id){this.id = id;}


	@Override
	protected void initialize() {
		//subscribe to terminate BroadCast
		subscribeBroadcast(TerminateBroadCast.class, (TerminateBroadCast terBC) -> {
					terminate();

		} );
		//subscribe to Tick BroadCast
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> {
			for (MissionInfo mission: missions){
				if (mission.getTimeIssued()==tick.getTick()){
					MissionReceivedEvent missionReceivedEvent = new MissionReceivedEvent(mission.getMissionName(),mission.getSerialAgentsNumbers(),mission.getGadget(), mission.getTimeExpired(), mission.getDuration(), mission.getTimeIssued());
					getSimplePublisher().sendEvent(missionReceivedEvent);
				}
			}
		});
		//subscribe M to MissionReceivedEvent

	}

	private int getId() {
		return id;
	}

	public MissionInfo[] getMissions() {
		return missions;
	}
}
