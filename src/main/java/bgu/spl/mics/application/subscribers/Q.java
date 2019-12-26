package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Callback;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.messages.GadgetAvailableEvent;
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
		subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> time = tick.getTick());
		subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent gadgetAvailableEvent )->{
			String askedGadget = gadgetAvailableEvent.getGadgetName(); //takes the value of the needed gadget
			gadgetAvailableEvent.setTime(time);
			complete(gadgetAvailableEvent, Inventory.getInstance().getItem(askedGadget)); // return, using complete, the availability of askedGadget



		});
	}

}
