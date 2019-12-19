package bgu.spl.mics.application.subscribers;
import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.passiveObjects.MissionInfo;
import bgu.spl.mics.Subscriber;

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


	public Intelligence(MissionInfo[] sourceMissions) {
		super("intelligence");
		missions = sourceMissions;
	}
	public void setId(int id){this.id = id;}


	@Override
	protected void initialize() {
		// TODO Implement this
	}
}
