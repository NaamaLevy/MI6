package bgu.spl.mics;
import java.util.LinkedList;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * The {@link MessageBrokerImpl class is the implementation of the MessageBroker interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBrokerImpl implements MessageBroker {

	/**
	 * Retrieves the single instance of this class.
	 */

	//***  fields  ***
	private ConcurrentHashMap <Class<? extends Message>, Queue<Subscriber>>MessageAndItsSubscribersQueueMap; //holds messages and subscribed subscribers queue
	private ConcurrentHashMap  <Subscriber, BlockingQueue<Message>> SubscriberAndItsMessagesQueueMap; // holds subscribers and their messages queue
	private ConcurrentHashMap<Event, Future> eventFutureMap; // holds Events and their Future
	private final Object lock;





	//***  constructor (singleton, thread-safe)  ***
	private static class SingletonHolder {
		private static MessageBrokerImpl messageBroker = new MessageBrokerImpl();
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static MessageBroker getInstance() {
		return SingletonHolder.messageBroker;
	}

	private MessageBrokerImpl(){  // initializes fields
		SubscriberAndItsMessagesQueueMap = new ConcurrentHashMap<>();
		MessageAndItsSubscribersQueueMap = new ConcurrentHashMap<>();
		eventFutureMap = new ConcurrentHashMap<>();
		lock = new Object();


	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, Subscriber s) {
		subscribeMessage(type,s);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, Subscriber s) {
		subscribeMessage(type,s);
	}

	private void subscribeMessage(Class<? extends Message> type, Subscriber s){
		//if type is not in the MessagesQueueMap, create new queue for this type
		synchronized (MessageAndItsSubscribersQueueMap){
			MessageAndItsSubscribersQueueMap.putIfAbsent(type, new LinkedList<Subscriber>());
		}
		//add Subscriber s to the queue of type messages
		synchronized (MessageAndItsSubscribersQueueMap.get(type)) {
			if (!MessageAndItsSubscribersQueueMap.get(type).contains(s))
				MessageAndItsSubscribersQueueMap.get(type).add(s);
		}
	}

	@Override
	public <T> void complete(Event<T> e, T result) {
		eventFutureMap.get(e).resolve(result);
		eventFutureMap.remove(e);
	}

	@Override
	public void sendBroadcast(Broadcast b) {
		//add b to every Subscriber subscribed to this type
		//sync for preventing other thread from unregister a subscriber from the queue while this method tries to add a broadcast to its queue.
		synchronized (lock){
			Queue<Subscriber> bSubscribers = MessageAndItsSubscribersQueueMap.get(b.getClass());
			if  (bSubscribers != null){
				for(Subscriber s : bSubscribers) {
					if (SubscriberAndItsMessagesQueueMap.get(s) != null)
						//add broadcast b to every interested Subscriber
						SubscriberAndItsMessagesQueueMap.get(s).add(b);
				}
			}
		}
	}


	@Override
	public <T> Future<T> sendEvent(Event<T> e) {
		Future<T> future = null;
		Queue<Subscriber> interestedSubscribers = MessageAndItsSubscribersQueueMap.get(e.getClass());
		//if there is no Subscribers up for the Event return null
		if (interestedSubscribers == null || interestedSubscribers.isEmpty())
			return future;
		//sync to prevent from the relevant Subscribers to unregister themselves while the method tries to catch one of them
		synchronized (interestedSubscribers){
			//assert the condition for finding relevant Subscriber
			if (interestedSubscribers == null || interestedSubscribers.isEmpty())
				return future;
			//locate the first Subscriber available for the mission and remove it from the queue
			Subscriber chosenSubscriberForE = interestedSubscribers.poll();
			//sync to prevent any action on the chosen Subscriber for the event
			synchronized (chosenSubscriberForE){
				if (chosenSubscriberForE != null){
					BlockingQueue<Message>  chosenMessagesQueue = SubscriberAndItsMessagesQueueMap.get(chosenSubscriberForE);
					if (chosenMessagesQueue != null){
						//return chosen to the event type queue
						interestedSubscribers.add(chosenSubscriberForE);
						//add e to the eventFutureMap
						future = new Future<>();
						eventFutureMap.put(e,future);
						//add e to the chosen's Massages queue
						chosenMessagesQueue.add(e);

					}
				}
			}
		}
		return future;

	}

	@Override
	public void register(Subscriber s) {
		SubscriberAndItsMessagesQueueMap.putIfAbsent(s,new LinkedBlockingQueue());
	}

	@Override
	public void unregister(Subscriber s) {
		//sync to prevent other threads send Broadcast while unregister s
		synchronized (lock){
			//complete every event assigned to s
			BlockingQueue<Message> subscriberQueue = SubscriberAndItsMessagesQueueMap.get(s);
			if (s != null && subscriberQueue != null){
				for (Message message : subscriberQueue){
					if (message instanceof Event){
						complete((Event)message, null);   //QQQ N, why do we resolve its events as null? aren't we missing events s was supposed to handle?
					}
				}
				SubscriberAndItsMessagesQueueMap.remove(s);
			}
			//unsubscribe s from every message type queue
			for ( Queue queue: MessageAndItsSubscribersQueueMap.values()){
				queue.remove(s);
			}
		}


	}

	@Override
	public Message awaitMessage(Subscriber s) throws InterruptedException {
		try{
			return SubscriberAndItsMessagesQueueMap.get(s).take();
		}catch (InterruptedException e){
			e.printStackTrace();
		}
		return null;
	}

}
