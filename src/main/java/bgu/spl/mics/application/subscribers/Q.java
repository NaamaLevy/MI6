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

	public Q() {
		super("instanceOfQ");
		//

	}

	@Override
	protected void initialize() {

		//QQQshould we subscribe to time
		subscribeEvent(GadgetAvailableEvent.class, (GadgetAvailableEvent gadgetAvailableEvent )->{
			String askedGadget = gadgetAvailableEvent.getGadgetName();
			complete(gadgetAvailableEvent, Inventory.getInstance().getItem(askedGadget)); // return to "complete" the availability of askedGadget



		});
	}

}
