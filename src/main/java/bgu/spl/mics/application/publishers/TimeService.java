package bgu.spl.mics.application.publishers;

import bgu.spl.mics.Publisher;
import bgu.spl.mics.application.messages.TerminateBroadCast;
import bgu.spl.mics.application.messages.TickBroadcast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this Publisher.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other subscribers about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for.
 * 
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends Publisher {
	int timeDuration;

	public TimeService(int timeDuration) {
		super("TimeService");
		this.timeDuration = timeDuration;
	}

	@Override
	protected void initialize() {

		/*sends:
		1. TickBroadcast (to APIService)
		2. TerminateBroadcast (to everyone)
		 * */

//		Timer timer = new Timer();
//		TimerTask task = new TimerTask() {
//			@Override
//			public void run() {
//				if (timeDuration > currTick) {
//					sendBroadcast(new TickBroadcast(currTick));
//					currTick++;
//				} else {
//					sendBroadcast(new TerminateBroadCast());
//					timer.cancel();
//				}
//			}
//		};
//		timer.scheduleAtFixedRate(task,100,speed);

	}

	@Override
	public void run() {
		// TODO Implement this
	}


}
