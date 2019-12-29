package bgu.spl.mics.application.passiveObjects;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class. 
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	private Map<String, Agent> agents;

	// creates a singleton
	private static class SingletonHolder {
		private static Squad squad = new Squad();
	}
	/**
	 * Retrieves the single instance of this class.
	 */
	public static Squad getInstance() { return SingletonHolder.squad; }

	/**
	 * Initializes the squad. This method adds all the agents to the squad.
	 * <p>
	 * @param agents 	Data structure containing all data necessary for initialization
	 * 						of the squad.
	 */
	public void load (Agent[] agents) {
		this.agents = new HashMap<String, Agent>();
		for(Agent agent : agents) this.agents.put(agent.getSerialNumber(), agent);
	}


	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials) {
		for (String serial:serials)
			if (agents.get(serial) != null) {
				agents.get(serial).release();
				agents.get(serial).notify();
			}
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		Thread t = new Thread("Sleeper");
		try{
			Thread.sleep(time);
		} catch (Exception e){}
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials){
		for (String serial:serials) {
			if (agents.get(serial) == null)
				return false;
		}
		for (String serial:serials){
			Agent agentToAcquire = agents.get(serial);
			synchronized (agentToAcquire){
				while (!agentToAcquire.isAvailable()) {
					try{
						agentToAcquire.wait();
					}catch (InterruptedException e){
						e.printStackTrace();
					}
				}
				agentToAcquire.acquire();
			}
		}
		return true;
	}


	/**
	 * gets the agents names
	 * @param serials the serial numbers of the agents
	 * @return a list of the names of the agents with the specified serials.
	 */
	public List<String> getAgentsNames(List<String> serials){
		List<String> agentsNames = new ArrayList<>();
		for (String serial : serials) {
			String agentName = agents.get(serial).getName();
			agentsNames.add(agentName);
		}
		return agentsNames;
	}

	public Map<String, Agent> getAgentsMap(){
		return agents;
	}
}
