package rollerslam.agent.goalbased;

import rollerslam.environment.model.Player;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.environment.model.utils.MathGeometry;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalUpdateComponent;

public class AgentGoalUpdater implements GoalUpdateComponent {
        @SuppressWarnings("unused")
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
            	System.out.println("GO TO BALL ");
            	model.currentGoal = AgentGoal.GO_TO_BALL;
            }			
		} else if (model.currentGoal == AgentGoal.GO_TO_BALL) { 
			Player me = model.getMe();
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
	                System.out.println("COUNTERTACKLE - " + me.id);
	                model.currentGoal = AgentGoal.COUNTER_TACKLE;
            	}else{
	                System.out.println("GO TO GOAL - " + me.id);
	                model.currentGoal = AgentGoal.GO_TO_GOAL;
            	}
            }else{
            	if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
                	System.out.println("BALL WITH TEAM - DO NOTHING - " + me.id);
                }else{
                	System.out.println("GO TO BALL - " + me.id);
                	model.currentGoal = AgentGoal.GO_TO_BALL;
                }
            }
        } else if(model.currentGoal == AgentGoal.TACKLE_PLAYER) {
			Player me = model.getMe();
            
            if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
            	System.out.println("BALL WITH TEAM - DO NOTHING - " + me.id);
            }else{
            	System.out.println("GO TO BALL - " + me.id);
            	model.currentGoal = AgentGoal.GO_TO_BALL;
            }
        } else if(model.currentGoal == AgentGoal.STAND_UP) {
			Player me = model.getMe();
        	
        	if(me.world.playerWithBall != null && me.world.playerWithBall.team == me.team){
            	System.out.println("BALL WITH TEAM - DO NOTHING - " + me.id);
            }else{
            	System.out.println("GO TO BALL - " + me.id);
            	model.currentGoal = AgentGoal.GO_TO_BALL;
            }
        } else if(model.currentGoal == AgentGoal.GO_TO_GOAL){
			Player me = model.getMe();
        	if(me.inGround){
        		System.out.println("STAND UP - " + me.id);
        		model.currentGoal = AgentGoal.STAND_UP;
        	} else {
        		if(me.team == PlayerTeam.TEAM_A){
    				if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalB.s.x, me.s.y, me.world.goalB.s.y) < 30000) {
    					System.out.println("THROW - " + me.id);
    	        		model.currentGoal = AgentGoal.THROW_BALL;
    				}else if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalB.s.x, me.s.y, me.world.goalB.s.y) < 50000) {
    					System.out.println("KICK - " + me.id);
    	        		model.currentGoal = AgentGoal.KICK_BALL;
    				}
    			}else{
    				if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalA.s.x, me.s.y, me.world.goalA.s.y) < 30000) {
    					System.out.println("THROW - " + me.id);
    	        		model.currentGoal = AgentGoal.THROW_BALL;    					
    				}if(MathGeometry.calculeDistancePoints(me.s.x, me.world.goalA.s.x, me.s.y, me.world.goalA.s.y) < 50000) {
    					System.out.println("KICK - " + me.id);
    	        		model.currentGoal = AgentGoal.KICK_BALL;
    				}
    			}        	
        	}
         } else if(model.currentGoal == AgentGoal.THROW_BALL){
 			Player me = model.getMe();
        	 if(me.inGround){
         		System.out.println("STAND UP - " + me.id);
         		model.currentGoal = AgentGoal.STAND_UP;
         	} else {
         		if(me.world.ball.withPlayer && me.world.playerWithBall.team != me.team){
         			System.out.println("GO TO BALL - " + me.id);
                	model.currentGoal = AgentGoal.GO_TO_BALL;
         		}
         	}
         } else if(model.currentGoal == AgentGoal.KICK_BALL){
        	 Player me = model.getMe();
        	 if(me.inGround){
         		System.out.println("STAND UP - " + me.id);
         		model.currentGoal = AgentGoal.STAND_UP;
         	} else {
         		if(me.world.ball.withPlayer && me.world.playerWithBall.team != me.team){
         			System.out.println("GO TO BALL - " + me.id);
                	model.currentGoal = AgentGoal.GO_TO_BALL;
         		}
         	}
         } else if(model.currentGoal == AgentGoal.COUNTER_TACKLE){
        	 Player me = model.getMe();
        	 if(me.inGround){
         		System.out.println("STAND UP - " + me.id);
         		model.currentGoal = AgentGoal.STAND_UP;
         	} else if(me.hasBall){
         		System.out.println("GO TO GOAL - " + me.id);
         		model.currentGoal = AgentGoal.GO_TO_GOAL;
         	} else {
         		System.out.println("GO TO BALL - " + me.id);
         		model.currentGoal = AgentGoal.GO_TO_BALL;
         	}
         }
	}
	
}
