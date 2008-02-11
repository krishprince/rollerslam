package rollerslam.agent.coach;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.Vector;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.environment.model.strategy.InitializationFact;
import rollerslam.environment.model.strategy.PositionCoord;
import rollerslam.environment.model.strategy.Receivers;
import rollerslam.environment.model.utils.MathGeometry;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalUpdateComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.SimulationState;
import rollerslam.logging.GoalUpdateLogEntry;

public class CoachAgentGoalUpdater implements GoalUpdateComponent {
	String logMsg = "";
	int cycle = 0;
	int id = -1;

	public void updateGoal(GoalBasedEnvironmentStateModel goal) {
		CoachAgentWorldModel model = (CoachAgentWorldModel) goal;
		
		ClientFacade facade = ClientFacadeImpl.getInstance();
		SimulationState state = SimulationState.STOPPED;
		
		try {
			state = facade.getSimulationAdmin().getState();
		} catch (RemoteException e) {
			if (PrintTrace.TracePrint){
				if (PrintTrace.TracePrint){
					e.printStackTrace();
				}
			}
		}	
		
		logMsg = "";
		
		if(state == SimulationState.STOPPED){
			//Stop reasoning			
		} else if (model.currentGoal == CoachAgentGoal.WAIT_JOIN_GAME) {
			waitJoinGame(model);
		} else if (model.currentGoal == CoachAgentGoal.LISTENING) {
			listening(model);
		} else if (model.currentGoal == CoachAgentGoal.SET_POSITION) {
			setPosition(model);
		} else if (model.currentGoal == CoachAgentGoal.CHANGE_DEFENSIVE_POSITION) {
			changeDefensivePosition(model);
		} else if (model.currentGoal == CoachAgentGoal.CHANGE_OFFENSIVE_POSITION) {
			changeOffensivePosition(model);
		} else if (model.currentGoal == CoachAgentGoal.GO_TO_BALL) {
			goToBall(model);
		}
		
		if (id != -1){
			try {
				GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(cycle, id, logMsg);
				facade.getLogRecordingService().addEntry(envLog);
			} catch (RemoteException e) {
				if (PrintTrace.TracePrint){
					e.printStackTrace();
				}
			}
		}

	}
	
	private void waitJoinGame(CoachAgentWorldModel model){
        if (model.gameStarted) {
        	model.currentGoal = CoachAgentGoal.LISTENING;
        } else {
        	if (model.environmentStateModel != null) {
        		model.gameStarted = true;
        	}
        }
	}
	
