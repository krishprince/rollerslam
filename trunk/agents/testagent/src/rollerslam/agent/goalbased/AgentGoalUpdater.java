package rollerslam.agent.goalbased;

import java.rmi.RemoteException;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.utils.MathGeometry;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalUpdateComponent;
import rollerslam.infrastructure.server.ServerFacade;
import rollerslam.infrastructure.server.ServerFacadeImpl;
import rollerslam.infrastructure.server.SimulationState;
import rollerslam.logging.GoalUpdateLogEntry;

public class AgentGoalUpdater implements GoalUpdateComponent {
        @SuppressWarnings("unused")
		private Agent remoteThis;
        
        public AgentGoalUpdater(Agent remoteThis){
            this.remoteThis = remoteThis;
        }
        
	public void updateGoal(GoalBasedEnvironmentStateModel goal) {
		AgentWorldModel model = (AgentWorldModel) goal;
		
		ServerFacade facade = ServerFacadeImpl.getInstance();
		SimulationState state = SimulationState.STOPPED;
		
		try {
			state = facade.getSimulationStateProvider().getState();
		} catch (RemoteException e) {
			e.printStackTrace();
		}	
		
		
		if(state == SimulationState.STOPPED){
			//Stop reasoning			
		} else if (model.currentGoal == AgentGoal.JOIN_GAME) {
			if (model.joinMessageSent) {
                model.currentGoal = AgentGoal.WAIT_JOIN_GAME;
			}
		} else if (model.currentGoal == AgentGoal.WAIT_JOIN_GAME) {
            if (model.gameStarted) {
            	Player me = model.getMe();
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to Goal");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
            	model.currentGoal = AgentGoal.GO_TO_BALL;
            } else {
            	if (model.myID != -1 && model.environmentStateModel != null) {
            		model.gameStarted = true;
            	}
            }
		} else if (model.currentGoal == AgentGoal.GO_TO_BALL) { 
			Player me = model.getMe();
            if(!me.inGround){
                if (MathGeometry.calculeDistancePoints(me.s.x, me.world.ball.s.x, me.s.y, me.world.ball.s.y) < 5000) {
                    if(!me.world.ball.withPlayer){
                		try {
                			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Catch the ball");
                			facade.getLogRecordingService().addEntry(envLog);
                		} catch (RemoteException e) {
                			e.printStackTrace();
                		}
                        model.currentGoal = AgentGoal.CATCH_BALL;				
                    }else if(me.world.playerWithBall != null){
                    	if(me.world.playerWithBall.team == me.team){
                    		try {
                    			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Ball with team. Do nothing");
                    			facade.getLogRecordingService().addEntry(envLog);
                    		} catch (RemoteException e) {
                    			e.printStackTrace();
                    		}
                    	}else{
                    		try {
                    			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Tackle the Player");
                    			facade.getLogRecordingService().addEntry(envLog);
                    		} catch (RemoteException e) {
                    			e.printStackTrace();
                    		}
                    		model.currentGoal = AgentGoal.TACKLE_PLAYER;
                    	}
                    }
                }
            } else {
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Stand Up");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
            	model.currentGoal = AgentGoal.STAND_UP;
            }
        } else if(model.currentGoal == AgentGoal.CATCH_BALL){
			Player me = model.getMe();
            if(me.hasBall){
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
            		try {
            			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "CounterTackle");
            			facade.getLogRecordingService().addEntry(envLog);
            		} catch (RemoteException e) {
            			e.printStackTrace();
            		}
	                model.currentGoal = AgentGoal.COUNTER_TACKLE;
            	}else{
            		try {
            			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to goal");
            			facade.getLogRecordingService().addEntry(envLog);
            		} catch (RemoteException e) {
            			e.printStackTrace();
            		}
	                model.currentGoal = AgentGoal.GO_TO_GOAL;
            	}
            }else{
            	if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
            		try {
            			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Ball with team. Do nothing");
            			facade.getLogRecordingService().addEntry(envLog);
            		} catch (RemoteException e) {
            			e.printStackTrace();
            		}
                }else{
            		try {
            			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
            			facade.getLogRecordingService().addEntry(envLog);
            		} catch (RemoteException e) {
            			e.printStackTrace();
            		}
                	model.currentGoal = AgentGoal.GO_TO_BALL;
                }
            }
        } else if(model.currentGoal == AgentGoal.TACKLE_PLAYER) {
			Player me = model.getMe();
            
            if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Ball with team. Do nothing");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
            }else{
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
            	model.currentGoal = AgentGoal.GO_TO_BALL;
            }
        } else if(model.currentGoal == AgentGoal.STAND_UP) {
			Player me = model.getMe();
        	
        	if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Ball with team. Do nothing");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
            }else{
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
            	model.currentGoal = AgentGoal.GO_TO_BALL;
            }
        } else if(model.currentGoal == AgentGoal.GO_TO_GOAL){
			Player me = model.getMe();
        	if(me.inGround){
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Stand Up");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
        		model.currentGoal = AgentGoal.STAND_UP;
        	} else {
        		if(me.team == PlayerTeam.TEAM_A){
    				if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalB.s.x, me.s.y, me.world.goalB.s.y) < 30000) {
    	        		try {
    	        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Throw the ball");
    	        			facade.getLogRecordingService().addEntry(envLog);
    	        		} catch (RemoteException e) {
    	        			e.printStackTrace();
    	        		}
    	        		model.currentGoal = AgentGoal.THROW_BALL;
    				}else if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalB.s.x, me.s.y, me.world.goalB.s.y) < 50000) {
    	        		try {
    	        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Kick the ball");
    	        			facade.getLogRecordingService().addEntry(envLog);
    	        		} catch (RemoteException e) {
    	        			e.printStackTrace();
    	        		}
    	        		model.currentGoal = AgentGoal.KICK_BALL;
    				}
    			}else{
    				if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalA.s.x, me.s.y, me.world.goalA.s.y) < 30000) {
    	        		try {
    	        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Throw the ball");
    	        			facade.getLogRecordingService().addEntry(envLog);
    	        		} catch (RemoteException e) {
    	        			e.printStackTrace();
    	        		}
    	        		model.currentGoal = AgentGoal.THROW_BALL;    					
    				}if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalA.s.x, me.s.y, me.world.goalA.s.y) < 50000) {
    	        		try {
    	        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Kick the ball");
    	        			facade.getLogRecordingService().addEntry(envLog);
    	        		} catch (RemoteException e) {
    	        			e.printStackTrace();
    	        		}
    	        		model.currentGoal = AgentGoal.KICK_BALL;
    				}
    			}        	
        	}
         } else if(model.currentGoal == AgentGoal.THROW_BALL){
 			Player me = model.getMe();
        	 if(me.inGround){
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Stand Up");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
         		model.currentGoal = AgentGoal.STAND_UP;
         	} else {
         		if(me.world.ball.withPlayer && me.world.playerWithBall.team != me.team){
	        		try {
	        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
	        			facade.getLogRecordingService().addEntry(envLog);
	        		} catch (RemoteException e) {
	        			e.printStackTrace();
	        		}
                	model.currentGoal = AgentGoal.GO_TO_BALL;
         		}
         	}
         } else if(model.currentGoal == AgentGoal.KICK_BALL){
        	 Player me = model.getMe();
        	 if(me.inGround){
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Stand Up");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
         		model.currentGoal = AgentGoal.STAND_UP;
         	} else {
         		if(me.world.ball.withPlayer && me.world.playerWithBall.team != me.team){
	        		try {
	        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
	        			facade.getLogRecordingService().addEntry(envLog);
	        		} catch (RemoteException e) {
	        			e.printStackTrace();
	        		}
                	model.currentGoal = AgentGoal.GO_TO_BALL;
         		}
         	}
         } else if(model.currentGoal == AgentGoal.COUNTER_TACKLE){
        	 Player me = model.getMe();
        	 if(me.inGround){
         		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Stand Up");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
         		model.currentGoal = AgentGoal.STAND_UP;
         	} else if(me.hasBall){
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
         		model.currentGoal = AgentGoal.GO_TO_GOAL;
         	} else {
        		try {
        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
        			facade.getLogRecordingService().addEntry(envLog);
        		} catch (RemoteException e) {
        			e.printStackTrace();
        		}
         		model.currentGoal = AgentGoal.GO_TO_BALL;
         	}
         }
	}
	
}
