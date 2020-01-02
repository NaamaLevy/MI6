package bgu.spl.mics.application.subscribers;

import bgu.spl.mics.*;
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
    MessageBroker instanceOfMB;
    private Diary diary;
    public M(String name) {
        super(name);
        instanceOfMB = MessageBrokerImpl.getInstance();
        diary = Diary.getInstance();

    }

    @Override
    protected void initialize() {

        //subscribe M to terminate BroadCast
        subscribeBroadcast(TerminateBroadCast.class, (TerminateBroadCast terBC) -> terminate());
        //subscribe M to Tick BroadCast
        subscribeBroadcast(TickBroadcast.class, (TickBroadcast tick) -> time = tick.getTick());
        //subscribe M to MissionReceivedEvent
        Callback<MissionReceivedEvent> missionReceivedEventCallback = (MissionReceivedEvent meE) ->{
            diary.incrementTotal();
            AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent(meE.getAgentsNumbers());
            Future<Integer> isAgentsAvailableFuture = getSimplePublisher().sendEvent(agentsAvailableEvent);
            int agentsAvailable = 0;
            if (isAgentsAvailableFuture != null)
                agentsAvailable = isAgentsAvailableFuture.get();
            int gadgetAvailable = 0;
            if (isAgentsAvailableFuture != null && agentsAvailable == 1){
                GadgetAvailableEvent gadgetAvailableEvent = (new GadgetAvailableEvent<Boolean>(meE.getGadget()));
                Future<Integer> isGadgetAvailableFuture = getSimplePublisher().sendEvent(gadgetAvailableEvent);

                if (isGadgetAvailableFuture!=null)
                    agentsAvailable = isGadgetAvailableFuture.get();
            }
            if ((agentsAvailable==1) && (gadgetAvailable > 0) && (agentsAvailable <= meE.getExpiredTime())){
                AgentsSendToMissionEvent agentsSendToMissionEvent = new AgentsSendToMissionEvent(meE.getAgentsNumbers(),meE.getDuration() );
                Future<Integer> isAgentsAvailableFuture = getSimplePublisher().sendEvent(agentsAvailableEvent);
            }


        };

        subscribeEvent(MissionReceivedEvent.class, (MissionReceivedEvent missionReceivedEvent) -> {
            Diary.getInstance().incrementTotal(); // performs total++
            int processTick = time;

            // checks if agents are available (isAgentsAvailableFuture will hold the answer)
            AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent(missionReceivedEvent.getAgentsNumbers(), missionReceivedEvent.getDuration());
            System.out.println("M: Event accepted" + " time:" + time );
            Future<Integer> isAgentsAvailableFuture = getSimplePublisher().sendEvent(agentsAvailableEvent); //future takes the returned value of agentsAvailableEvent
            System.out.println("M: future" + " time:" + time );
            if (isAgentsAvailableFuture.isResolved()) {
                int isAgentAvailable = isAgentsAvailableFuture.get();
                if (isAgentAvailable>0) {
                    System.out.println("M: We got the agents" + " time:" + time );
                    GadgetAvailableEvent gadgetAvailableEvent = (new GadgetAvailableEvent<Boolean>(missionReceivedEvent.getGadget()));
                    Future<Integer> isGadgetAvailableFuture = getSimplePublisher().sendEvent(gadgetAvailableEvent); // future takes the returned value of gadgetAvailableEvent
                    if (isGadgetAvailableFuture != null) {
                        int isGadgetAvailable = isGadgetAvailableFuture.get(); //takes the returned value of the future (in int, -1 represents false, true otherwise
                        if (isGadgetAvailable > 0) { //default value is -1, if gadget is available time is set to current time (so it'll be positive)
                            System.out.println("M: We got the gadget " + " time:" + time );
                            if (missionReceivedEvent.getExpiredTime() > processTick && time != -1) {
                                agentsAvailableEvent.setShouldSendAgents(true); // set shouldSendAgents to true, so Monneypenny will send the agent(s)
                                //builds the report
                                missionReceivedEvent.setM(this.getName());
                                missionReceivedEvent.setMoneypenny(agentsAvailableEvent.getMonneypenny());
                                Report report = new Report();
                                report.setTimeCreated(time);
                                report.setMissionName(missionReceivedEvent.getMissionName());
                                report.setAgentsNames(missionReceivedEvent.getAgentsNumbers());
                                report.setAgentsSerialNumbers(agentsAvailableEvent.getAgentsName());
                                report.setGadgetName(missionReceivedEvent.getGadget());
                                System.out.println(this.getName());
                                System.out.println(this.getName());
                                System.out.println(this.getName());
                                int mName = (int) Integer.parseInt(this.getName());
                                report.setM(mName);
                                int mpName = (int)Integer.parseInt(agentsAvailableEvent.getMonneypenny());
                                report.setMoneypenny(mpName);
                                report.setTimeIssued(missionReceivedEvent.getTimeIssued());
                                report.setQTime(gadgetAvailableEvent.getTime());
                                Diary.getInstance().addReport(report); //adds the report to the diary
                            }
                        }
                    }
                }
            }
            agentsAvailableEvent.setShouldSendAgents(false); // set shouldSendAgents to true, so Monneypenny will release the agent(s)




            }
        );
    }


}