	private void listening(CoachAgentWorldModel model){
		cycle = ((World)model.environmentStateModel).currentCycle;
		
		if(model.myTeam == PlayerTeam.TEAM_A){
			id = -2;
		} else {
			id = -3;
		}
			
		logMsg += " -- I WAS LISTENING";
		
		Vector<Message> acts = ((World)model.environmentStateModel).actions;
		String me = null;
		
		if(model.myTeam == PlayerTeam.TEAM_A)
			me = Receivers.COACH_A.getValue();
		else
			me = Receivers.COACH_B.getValue();

		for(Message a : acts){
			if(a instanceof SendMsgAction){
				Fact f = ((SendMsgAction)a).subject;
				if(f.receiver.equals(me)){
					if(f.message instanceof InitializationFact){
						if(!model.playersToSetPosition.contains(Integer.parseInt(f.sender))){
							model.playersToSetPosition.add(Integer.parseInt(f.sender));
							
							logMsg += " -- RECEIVED INITIALIZE MESSAGE - AGENT " + Integer.parseInt(f.sender);
						}
					}
				}
			}
		}

		if(model.playersToSetPosition.size() > 0){
			if(model.playersPosition.containsKey(model.playersToSetPosition.get(0))){
				model.playersPosition.put(model.playersToSetPosition.get(0), model.playersPosition.get(model.playersToSetPosition.get(0)));
				model.lastPlayers = model.playersToSetPosition.get(0);
				model.lastPosition = model.playersPosition.get(model.playersToSetPosition.get(0));
				model.playersToSetPosition.remove(0); 
				
				logMsg += " -- SETTING AGENT POSITION - AGENT " + model.lastPlayers + "; POSITION " + model.lastPosition;
			}else {
				model.playersPosition.put(model.playersToSetPosition.get(0), model.positionToSet.get(0));
				model.lastPlayers = model.playersToSetPosition.get(0);
				model.lastPosition = model.positionToSet.get(0);
				model.playersToSetPosition.remove(0); 
				model.positionToSet.remove(0);
				
				logMsg += " -- SETTING AGENT POSITION - AGENT " + model.lastPlayers + "; POSITION " + model.lastPosition;
			}
			
			logMsg += " -- SENDING POSITION TO AGENT " + model.lastPlayers;
			
			logMsg += " -- NOW I'M SET_POSITION";
			model.currentGoal = CoachAgentGoal.SET_POSITION;
			
			return;
		}
		
		Random rand = new Random();
		
		double variant = rand.nextDouble() + 1;
		
		if(model.myTeam == PlayerTeam.TEAM_A){
			if(((World)model.environmentStateModel).scoreboard.scoreTeamA - ((World)model.environmentStateModel).scoreboard.scoreTeamB > (variant * PositionCoord.GOALDIFFERENCE)){
				if(!model.defensivePosition){
					logMsg += " -- TEAM LOSING WITH " + (((World)model.environmentStateModel).scoreboard.scoreTeamA - ((World)model.environmentStateModel).scoreboard.scoreTeamB) + " DIFERENCE";
					model.defensivePosition = true;
					model.offensivePosition = false;
					logMsg += " -- NOW I'M CHANGE_DEFENSIVE_POSITION";
					model.currentGoal = CoachAgentGoal.CHANGE_DEFENSIVE_POSITION;
				}
				
			} else if(((World)model.environmentStateModel).scoreboard.scoreTeamB - ((World)model.environmentStateModel).scoreboard.scoreTeamA > (variant * PositionCoord.GOALDIFFERENCE)){
				if(!model.offensivePosition){
					logMsg += " -- TEAM WINNING WITH " + (((World)model.environmentStateModel).scoreboard.scoreTeamA - ((World)model.environmentStateModel).scoreboard.scoreTeamB) + " DIFERENCE";
					model.offensivePosition = true;
					model.defensivePosition = false;
					logMsg += " -- NOW I'M CHANGE_OFFENSIVE_POSITION";
					model.currentGoal = CoachAgentGoal.CHANGE_OFFENSIVE_POSITION;
				}
			}
		} else {
			if(((World)model.environmentStateModel).scoreboard.scoreTeamB - ((World)model.environmentStateModel).scoreboard.scoreTeamA > (variant * PositionCoord.GOALDIFFERENCE)){
				if(!model.defensivePosition){
					logMsg += " -- TEAM LOSING WITH " + (((World)model.environmentStateModel).scoreboard.scoreTeamA - ((World)model.environmentStateModel).scoreboard.scoreTeamB) + " DIFERENCE";
					model.defensivePosition = true;
					model.offensivePosition = false;
					logMsg += " -- NOW I'M CHANGE_DEFENSIVE_POSITION";
					model.currentGoal = CoachAgentGoal.CHANGE_DEFENSIVE_POSITION;
				}
			} else if(((World)model.environmentStateModel).scoreboard.scoreTeamA - ((World)model.environmentStateModel).scoreboard.scoreTeamB > (variant * PositionCoord.GOALDIFFERENCE)){
				if(!model.offensivePosition){
					logMsg += " -- TEAM WINNING WITH " + (((World)model.environmentStateModel).scoreboard.scoreTeamA - ((World)model.environmentStateModel).scoreboard.scoreTeamB) + " DIFERENCE";
					model.offensivePosition = true;
					model.defensivePosition = false;
					logMsg += " -- NOW I'M CHANGE_OFFENSIVE_POSITION";
					model.currentGoal = CoachAgentGoal.CHANGE_OFFENSIVE_POSITION;
				}
			}
		}
		
		if(model.currentGoal == CoachAgentGoal.CHANGE_DEFENSIVE_POSITION || model.currentGoal == CoachAgentGoal.CHANGE_OFFENSIVE_POSITION){
			model.playersToChangePosition.clear();
			Iterator<Integer> iterator = model.playersPosition.keySet().iterator(); 
			while(iterator.hasNext()){
				model.playersToChangePosition.add(iterator.next());
			}
			
			return;
		}
		
		if(model.playersToGoBall.size() > 0 || (model.playersToGoBall = verifyHowMuchGoBall(model)).size() > 0){
			logMsg += " -- MANY ENEMY NEAR THE BALL";
			logMsg += " -- NOW I'M GO_TO_BALL";
			model.currentGoal = CoachAgentGoal.GO_TO_BALL;
		}
		
	}
	
