package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
import bgu.spl.mics.application.messages.TerminateBroadCast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;

/**
 * Q is the only Subscriber\Publisher that has access to the {@link bgu.spl.mics.application.passiveObjects.Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class Q extends Subscriber {

	private int time;

	public Q() {
		super("instanceOfQ");
		this.time = -1;



	}

	@Override
	protected void initialize() {
		//subscribe Q to terminate BroadCast
		subscribeBroadcast(TerminateBroadCast.class, (TerminateBroadCast terBC) -> {
			terminate();

		} );
		//subscribe Q to Tick BroadCast
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> time = tick.getTick());
		//subscribe Q to GadgetAvailableEvent BroadCast
		subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent gadgetAvailableEvent )->{
			String askedGadget = gadgetAvailableEvent.getGadgetName(); //takes the value of the needed gadget
			if(Inventory.getInstance().getItem(askedGadget)) gadgetAvailableEvent.setTime(time); // if gadget is available, set the event's time field to current time
			complete(gadgetAvailableEvent, gadgetAvailableEvent.getTime()); // return, using complete, the availability of askedGadget
		});
	}

}
