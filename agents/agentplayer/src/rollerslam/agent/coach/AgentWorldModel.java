package rollerslam.agent.coach;

import java.util.HashMap;
import java.util.Vector;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;

public class AgentWorldModel extends GoalBasedEnvironmentStateModel {

	public AgentGoal currentGoal;
	public boolean gameStarted = false;
	public boolean joinMessageSent = false;
	public int myID = -1;
	public PlayerTeam myTeam;
	
	//Used when players is a coach
	public HashMap<Integer, Integer> playersPosition = new HashMap<Integer, Integer>();
	public Vector<Integer> playersToSetPosition = new Vector<Integer>();
	public Vector<Integer> positionToSet = new Vector<Integer>();
	public Integer lastPosition = -1;
	public Integer lastPlayers = -1;
		
	public AgentWorldModel(EnvironmentStateModel model) {
		super(model);
		
	
		//Strategy
		positionToSet.add(15);	 //A	
		positionToSet.add(4);   //D
		positionToSet.add(9);   //M
		positionToSet.add(19);   //A
		positionToSet.add(1);   //D
		positionToSet.add(8);   //M
		positionToSet.add(17);   //A
		positionToSet.add(5);   //D
		positionToSet.add(10);   //M
		positionToSet.add(18);   //A
		positionToSet.add(6);   //D
		positionToSet.add(12);   //M
		positionToSet.add(14);   //A
		positionToSet.add(13);   //D
		positionToSet.add(2);   //M
		positionToSet.add(16);   //A
		positionToSet.add(3);   //D
		positionToSet.add(7);   //M
		positionToSet.add(11);   //M
		
		//What's moment set the goalkeeper?
		positionToSet.add(0);   //D
	}

	
	public Player getMe() {
		for (Player player : ((World)environmentStateModel).playersA) {
			if (player.id == myID) {
               	return player;
			}
		}

		for (Player player : ((World)environmentStateModel).playersB) {
			if (player.id == myID) {
				return player;
			}
		}

		return null;
	}	
}