	private void setPosition(CoachAgentWorldModel model){
		cycle = ((World)model.environmentStateModel).currentCycle;
		
		if(model.myTeam == PlayerTeam.TEAM_A){
			id = -2;
		} else {
			id = -3;
		}
		logMsg += " -- I WAS SET_POSITION";
		
		logMsg += " -- NOW I'M LISTENING";
		model.currentGoal = CoachAgentGoal.LISTENING;
	}
	
	private void goToBall(CoachAgentWorldModel model){
		cycle = ((World)model.environmentStateModel).currentCycle;
		
		if(model.myTeam == PlayerTeam.TEAM_A){
			id = -2;
		} else {
			id = -3;
		}
		logMsg += " -- I WAS GO_TO_BALL";
		
		logMsg += " -- NOW I'M LISTENING";
		model.currentGoal = CoachAgentGoal.LISTENING;
	}
	
	private void changeOffensivePosition(CoachAgentWorldModel model){
		if(model.playersToChangePosition.size() == 0){
			cycle = ((World)model.environmentStateModel).currentCycle;
			
			if(model.myTeam == PlayerTeam.TEAM_A){
				id = -2;
			} else {
				id = -3;
			}
			logMsg += " -- I WAS CHANGE_OFFENSIVE_POSITION";
			
			logMsg += " -- NOW I'M LISTENING";
			model.currentGoal = CoachAgentGoal.LISTENING;
		}
	}

	private void changeDefensivePosition(CoachAgentWorldModel model){
		if(model.playersToChangePosition.size() == 0){
			cycle = ((World)model.environmentStateModel).currentCycle;
			
			if(model.myTeam == PlayerTeam.TEAM_A){
				id = -2;
			} else {
				id = -3;
			}
			logMsg += " -- I WAS CHANGE_DEFENSIVE_POSITION";
			
			logMsg += " -- NOW I'M LISTENING";
			model.currentGoal = CoachAgentGoal.LISTENING;
		}	
	}

	private Vector<Integer> verifyHowMuchGoBall(CoachAgentWorldModel model){
		Player[] enemyTeam = null;
		Player[] allyTeam = null;
		int qtdEnemyPlayers = 0;
		int qtdAllyPlayers = 0;
		
		TreeMap<Double, Integer> players = new TreeMap<Double, Integer>();
		
		World world = (World)model.environmentStateModel;	

		if(world.playerWithBall != null && model.myTeam == world.playerWithBall.team){
			if(model.myTeam == PlayerTeam.TEAM_A){
				enemyTeam = ((World)model.environmentStateModel).playersB;
			}else{
				enemyTeam = ((World)model.environmentStateModel).playersA;
			}

			for(Player enemy : enemyTeam){
				if(MathGeometry.calculeDistancePoints(enemy.s.x, world.ball.s.x, enemy.s.y, world.ball.s.y) < model.myMaxArea){
					qtdEnemyPlayers++;
				}
			}

			if(model.myTeam == PlayerTeam.TEAM_A){
				allyTeam = ((World)model.environmentStateModel).playersA;
			}else{
				allyTeam = ((World)model.environmentStateModel).playersB;
			}
			
			for(Player ally : allyTeam){
				double distance = MathGeometry.calculeDistancePoints(ally.s.x, world.ball.s.x, ally.s.y, world.ball.s.y);
				players.put(distance, ally.id);
				
				if(distance < model.myMaxArea){
					players.remove(distance);
					qtdAllyPlayers++;
				}
			}
			
			if(qtdEnemyPlayers - qtdAllyPlayers > 0){
				Vector<Integer> a = new Vector<Integer>();
				
				java.util.Iterator<Map.Entry<Double, Integer>> iterator = players.entrySet().iterator();
				int i = 0;
				
				while(iterator.hasNext() && i < qtdEnemyPlayers - qtdAllyPlayers){
					a.add(iterator.next().getValue());
					i++;
				}
				
				return a;
			}
		}

		return new Vector<Integer>();
	}
	

}
