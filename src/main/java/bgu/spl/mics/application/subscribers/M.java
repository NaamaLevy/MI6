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
            System.out.println("M: Mission ---"+meE.getMissionName()+ "--- accepted." + "      time:" + time );
            diary.incrementTotal();
            Report report = new Report();
            //create a report for the mission
            report.setTimeCreated(time);
            report.setMissionName(meE.getMissionName());
            report.setAgentsNames(meE.getAgentsNumbers());
            report.setGadgetName(meE.getGadget());

            AgentsAvailableEvent agentsAvailableEvent = new AgentsAvailableEvent(meE.getAgentsNumbers());
            Future<Integer> isAgentsAvailableFuture = getSimplePublisher().sendEvent(agentsAvailableEvent);
            System.out.println("M: Agents are you up for the mission---"+ meE.getMissionName() + "---    time:" + time );
            int agentsAvailable = 0;
            if (isAgentsAvailableFuture != null)
                agentsAvailable = isAgentsAvailableFuture.get();
            int gadgetAvailable = 0;
            if (isAgentsAvailableFuture != null && agentsAvailable == 1){
                System.out.println("M: We got the agents" + "       time:" + time );
                GadgetAvailableEvent gadgetAvailableEvent = (new GadgetAvailableEvent<Integer>(meE.getGadget()));
                Future<Integer> isGadgetAvailableFuture = getSimplePublisher().sendEvent(gadgetAvailableEvent);

                if (isGadgetAvailableFuture!=null)
                    gadgetAvailable = isGadgetAvailableFuture.get();
            }
            if ((agentsAvailable==1) && (gadgetAvailable > 0) && (gadgetAvailable <= meE.getExpiredTime())){
                System.out.println("M: We got the gadget " +meE.getGadget()+ "      time:" + time );
                AgentsSendToMissionEvent agentsSendToMissionEvent = new AgentsSendToMissionEvent(meE.getAgentsNumbers(),meE.getDuration() );
                Future SendAgentsFuture = getSimplePublisher().sendEvent(agentsSendToMissionEvent);

                int mName = (int) Integer.parseInt(this.getName());
                report.setM(mName);
                int mpName = (int)Integer.parseInt(agentsAvailableEvent.getMonneypenny());
                report.setMoneypenny(mpName);
                report.setTimeIssued(meE.getTimeIssued());
                report.setQTime(meE.getTime());
                report.setAgentsSerialNumbers(agentsAvailableEvent.getAgentsName());
                diary.addReport(report); //adds the report to the diary
                System.out.println("M: Agents out for the mission ---"+ meE.getMissionName() + "---      time:" + time );
            }
            else{
                ReleaseAgentsEvent releaseAgentsEvent = new ReleaseAgentsEvent(meE.getAgentsNumbers());
                Future releaseAgents = getSimplePublisher().sendEvent(releaseAgentsEvent);
                System.out.println("M: Mission has been cancelled ---" + meE.getMissionName() + "---      time:" + time );
            }
        };

        subscribeEvent(MissionReceivedEvent.class, missionReceivedEventCallback);

    }
}
