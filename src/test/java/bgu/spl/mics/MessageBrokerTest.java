package bgu.spl.mics;

import bgu.spl.mics.application.messages.AgentsAvailableEvent;
import bgu.spl.mics.application.subscribers.M;
import bgu.spl.mics.application.subscribers.Moneypenny;
import bgu.spl.mics.example.ExampleManager;
import bgu.spl.mics.example.messages.ExampleBroadcast;
import bgu.spl.mics.example.messages.ExampleEvent;
import bgu.spl.mics.example.publishers.ExampleMessageSender;
import bgu.spl.mics.example.subscribers.ExampleBroadcastSubscriber;
import bgu.spl.mics.example.subscribers.ExampleEventHandlerSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.jupiter.api.Assertions.*;

public class MessageBrokerTest {
    private String s;
    private String[] args ={"1"};
    private Message message = new ExampleEvent("name for test");
    private MessageBroker messageBroker = MessageBrokerImpl.getInstance();
    private Event<String> exampleEvent1 = new ExampleEvent<>("event for test");
    private Event<String> exampleEvent2 = new ExampleEvent<>("");
    private ExampleBroadcast exampleBroadcast = new ExampleBroadcast("broadcast for test");
    private ExampleManager exampleManager = new ExampleManager();
    private ExampleMessageSender exampleMessageSender = new ExampleMessageSender("message sender for test" , args);
    private ExampleEventHandlerSubscriber exampleEventHandlerSubscriber = new ExampleEventHandlerSubscriber("name for test" , args);
    private ExampleBroadcastSubscriber exampleBroadcastSubscriber = new ExampleBroadcastSubscriber("name for test" , args);
    private ConcurrentHashMap <Class<? extends Message>, Queue<Subscriber>>MessagesQueuesMap; //holds messages and subscribed subscribers queue
    private ConcurrentHashMap  <Subscriber, BlockingQueue<Message>> SubscribersQueueMap; // holds subscribers and their messages queue
    private ConcurrentHashMap<Event, Future> eventFutureMap; // holds Events and their Future
    private LinkedBlockingQueue<Message> messagesBlockingQueue = new LinkedBlockingQueue<Message>();
    private Queue<Subscriber> subscriberQueue = new LinkedList<Subscriber>();
    private Future future = new Future<String>();


    @BeforeEach
    public void setUp() throws InterruptedException {
        subscriberQueue.add(exampleEventHandlerSubscriber);
        MessagesQueuesMap.put( exampleEvent1.getClass() , subscriberQueue );
        messagesBlockingQueue.put(exampleEvent1);
        SubscribersQueueMap.put(exampleEventHandlerSubscriber , messagesBlockingQueue);
        eventFutureMap.put(exampleEvent1 , future);



    }

    @Test
    public void test_subscribeMessage(){
        assertTrue(MessagesQueuesMap.size() == 1);
        assertTrue(messagesBlockingQueue.size() == 1);
        messageBroker.subscribeEvent(exampleEvent2.getClass().asSubclass(exampleEvent2.getClass()) , exampleEventHandlerSubscriber);
        assertTrue(MessagesQueuesMap.size() == 1);
        assertTrue(messagesBlockingQueue.size() == 2);
    }

    @Test
    public void test_complete(){
            assertFalse(exampleEvent2.toString() == "done");
            messageBroker.complete(exampleEvent1, "done");
            assertTrue(exampleEvent2.toString() == "done");
    }

    @Test
    public void test_sendBroadcast(){
        assertTrue(messagesBlockingQueue.size() == 1);
        messageBroker.sendBroadcast(exampleBroadcast);
        assertTrue(messagesBlockingQueue.size() == 2);
    }

    @Test
    public void test_sendEvent(){
        future = messageBroker.sendEvent(exampleEvent2);
        assertEquals(future , null);
        assertEquals(eventFutureMap.size(), 1);
        assertTrue(subscriberQueue.contains(exampleEventHandlerSubscriber));
        messageBroker.sendEvent(exampleEvent1);
        assertFalse(subscriberQueue.contains(exampleEventHandlerSubscriber));
        assertEquals(eventFutureMap.size(), 1);
    }

    @Test
    public void test_register(){
        subscriberQueue.clear();
        SubscribersQueueMap.clear();
        assertEquals(subscriberQueue.size(),0);
        assertEquals(SubscribersQueueMap.size(),0);
        messageBroker.register(exampleBroadcastSubscriber);
        assertEquals(subscriberQueue.size(),1);
        assertEquals(SubscribersQueueMap.size(),1);
    }

    @Test
    public void test_unregister(){
        subscriberQueue.clear();
        SubscribersQueueMap.clear();
        messageBroker.register(exampleBroadcastSubscriber);
        messageBroker.unregister(exampleBroadcastSubscriber);
        assertEquals(subscriberQueue.size(),0);
        assertEquals(SubscribersQueueMap.size(),0);
    }
}
