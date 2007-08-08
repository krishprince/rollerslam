package rollerslam.agent.goalbased;

import java.rmi.RemoteException;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.World;
import rollerslam.environment.model.actions.leg.DashAction;
import rollerslam.environment.model.utils.MathGeometry;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalUpdateComponent;

public class AgentGoalUpdater implements GoalUpdateComponent {
        private Agent remoteThis;
        
        public AgentGoalUpdater(Agent remoteThis){
            this.remoteThis = remoteThis;
        }
        
	public void updateGoal(GoalBasedEnvironmentStateModel goal) {
		AgentWorldModel model = (AgentWorldModel) goal;
		
		if (model.currentGoal == AgentGoal.JOIN_GAME) {
			if (model.joinMessageSent) {
                model.currentGoal = AgentGoal.WAIT_JOIN_GAME;
			}
		} else if (model.currentGoal == AgentGoal.WAIT_JOIN_GAME) {
            if (model.gameStarted) {
                Player me = getMeFromModel(model);

                if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
                	System.out.println("BALL WITH TEAM - DO NOTHING - " + me.id);
                }else{
                	System.out.println("GO TO BALL - " + me.id);
                	model.currentGoal = AgentGoal.GO_TO_BALL;
                }
                	
            }			
		} else if (model.currentGoal == AgentGoal.GO_TO_BALL) { 
            Player me = getMeFromModel(model);
            if(!me.inGround){
                if (MathGeometry.calculeDistancePoints(me.s.x, me.world.ball.s.x, me.s.y, me.world.ball.s.y) < 5000) {
                    if(!me.world.ball.withPlayer){
                        System.out.println("CATCH THE BALL - " + me.id);
                        model.currentGoal = AgentGoal.CATCH_BALL;				
                    }else if(me.world.playerWithBall != null){
                    	if(me.world.playerWithBall.team == me.team){
                    		System.out.println("BALL WITH TEAM - DO NOTHING - " + me.id);
                    	}else{
                    		System.out.println("TACKLE THE PLAYER - " + me.id);
                    		model.currentGoal = AgentGoal.TACKLE_PLAYER;
                    	}
                    }
                }
            } else {
            	System.out.println("STAND UP - " + me.id);
            	model.currentGoal = AgentGoal.STAND_UP;
            }
        } else if(model.currentGoal == AgentGoal.CATCH_BALL){
            Player me = getMeFromModel(model);
            if(me.hasBall){
                System.out.println("GO TO GOAL - " + me.id);
                model.currentGoal = AgentGoal.GO_TO_GOAL;
            }else{
            	if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
                	System.out.println("BALL WITH TEAM - DO NOTHING - " + me.id);
                }else{
                	System.out.println("GO TO BALL - " + me.id);
                	model.currentGoal = AgentGoal.GO_TO_BALL;
                }
            }
        } else if(model.currentGoal == AgentGoal.TACKLE_PLAYER) {
            Player me = getMeFromModel(model);
            
            if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
            	System.out.println("BALL WITH TEAM - DO NOTHING - " + me.id);
            }else{
            	System.out.println("GO TO BALL - " + me.id);
            	model.currentGoal = AgentGoal.GO_TO_BALL;
            }
        } else if(model.currentGoal == AgentGoal.STAND_UP) {
        	Player me = getMeFromModel(model);
        	
        	if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
            	System.out.println("BALL WITH TEAM - DO NOTHING - " + me.id);
            }else{
            	System.out.println("GO TO BALL - " + me.id);
            	model.currentGoal = AgentGoal.GO_TO_BALL;
            }
        } else if(model.currentGoal == AgentGoal.GO_TO_GOAL){
        	Player me = getMeFromModel(model);
        	if(me.inGround){
        		System.out.println("STAND UP - " + me.id);
        		model.currentGoal = AgentGoal.STAND_UP;
        	} else {
        		if(me.team == PlayerTeam.TEAM_A){
    				if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalB.s.x, me.s.y, me.world.goalB.s.y) < 30000) {
    					System.out.println("THROW - " + me.id);
    	        		model.currentGoal = AgentGoal.THROW_BALL;
    				}
    			}else{
    				if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalA.s.x, me.s.y, me.world.goalA.s.y) < 30000) {
    					System.out.println("THROW - " + me.id);
    	        		model.currentGoal = AgentGoal.THROW_BALL;    					
    				}
    			}        	
        	}
         } else if(model.currentGoal == AgentGoal.THROW_BALL){
        	 Player me = getMeFromModel(model);
        	 if(me.inGround){
         		System.out.println("STAND UP - " + me.id);
         		model.currentGoal = AgentGoal.STAND_UP;
         	} else {
         		if(me.world.ball.withPlayer && me.world.playerWithBall.team != me.team){
         			System.out.println("GO TO BALL - " + me.id);
                	model.currentGoal = AgentGoal.GO_TO_BALL;
         		}
         	}
         }
	}

	private Player getMeFromModel(AgentWorldModel model) {
		for (Player player : ((World)model.environmentStateModel).playersA) {
			//if (player.id == model.myID) {
                    try{
                        if(player.id == remoteThis.getID()){
				return player;
			}
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
		}

		for (Player player : ((World)model.environmentStateModel).playersB) {
			//if (player.id == model.myID) {
                    try{
                        if(player.id == remoteThis.getID()){
				return player;
			}
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
		}

		return null;
	}
	
}
