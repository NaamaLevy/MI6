package bgu.spl.mics.application.passiveObjects;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * Passive data-object representing a information about an agent in MI6.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You may add ONLY private fields and methods to this class.
 */
public class Squad {

	public Map<String, Agent> agents;
	private Semaphore semaphore;


	// creates a singleton
	private static class SingletonHolder {
		private static Squad squad = new Squad();

	}
	private Squad (){
		semaphore = new Semaphore (10, true);
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
		synchronized (this){
			this.agents = new HashMap<String, Agent>();
			for(Agent agent : agents) this.agents.put(agent.getSerialNumber(), agent);
		}
	}


	/**
	 * Releases agents.
	 */
	public void releaseAgents(List<String> serials) {

		if (!(serials == null)) {
			if (!serials.isEmpty()) {
				for (String serial : serials) {
					if (agents.containsKey(serial)) {

						agents.get(serial).release();
					}
				}
			}
		}
		semaphore.release();
	}

	/**
	 * simulates executing a mission by calling sleep.
	 * @param time   milliseconds to sleep
	 */
	public void sendAgents(List<String> serials, int time){
		Thread t = new Thread("Sleeper");
		try{
			Thread.sleep(time*100);
		} catch (Exception e){}
		releaseAgents(serials);
	}

	/**
	 * acquires an agent, i.e. holds the agent until the caller is done with it
	 * @param serials   the serial numbers of the agents
	 * @return ‘false’ if an agent of serialNumber ‘serial’ is missing, and ‘true’ otherwise
	 */
	public boolean getAgents(List<String> serials) throws InterruptedException {
		for (String serial:serials) {
			if (agents.get(serial) == null) {
				return false;
			}
		}
		semaphore.acquire();
		for (String serial:serials) {
			Agent agentToAcquire = agents.get(serial);
			while (!agentToAcquire.isAvailable()) {
				try {
					semaphore.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}
			agentToAcquire.acquire();
		}
		semaphore.release();
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
			if(agents.get(serial) != null){
				String agentName = agents.get(serial).getName();
				agentsNames.add(agentName);
			}
			else return null;
		}
		return agentsNames;
	}

}

