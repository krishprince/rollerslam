package rollerslam.agent.player;

import java.rmi.RemoteException;
import java.util.Vector;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.environment.model.strategy.DefineRoleFact;
import rollerslam.environment.model.strategy.PositionCoord;
import rollerslam.environment.model.utils.MathGeometry;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalUpdateComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.server.SimulationState;
import rollerslam.logging.GoalUpdateLogEntry;

public class AgentGoalUpdater implements GoalUpdateComponent {
	String logMsg = "";
	int cycle = 0;
	int id = -1;

	public void updateGoal(GoalBasedEnvironmentStateModel goal) {
		AgentWorldModel model = (AgentWorldModel) goal;
		
		if (model != null && model.changed) {

			ClientFacade facade = ClientFacadeImpl.getInstance();
			SimulationState state = SimulationState.STOPPED;

			logMsg = "";

			try {
				state = facade.getSimulationAdmin().getState();
			} catch (RemoteException e) {
				if (PrintTrace.TracePrint) {
					if (PrintTrace.TracePrint) {
						e.printStackTrace();
					}
				}
			}

			if (model != null && model.environmentStateModel != null) {
				calculeMaxArea(model);
			}

			if (state == SimulationState.STOPPED) {
				// Stop reasoning
			} else if (model.currentGoal == AgentGoal.JOIN_GAME) {
				joinGame(model);
			} else if (model.currentGoal == AgentGoal.WAIT_JOIN_GAME) {
				waitJoinGame(model);
			} else if (model.currentGoal == AgentGoal.INITIALIZATION) {
				initialization(model);
			} else if (model.currentGoal == AgentGoal.SET_ROLES) {
				setRoles(model);
			} else if (model.currentGoal == AgentGoal.GO_TO_INIT_COORD) {
				goToInitCoord(model);
			} else if (model.currentGoal == AgentGoal.STOP) {
				stop(model);
			} else if (model.currentGoal == AgentGoal.WAIT_MOVIMENT) {
				waitMoviment(model);
			} else if (model.currentGoal == AgentGoal.GO_TO_BALL) {
				goToBall(model);
			} else if (model.currentGoal == AgentGoal.CATCH_BALL) {
				catchBall(model);
			} else if (model.currentGoal == AgentGoal.TACKLE_PLAYER) {
				tacklePlayer(model);
			} else if (model.currentGoal == AgentGoal.STAND_UP) {
				standUp(model);
			} else if (model.currentGoal == AgentGoal.GO_TO_GOAL) {
				goToGoal(model);
			} else if (model.currentGoal == AgentGoal.THROW_BALL) {
				throwBall(model);
			} else if (model.currentGoal == AgentGoal.KICK_BALL) {
				kickBall(model);
			} else if (model.currentGoal == AgentGoal.COUNTER_TACKLE) {
				counterTackle(model);
			}

			if (id != -1) {
				try {
					GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(cycle,
							id, logMsg);
					facade.getLogRecordingService().addEntry(envLog);
				} catch (RemoteException e) {
					if (PrintTrace.TracePrint) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void joinGame(AgentWorldModel model){
		if (model.joinMessageSent) {
            model.currentGoal = AgentGoal.WAIT_JOIN_GAME;
		}
	}
	
	private void waitJoinGame(AgentWorldModel model){
        if (model.gameStarted) {
        	logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.WAIT_JOIN_GAME.getValue(); 
        	
			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.INITIALIZATION.getValue();
			
			cycle = ((World)model.environmentStateModel).currentCycle;
			id = model.getMe().id;
			
       		model.currentGoal = AgentGoal.INITIALIZATION;
        } else {
        	if (model.myID != -1 && model.environmentStateModel != null) {
        		model.gameStarted = true;
        	}
        }
	}
	
	private void initialization(AgentWorldModel model){
		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.INITIALIZATION.getValue();
		
		logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.SET_ROLES.getValue();
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = model.getMe().id;

		model.currentGoal = AgentGoal.SET_ROLES;
		model.cycleLastMsg = ((World)model.environmentStateModel).currentCycle;
	}
	
	private void setRoles(AgentWorldModel model){
		Vector<Message> acts = ((World)model.environmentStateModel).actions;

		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.SET_ROLES.getValue();
		
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = model.getMe().id;

		for(Message a : acts){
			if(a instanceof SendMsgAction){
				Fact f = ((SendMsgAction)a).subject;
				if(f.receiver.equals(String.valueOf(model.myID))){					
					if(f.message instanceof DefineRoleFact){
						model.position = ((DefineRoleFact)f.message).position;
						model.role = ((DefineRoleFact)f.message).role;

						if(model.myTeam == PlayerTeam.TEAM_A){
							model.posCoord = ((DefineRoleFact)f.message).posCoord;
						} else {
							model.posCoord = ((DefineRoleFact)f.message).posCoord;
							model.posCoord.x *= -1;
							model.posCoord.y *= -1;
						}

						logMsg += " -- " + AgentGoalLogMessages.ROLES_RECEIVED.getValue().replace("%POS%", model.position.toString()).replace("%ROLE%", model.role.toString());

						logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD;
						model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
					}
				}
			}
		}	
		
		if(model.cycleLastMsg != -1 && ((World)model.environmentStateModel).currentCycle - model.cycleLastMsg > 10){
			logMsg += " -- " + AgentGoalLogMessages.NOT_RECEIVE_COACH_MESG.getValue();
			
			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.INITIALIZATION.getValue();
			model.currentGoal = AgentGoal.INITIALIZATION;
		}
	}
	
	private void goToInitCoord(AgentWorldModel model){
		Player me = model.getMe();

		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = me.id;
		
		if(me.inGround){
			logMsg += " -- " + AgentGoalLogMessages.IN_GROUND;

			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.STAND_UP.getValue();
			model.currentGoal = AgentGoal.STAND_UP;
		} else if(me.hasBall){
			logMsg += " -- " + AgentGoalLogMessages.HAS_BALL.getValue();

			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_GOAL.getValue();
			model.currentGoal = AgentGoal.GO_TO_GOAL;
		} else if(nearBall(model)){
			logMsg += " -- " + AgentGoalLogMessages.NEAR_BALL.getValue();

			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_BALL.getValue();
			model.currentGoal = AgentGoal.GO_TO_BALL;
		} else if(MathGeometry.calculeDistancePoints(me.s.x, model.posCoord.x, me.s.y, model.posCoord.y) < model.myMaxArea * 0.10) {
			logMsg += " -- " + AgentGoalLogMessages.IN_AREA.getValue();

			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.STOP.getValue();
			model.currentGoal = AgentGoal.STOP;
		}
	}
	
	private void stop(AgentWorldModel model){
		Player me = model.getMe();

		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.STOP.getValue();
		
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = me.id;

		//It only will stop to dash with a = -v when v = 0 and a = 0, but, he is near the ball, he go to catch it
		if(nearBall(model)){
			logMsg += " -- " + AgentGoalLogMessages.NEAR_BALL.getValue();

			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_BALL.getValue();
			model.currentGoal = AgentGoal.GO_TO_BALL;
		} else if(me.v.getModule() == 0 && me.a.getModule() == 0 ){
			logMsg += " -- " + AgentGoalLogMessages.IN_AREA.getValue();

			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.WAIT_MOVIMENT.getValue();
			model.currentGoal = AgentGoal.WAIT_MOVIMENT;
		}
	}
	
	private void waitMoviment(AgentWorldModel model){
		//if(MathGeometry.calculeDistancePoints(me.world.ball.s.x, model.posCoord.x, me.world.ball.s.y, model.posCoord.y) < model.myMaxArea) {
		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.WAIT_MOVIMENT.getValue();
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = model.getMe().id;

		if(nearBall(model)){
			logMsg += " -- " + AgentGoalLogMessages.NEAR_BALL.getValue();

			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_BALL.getValue();
			model.currentGoal = AgentGoal.GO_TO_BALL;
		}
	}
	
	private void goToBall(AgentWorldModel model){
		Player me = model.getMe();

		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.GO_TO_BALL.getValue();
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = me.id;
		
		if(!me.inGround){
			logMsg += " -- " + AgentGoalLogMessages.NOT_IN_GROUND.getValue();
        	//if(MathGeometry.calculeDistancePoints(me.s.x, model.posCoord.x, me.s.y, model.posCoord.y) > model.myMaxArea) {
        	if(!nearBall(model)){
        		logMsg += " -- " + AgentGoalLogMessages.FAR_BALL.getValue();
        		
        		logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
    			model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
    		} else if (MathGeometry.calculeDistancePoints(me.s.x, me.world.ball.s.x, me.s.y, me.world.ball.s.y) < 5000) {
    			logMsg += " -- " + AgentGoalLogMessages.VERY_NEAR_BALL.getValue();
                if(!me.world.ball.withPlayer){
                	logMsg += " -- " + AgentGoalLogMessages.BALL_WITH_NOBODY.getValue();
                	
                	logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.CATCH_BALL.getValue();
                    model.currentGoal = AgentGoal.CATCH_BALL;				
                }else if(me.world.playerWithBall != null){
                	logMsg += " -- " + AgentGoalLogMessages.BALL_WITH_ANYBODY.getValue();
                	if(me.world.playerWithBall.team == me.team){
                		logMsg += " -- " + AgentGoalLogMessages.BALL_WITH_TEAM.getValue();
                		
                		logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
                		model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
                	}else{
                		logMsg += " -- " + AgentGoalLogMessages.BALL_WITH_OPPONENT.getValue();
                		
                		logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.TACKLE_PLAYER.getValue();
                		model.currentGoal = AgentGoal.TACKLE_PLAYER;
                	}
                }
            }
        } else {
        	logMsg += " -- " + AgentGoalLogMessages.IN_GROUND.getValue();
        	
        	logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.STAND_UP.getValue();
        	model.currentGoal = AgentGoal.STAND_UP;
        }
	}
	
	private void catchBall(AgentWorldModel model){
		Player me = model.getMe();

		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.CATCH_BALL.getValue();
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = me.id;

		if(me.hasBall){
			logMsg += " -- " + AgentGoalLogMessages.IN_AREA.getValue();
			
        	boolean nearOpponent = false;
        	
        	if(me.team == PlayerTeam.TEAM_A){
        		for(Player p : me.world.playersB){
        			if(MathGeometry.calculeDistancePoints(me.s.x, p.s.x, me.s.y, p.s.y) < 5000 && Math.random() > 0.5){
        				nearOpponent = true;
        			}
        		}
        	}else{
        		for(Player p : me.world.playersA){
        			if(MathGeometry.calculeDistancePoints(me.s.x, p.s.x, me.s.y, p.s.y) < 5000 && Math.random() > 0.5){
        				nearOpponent = true;
        			}
        		}
        	}
        	
        	if(nearOpponent){
        		logMsg += " -- " + AgentGoalLogMessages.NEAR_OPPONENT.getValue();
        		
        		logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.COUNTER_TACKLE.getValue();
                model.currentGoal = AgentGoal.COUNTER_TACKLE;
        	}else{
        		logMsg += " -- " + AgentGoalLogMessages.FAR_OPPONENT.getValue();
        		
        		logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_GOAL.getValue();
                model.currentGoal = AgentGoal.GO_TO_GOAL;
        	}
        }else{
        	logMsg += " -- " + AgentGoalLogMessages.DOESNT_HAVE_BALL.getValue();
        	if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
        		logMsg += " -- " + AgentGoalLogMessages.BALL_WITH_TEAM.getValue();
        		
        		logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
        		model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
            }else{
            	logMsg += " -- " + AgentGoalLogMessages.BALL_WITHOUT_TEAM.getValue();
        		        		
        		if(!nearBall(model)){
        			logMsg += " -- " + AgentGoalLogMessages.FAR_BALL.getValue();
        			
        			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
        			model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
        		} else {
        			logMsg += " -- " + AgentGoalLogMessages.NEAR_BALL.getValue();
        			
        			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_BALL.getValue();
        			model.currentGoal = AgentGoal.GO_TO_BALL;
        		}
            }
        }		
	}
	
	private void tacklePlayer(AgentWorldModel model){
		Player me = model.getMe();
        
		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.TACKLE_PLAYER.getValue();
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = me.id;
		
		if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
			logMsg += " -- " + AgentGoalLogMessages.BALL_WITH_TEAM.getValue();
    		
			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
    		model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
        }else{
        	logMsg += " -- " + AgentGoalLogMessages.BALL_WITHOUT_TEAM.getValue();
    		
    		if(!nearBall(model)){
    			logMsg += " -- " + AgentGoalLogMessages.FAR_BALL.getValue();
    			
    			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
    			model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
    		} else {
    			logMsg += " -- " + AgentGoalLogMessages.NEAR_BALL.getValue();
    			
    			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_BALL.getValue();
    			model.currentGoal = AgentGoal.GO_TO_BALL;
    		}
        }		
	}
	
	private void standUp(AgentWorldModel model){
		Player me = model.getMe();
		
		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.STAND_UP.getValue();
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = me.id;

		if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
			logMsg += " -- " + AgentGoalLogMessages.BALL_WITH_TEAM.getValue();
    		
			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
			model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
        }else{
        	logMsg += " -- " + AgentGoalLogMessages.BALL_WITHOUT_TEAM.getValue();
    		
    		if(!nearBall(model)){
    			logMsg += " -- AGENT FAR THE BALL";
    			
    			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
    			model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
    		} else {
    			logMsg += " -- " + AgentGoalLogMessages.NEAR_BALL.getValue();
    			
    			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_BALL.getValue();
    			model.currentGoal = AgentGoal.GO_TO_BALL;
    		}
        }
	}
	
	private void goToGoal(AgentWorldModel model){
		Player me = model.getMe();
		
		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.GO_TO_GOAL.getValue();
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = me.id;

    	if(me.inGround){
    		logMsg += " -- " + AgentGoalLogMessages.IN_GROUND.getValue();

    		logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.STAND_UP.getValue();    		
    		model.currentGoal = AgentGoal.STAND_UP;
    	} else if (me.hasBall) {
    		logMsg += " -- " + AgentGoalLogMessages.HAS_BALL.getValue();
    		
    		if(shootBall(model)){
    			logMsg += " -- " + AgentGoalLogMessages.MANY_OPPONENTS_NEAR.getValue();
    			
    			logMsg += " -- " + AgentGoalLogMessages.KICK_BALL.getValue();
    			model.currentGoal = AgentGoal.KICK_BALL;
    		} else if(me.team == PlayerTeam.TEAM_A){
				if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalB.s.x, me.s.y, me.world.goalB.s.y) < 30000) {
					logMsg += " -- " + AgentGoalLogMessages.NEAR_GOAL.getValue();
					
					logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.THROW_BALL.getValue();
	        		model.currentGoal = AgentGoal.THROW_BALL;
				}else if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalB.s.x, me.s.y, me.world.goalB.s.y) < 50000) {
					logMsg += " -- " + AgentGoalLogMessages.NEAR_GOAL.getValue();
					
					logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.KICK_BALL.getValue();
	        		model.currentGoal = AgentGoal.KICK_BALL;
				}
			}else{
				if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalA.s.x, me.s.y, me.world.goalA.s.y) < 30000) {
					logMsg += " -- " + AgentGoalLogMessages.NEAR_GOAL.getValue();
					
					logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.THROW_BALL.getValue();
	        		model.currentGoal = AgentGoal.THROW_BALL;    					
				}if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalA.s.x, me.s.y, me.world.goalA.s.y) < 50000) {
					logMsg += " -- " + AgentGoalLogMessages.NEAR_GOAL.getValue();
					
					logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.KICK_BALL.getValue();
	        		model.currentGoal = AgentGoal.KICK_BALL;
				}
			}        	
    	} else {
			logMsg += " -- " + AgentGoalLogMessages.DOESNT_HAVE_BALL.getValue();
    		if(!nearBall(model)){
    			logMsg += " -- " + AgentGoalLogMessages.FAR_BALL.getValue();
    			
    			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
    			model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
    		} else {
    			logMsg += " -- " + AgentGoalLogMessages.NEAR_BALL.getValue();
    			
    			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_BALL.getValue();
    			model.currentGoal = AgentGoal.GO_TO_BALL;
    		}
    	}
	}
	
	private void throwBall(AgentWorldModel model){
		Player me = model.getMe();

		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.THROW_BALL.getValue();
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = me.id;
		
	    if(me.inGround){
	    	logMsg += " -- " + AgentGoalLogMessages.IN_GROUND.getValue();

	    	logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.STAND_UP.getValue();
	   		model.currentGoal = AgentGoal.STAND_UP;
    	} else {      		
    		if(me.world.ball.withPlayer && me.world.playerWithBall.team == me.team){
    			logMsg += " -- " + AgentGoalLogMessages.BALL_WITH_TEAM.getValue();
    			
    			if(me.hasBall){
    				logMsg += " -- " + AgentGoalLogMessages.HAS_BALL.getValue();
    				
    				logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_GOAL.getValue();
    				model.currentGoal = AgentGoal.GO_TO_GOAL;
    			} else {
    				logMsg += " -- " + AgentGoalLogMessages.DOESNT_HAVE_BALL.getValue();
    				
    				logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
    				model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
    			}
    		}else{
    			logMsg += " -- " + AgentGoalLogMessages.BALL_WITHOUT_TEAM.getValue();

	       		if(!nearBall(model)) {
	       			logMsg += " -- " + AgentGoalLogMessages.FAR_BALL.getValue();
	       			
	       			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
	    			model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
	    		} else {
	    			logMsg += " -- " + AgentGoalLogMessages.NEAR_BALL.getValue();
	    			
	    			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_BALL.getValue();
	    			model.currentGoal = AgentGoal.GO_TO_BALL;
	    		}
    		}
    	}
	}
	
	private void kickBall(AgentWorldModel model){
		Player me = model.getMe();

		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.KICK_BALL.getValue();
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = me.id;
		
	    if(me.inGround){
	    	logMsg += " -- " + AgentGoalLogMessages.IN_GROUND.getValue();

	    	logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.STAND_UP.getValue();
	   		model.currentGoal = AgentGoal.STAND_UP;
    	} else {      		
    		if(me.world.ball.withPlayer && me.world.playerWithBall.team == me.team){
    			logMsg += " -- " + AgentGoalLogMessages.BALL_WITH_TEAM.getValue();
    			
    			if(me.hasBall){
    				logMsg += " -- " + AgentGoalLogMessages.HAS_BALL.getValue();
    				
    				logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_GOAL.getValue();
    				model.currentGoal = AgentGoal.GO_TO_GOAL;
    			} else {
    				logMsg += " -- " + AgentGoalLogMessages.DOESNT_HAVE_BALL.getValue();
    				
    				logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
    				model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
    			}
    		}else{
    			logMsg += " -- " + AgentGoalLogMessages.BALL_WITHOUT_TEAM.getValue();

	       		if(!nearBall(model)) {
	       			logMsg += " -- " + AgentGoalLogMessages.FAR_BALL.getValue();
	       			
	       			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
	    			model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
	    		} else {
	    			logMsg += " -- " + AgentGoalLogMessages.NEAR_BALL.getValue();
	    			
	    			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_BALL.getValue();
	    			model.currentGoal = AgentGoal.GO_TO_BALL;
	    		}
    		}
    	}
	}
	
	private void counterTackle(AgentWorldModel model){
		Player me = model.getMe();

		logMsg += " -- " + AgentGoalLogMessages.LAST_GOAL.getValue() + AgentGoalLogMessages.COUNTER_TACKLE.getValue();
		cycle = ((World)model.environmentStateModel).currentCycle;
		id = me.id;

		if(me.inGround){
    		logMsg += " -- " + AgentGoalLogMessages.IN_GROUND.getValue();
    		
    		logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.STAND_UP.getValue();
     		model.currentGoal = AgentGoal.STAND_UP;
     	} else if(me.hasBall){
     		logMsg += " -- " + AgentGoalLogMessages.HAS_BALL.getValue();
     		
     		logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_GOAL.getValue();
     		model.currentGoal = AgentGoal.GO_TO_GOAL;
     	} else {
     		logMsg += " -- " + AgentGoalLogMessages.DOESNT_HAVE_BALL.getValue();

    		if(!nearBall(model)) {
    			logMsg += " -- " + AgentGoalLogMessages.FAR_BALL.getValue();
    			
    			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_INIT_COORD.getValue();
    			model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
    		} else {
    			logMsg += " -- " + AgentGoalLogMessages.NEAR_BALL.getValue();
    			
    			logMsg += " -- " + AgentGoalLogMessages.NEXT_GOAL.getValue() + AgentGoalLogMessages.GO_TO_BALL.getValue();
    			model.currentGoal = AgentGoal.GO_TO_BALL;
    		}
     	}		
	}
	
	private boolean nearBall(AgentWorldModel model){
		Player me = model.getMe();

		if(MathGeometry.calculeDistancePoints(me.world.ball.s.x, model.posCoord.x, me.world.ball.s.y, model.posCoord.y) < model.myMaxArea) {
			return true;
		}
		
		int myDistance = MathGeometry.calculeDistancePoints(me.s.x, me.world.ball.s.x, me.s.y, me.world.ball.s.y);
		boolean flag = true;
		
		Player[] team = null;
		
		if(model.myTeam == PlayerTeam.TEAM_A){
			team = me.world.playersA;
		}else{
			team = me.world.playersB;
		}
		
		for(Player others : team){
			if(myDistance >= MathGeometry.calculeDistancePoints(others.s.x, me.world.ball.s.x, others.s.y, me.world.ball.s.y) && others.id != me.id){
				flag = false;
			}
		}
		
		return flag;
	}
	
	private boolean shootBall(AgentWorldModel model){
		Player me = model.getMe();
		
		Player[] enemyTeam = null;
		int qtdEnemyPlayers = 0;
		
		if(model.myTeam == PlayerTeam.TEAM_A){
			enemyTeam = ((World)model.environmentStateModel).playersB;
		}else{
			enemyTeam = ((World)model.environmentStateModel).playersA;
		}
		
		for(Player enemy : enemyTeam){
			if(MathGeometry.calculeDistancePoints(enemy.s.x, me.s.x, enemy.s.y, me.s.y) < model.myMaxArea){
				qtdEnemyPlayers++;
			}
		}
		
		if(qtdEnemyPlayers >= PositionCoord.maxPlayers * me.world.playersA.length){
			return true;
		} else {
			return false;
		}
	}
	
	private void calculeMaxArea(AgentWorldModel model){
		Player me = model.getMe();
		
		if(me == null){
			model.myMaxArea = PositionCoord.maxArea;
			return;
		}
		
		Player[] enemyTeam = null;
		Player[] allyTeam = null;
		int qtdEnemyPlayers = 0;
		int qtdAllyPlayers = 0;
		boolean ballWithTeam = false;
		
		if(model.myTeam == PlayerTeam.TEAM_A){
			enemyTeam = ((World)model.environmentStateModel).playersB;
		}else{
			enemyTeam = ((World)model.environmentStateModel).playersA;
		}
		
		for(Player enemy : enemyTeam){
			if(MathGeometry.calculeDistancePoints(enemy.s.x, me.s.x, enemy.s.y, me.s.y) < model.myMaxArea){
				qtdEnemyPlayers++;
			}
		}
				
		if(model.myTeam == PlayerTeam.TEAM_A){
			allyTeam = ((World)model.environmentStateModel).playersA;
		}else{
			allyTeam = ((World)model.environmentStateModel).playersB;
		}
		
		for(Player ally : allyTeam){
			if(MathGeometry.calculeDistancePoints(ally.s.x, me.s.x, ally.s.y, me.s.y) < model.myMaxArea && ally.id != me.id){
				qtdAllyPlayers++;
			}
		}
		
		if(me.world.playerWithBall != null && me.team == me.world.playerWithBall.team){
			ballWithTeam = true;
		}
		
		model.myMaxArea = (int)(PositionCoord.maxArea * calcFactorMult(qtdEnemyPlayers, qtdAllyPlayers, ballWithTeam, me.world.playersA.length));
		
		
	}
	
	public double calcFactorMult(int qtdEnemyPlayers, int qtdAllyPlayers, boolean ballWithTeam, int qtdPlayersTeam){
		double factorMult = 1;
		
		//Ball not with team +1
		if(!ballWithTeam){
			factorMult += 0.7;
		}
		
		factorMult += 2 * (qtdEnemyPlayers / qtdPlayersTeam);
		
		factorMult -= 1 * (qtdAllyPlayers / qtdPlayersTeam);
		
		if(factorMult < 1){
			factorMult = 1;
		}


		return factorMult;
		//return 1;
	}
}