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
	ClientFacade facade = ClientFacadeImpl.getInstance();

	public void updateGoal(GoalBasedEnvironmentStateModel goal) {
		AgentWorldModel model = (AgentWorldModel) goal;
		
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
		
		if(state == SimulationState.STOPPED){
			//Stop reasoning			
		} else if (model.currentGoal == AgentGoal.JOIN_GAME) {
			joinGame(model);
		} else if (model.currentGoal == AgentGoal.WAIT_JOIN_GAME) {
			waitJoinGame(model);
		} else if (model.currentGoal == AgentGoal.INITIALIZATION){
			initialization(model);
		} else if (model.currentGoal == AgentGoal.SET_ROLES) {
			setRoles(model);
		} else if (model.currentGoal == AgentGoal.GO_TO_INIT_COORD){
			goToInitCoord(model);
		} else if (model.currentGoal == AgentGoal.GO_TO_BALL) {
			goToBall(model);
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
	        			if (PrintTrace.TracePrint){
	    					e.printStackTrace();
	    				}
	        		}
	                model.currentGoal = AgentGoal.COUNTER_TACKLE;
	        	}else{
	        		try {
	        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to goal");
	        			facade.getLogRecordingService().addEntry(envLog);
	        		} catch (RemoteException e) {
	        			if (PrintTrace.TracePrint){
	    					e.printStackTrace();
	    				}
	        		}
	                model.currentGoal = AgentGoal.GO_TO_GOAL;
	        	}
	        }else{
	        	if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
	        		try {
	        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Ball with team. Do nothing");
	        			facade.getLogRecordingService().addEntry(envLog);
	        		} catch (RemoteException e) {
	        			if (PrintTrace.TracePrint){
	    					e.printStackTrace();
	    				}
	        		}
	            }else{
	        		try {
	        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
	        			facade.getLogRecordingService().addEntry(envLog);
	        		} catch (RemoteException e) {
	        			if (PrintTrace.TracePrint){
	    					e.printStackTrace();
	    				}
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
	    			if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
	    		}
	        }else{
	    		try {
	    			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
	    			facade.getLogRecordingService().addEntry(envLog);
	    		} catch (RemoteException e) {
	    			if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
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
	    			if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
	    		}
	        }else{
	    		try {
	    			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
	    			facade.getLogRecordingService().addEntry(envLog);
	    		} catch (RemoteException e) {
	    			if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
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
	    			if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
	    		}
	    		model.currentGoal = AgentGoal.STAND_UP;
	    	} else {
	    		if(me.team == PlayerTeam.TEAM_A){
					if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalB.s.x, me.s.y, me.world.goalB.s.y) < 30000) {
		        		try {
		        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Throw the ball");
		        			facade.getLogRecordingService().addEntry(envLog);
		        		} catch (RemoteException e) {
		        			if (PrintTrace.TracePrint){
		    					e.printStackTrace();
		    				}
		        		}
		        		model.currentGoal = AgentGoal.THROW_BALL;
					}else if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalB.s.x, me.s.y, me.world.goalB.s.y) < 50000) {
		        		try {
		        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Kick the ball");
		        			facade.getLogRecordingService().addEntry(envLog);
		        		} catch (RemoteException e) {
		        			if (PrintTrace.TracePrint){
		    					e.printStackTrace();
		    				}
		        		}
		        		model.currentGoal = AgentGoal.KICK_BALL;
					}
				}else{
					if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalA.s.x, me.s.y, me.world.goalA.s.y) < 30000) {
		        		try {
		        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Throw the ball");
		        			facade.getLogRecordingService().addEntry(envLog);
		        		} catch (RemoteException e) {
		        			if (PrintTrace.TracePrint){
		    					e.printStackTrace();
		    				}
		        		}
		        		model.currentGoal = AgentGoal.THROW_BALL;    					
					}if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalA.s.x, me.s.y, me.world.goalA.s.y) < 50000) {
		        		try {
		        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Kick the ball");
		        			facade.getLogRecordingService().addEntry(envLog);
		        		} catch (RemoteException e) {
		        			if (PrintTrace.TracePrint){
		    					e.printStackTrace();
		    				}
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
	    			if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
	    		}
	     		model.currentGoal = AgentGoal.STAND_UP;
	     	} else {      		
	     		if(me.world.ball.withPlayer && me.world.playerWithBall.team != me.team){
	        		try {
	        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
	        			facade.getLogRecordingService().addEntry(envLog);
	        		} catch (RemoteException e) {
	        			if (PrintTrace.TracePrint){
	    					e.printStackTrace();
	    				}
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
	    			if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
	    		}
	     		model.currentGoal = AgentGoal.STAND_UP;
	     	} else {
	     		if(me.world.ball.withPlayer && me.world.playerWithBall.team != me.team){
	        		try {
	        			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
	        			facade.getLogRecordingService().addEntry(envLog);
	        		} catch (RemoteException e) {
	        			if (PrintTrace.TracePrint){
	    					e.printStackTrace();
	    				}
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
	    			if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
	    		}
	     		model.currentGoal = AgentGoal.STAND_UP;
	     	} else if(me.hasBall){
	    		try {
	    			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
	    			facade.getLogRecordingService().addEntry(envLog);
	    		} catch (RemoteException e) {
	    			if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
	    		}
	     		model.currentGoal = AgentGoal.GO_TO_GOAL;
	     	} else {
	    		try {
	    			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Go to ball");
	    			facade.getLogRecordingService().addEntry(envLog);
	    		} catch (RemoteException e) {
	    			if (PrintTrace.TracePrint){
						e.printStackTrace();
					}
	    		}
	     		model.currentGoal = AgentGoal.GO_TO_BALL;
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
        		model.currentGoal = AgentGoal.INITIALIZATION;
        } else {
        	if (model.myID != -1 && model.environmentStateModel != null) {
        		model.gameStarted = true;
        	}
        }
	}
	
	private void initialization(AgentWorldModel model){
		model.currentGoal = AgentGoal.SET_ROLES;
		model.cycleLastMsg = ((World)model.environmentStateModel).currentCycle;
	}
	
	private void setRoles(AgentWorldModel model){
		Vector<Message> acts = ((World)model.environmentStateModel).actions;

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

						//TODO proximo objetivo
						//model.currentGoal = AgentGoal.GO_TO_BALL;
						model.currentGoal = AgentGoal.GO_TO_INIT_COORD;
					}
				}
			}
		}	
		
		if(((World)model.environmentStateModel).currentCycle - model.cycleLastMsg > 10){
			model.currentGoal = AgentGoal.INITIALIZATION;
		}
	}
	
	private void goToInitCoord(AgentWorldModel model){
		Player me = model.getMe();
		
		if(MathGeometry.calculeDistancePoints(me.s.x, model.posCoord.x, me.s.y, model.posCoord.y) < PositionCoord.maxArea) {
			model.currentGoal = AgentGoal.GO_TO_BALL;
		}
	}
	
	private void goToBall(AgentWorldModel model){
		Player me = model.getMe();
        if(!me.inGround){
            if (MathGeometry.calculeDistancePoints(me.s.x, me.world.ball.s.x, me.s.y, me.world.ball.s.y) < 5000) {
                if(!me.world.ball.withPlayer){
            		try {
            			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Catch the ball");
            			facade.getLogRecordingService().addEntry(envLog);
            		} catch (RemoteException e) {
            			if (PrintTrace.TracePrint){
        					e.printStackTrace();
        				}
            		}
                    model.currentGoal = AgentGoal.CATCH_BALL;				
                }else if(me.world.playerWithBall != null){
                	if(me.world.playerWithBall.team == me.team){
                		try {
                			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Ball with team. Do nothing");
                			facade.getLogRecordingService().addEntry(envLog);
                		} catch (RemoteException e) {
                			if (PrintTrace.TracePrint){
            					e.printStackTrace();
            				}
                		}
                	}else{
                		try {
                			GoalUpdateLogEntry envLog = new GoalUpdateLogEntry(me.world.currentCycle, me.id, "Tackle the Player");
                			facade.getLogRecordingService().addEntry(envLog);
                		} catch (RemoteException e) {
                			if (PrintTrace.TracePrint){
            					e.printStackTrace();
            				}
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
    			if (PrintTrace.TracePrint){
					e.printStackTrace();
				}
    		}
        	model.currentGoal = AgentGoal.STAND_UP;
        }
	}

}
