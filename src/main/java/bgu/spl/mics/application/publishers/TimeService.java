package bgu.spl.mics.application.publishers;

import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
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
	int currentTime;
	MessageBroker MB;

	public TimeService(int timeDuration) {
		super("TimeService");
		this.timeDuration = timeDuration;
		currentTime = 1;
		MB = MessageBrokerImpl.getInstance();
	}

	@Override
	protected void initialize() {}

	@Override
	public void run() {
		{
			Timer timer = new Timer();
			TimerTask task = new TimerTask() {
				@Override
				public void run() {
					if (timeDuration >= currentTime) {
						getSimplePublisher().sendBroadcast(new TickBroadcast(currentTime));
						currentTime++;
					} else {
						getSimplePublisher().sendBroadcast(new TerminateBroadCast());
						timer.cancel();
						timer.purge();
					}
				}
			};
			timer.scheduleAtFixedRate(task,100,100);
		}

	}
}
