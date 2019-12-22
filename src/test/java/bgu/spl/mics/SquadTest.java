package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Agent;
import bgu.spl.mics.application.passiveObjects.Squad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class SquadTest {
    private Squad squad = Squad.getInstance();
    private Map<String, Agent> agents;
    private Agent[] agentsArray;
    List<String> serials = new ArrayList<>();
    private long timeOut = 3;
    private TimeUnit unit = TimeUnit.SECONDS;

    @BeforeEach
    public void setUp() {
        //creating test agents
        Agent agent1 = new Agent();
        agent1.setName("jb1");
        agent1.setSerialNumber("001");
        Agent agent2 = new Agent();
        agent2.setName("jb2");
        agent2.setSerialNumber("002");
        Agent agent3 = new Agent();
        agent3.setName("jb3");
        agent3.setSerialNumber("003");
        Agent agent4 = new Agent();
        agent4.setName("jb4");
        agent4.setSerialNumber("004");

        //creating agents map
        Map<String, Agent> agents = new HashMap<String, Agent>();
        agents.put(agent1.getSerialNumber(), agent1);
        agents.put(agent2.getSerialNumber(), agent2);
        agents.put(agent3.getSerialNumber(), agent3);
        agents.put(agent4.getSerialNumber(), agent4);

        //creating agents array
        agentsArray[0] = agent1;
        agentsArray[1] = agent2;
        agentsArray[2] = agent3;
        agentsArray[3] = agent4;
    }

    @Test
    public void test_Load() {
        squad.load(agentsArray);
        assertFalse(squad.getAgentsMap().isEmpty());
        assertEquals(squad.getAgentsMap().get("001"), agentsArray[0]);
        assertEquals(squad.getAgentsMap().get("002"), agentsArray[1]);
        assertEquals(squad.getAgentsMap().get("003"), agentsArray[2]);
        assertEquals(squad.getAgentsMap().get("004"), agentsArray[3]);
    }

    @Test
    public void test_GetAgents() {

        serials.add("005");
        assertFalse(squad.getAgents(serials));
        serials.clear();
        serials.add("001");
        serials.add("003");
        assertTrue(squad.getAgents(serials));
        assertFalse(squad.getAgentsMap().get("001").isAvailable());
        assertFalse(squad.getAgentsMap().get("003").isAvailable());
        serials.clear();
    }

    public void test_releaseAgents() {
        serials.clear();
        serials.add("001");
        squad.releaseAgents(serials);
        assertTrue(squad.getAgentsMap().get("001").isAvailable());
        serials.clear();

    }

    public void test_sendAgents(){
        serials.add("001");
        serials.add("003");
        squad.sendAgents(serials, 1000);
        Thread t = new Thread("stam");
        try{
            Thread.sleep(1000);
        } catch (Exception e){
            System.out.println("blabla");
        }
        assertTrue(squad.getAgentsMap().get("001").isAvailable());
        assertTrue(squad.getAgentsMap().get("003").isAvailable());
        serials.clear();

    }

    public void test_getAgentsName(){
        serials.add("002");
        String output = "jb2";
        assertTrue((squad.getAgentsNames(serials).contains(output)));
    }








}
