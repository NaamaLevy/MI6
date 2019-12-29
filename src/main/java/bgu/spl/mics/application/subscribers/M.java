package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.Future;
import bgu.spl.mics.MessageBroker;
import bgu.spl.mics.MessageBrokerImpl;
import bgu.spl.mics.Subscriber;
import bgu.spl.mics.application.messages.*;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Report;

/**
 * M handles ReadyEvent - fills a report and sends agents to mission.
 * <p>
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class M extends Subscriber {

    private int time;
    MessageBroker instanceOfMB = MessageBrokerImpl.getInstance();

    public M(String name) {
        super(name);
        this.time = -1;

    }

    @Override
    protected void initialize() {


        //subscribe M to terminate BroadCast
        subscribeBroadcast(TerminateBroadCast.class, (TerminateBroadCast terBC) -> terminate());
        //subscribe M to Tick BroadCast
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> time = tick.getTick());
        //subscribe M to MissionReceivedEvent
        subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEvent missionReceivedEvent) -> {
            int processTick = time;
            boolean completed = false;
            // checks if agents are available (isAgentsAvailableFuture will hold the answer)
            AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent(missionReceivedEvent.getAgentsNumbers(), missionReceivedEvent.getDuration());
            Future<Boolean> isAgentsAvailableFuture = instanceOfMB.sendEvent(agentsAvailableEvent); //future takes the returned value of agentsAvailableEvent
            if (isAgentsAvailableFuture != null) {
                boolean isAgentAvailable = isAgentsAvailableFuture.get();
                if (isAgentAvailable) {
                    GadgetAvailableEvent gadgetAvailableEvent = (new GadgetAvailableEvent<Boolean>(missionReceivedEvent.getGadget()));
                    Future<Integer> isGadgetAvailableFuture = instanceOfMB.sendEvent(gadgetAvailableEvent); // future takes the returned value of gadgetAvailableEvent
                    if (isGadgetAvailableFuture != null) {
                        int isGadgetAvailable = isGadgetAvailableFuture.get(); //takes the returned value of the future (in int, -1 represents false, true otherwise
                        if (isGadgetAvailable > 0) { //default value is -1, if gadget is available time is set to current time (so it'll be positive)
                            if (missionReceivedEvent.getExpiredTime() > processTick && time != -1) {
                                agentsAvailableEvent.setShouldSendAgents(true); // set shouldSendAgents to true, so Monneypenny will send the agent(s)
                                //builds the report
                                missionReceivedEvent.setM(this.getName());
                                missionReceivedEvent.setMoneypenny(agentsAvailableEvent.getMonneypenny());
                                Report report = new Report();
                                report.setMissionName(missionReceivedEvent.getMissionName());
                                report.setAgentsNames(missionReceivedEvent.getAgentsNumbers());
                                report.setAgentsSerialNumbers(agentsAvailableEvent.getAgentsName());
                                report.setGadgetName(missionReceivedEvent.getGadget());
                                report.setM(Integer.parseInt(this.getName()));
                                report.setMoneypenny(Integer.parseInt(agentsAvailableEvent.getMonneypenny()));
                                report.setTimeIssued(missionReceivedEvent.getTimeIssued());
                                report.setQTime(gadgetAvailableEvent.getTime());
                                report.setTimeCreated(time);
                                Diary.getInstance().addReport(report); //adds the report to the diary
                            }
                        }
                    }
                }
            }
            agentsAvailableEvent.setShouldSendAgents(false); // set shouldSendAgents to true, so Monneypenny will release the agent(s)
            //TODO - 1. we shouldnt has changed addReport (function mustn't get changed)
            //       2. we are allowed to change int total to atomicInteger (and it suppoded to be good for a reason. through whatsapp.
            //       3. we need the ability to increment WITHOUT addReport since increment happen regardless of the mission success


            }
        );
    }


}
