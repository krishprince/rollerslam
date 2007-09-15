package rollerslam.environment.model;

import rollerslam.infrastructure.settings.GeneralSettings;
import rollerslam.infrastructure.settings.GeneralSettingsImpl;

public class SimulationSettings {

	public static final int BALL_HEIGHT = 500;
	public static final int BALL_WIDTH = 500;
	public static final int BASKET_WIDTH  = 500;
	public static final int BASKET_HEIGHT = 500;
	public static final int GOAL_WIDTH  = 200;
	public static final int GOAL_HEIGHT = 7000;
	public static final int OUTTRACK_WIDTH  = 188000;
	public static final int OUTTRACK_HEIGHT = 138000;
	public static final int PLAYER_WIDTH  = 1000;
	public static final int PLAYER_HEIGHT = 1000;
	public static final int DISTANCE_BETWEEN_BASKETS = 3500;
	public static final int RAMP_WIDTH  = 30000;
	public static final int RAMP_HEIGHT = 30000;
	public static final int TRAMPOLINE_WIDTH  = 23000;
	public static final int TRAMPOLINE_HEIGHT = 23000;
	public static final int GOAL_A_Y = 0;
	public static final int GOAL_A_X = -64350;
	public static final int FOCUS1X = -63835;
	public static final int FOCUS1Y = 0;
	public static final int FOCUS2X = 63835;
	public static final int FOCUS2Y = 0;

	public static final int PLAYERS_PER_TEAM;
        
        static {
            try {
                PLAYERS_PER_TEAM = Integer.parseInt((String)GeneralSettingsImpl.getInstance().getSetting(GeneralSettings.PLAYERS_PER_TEAM));
            } catch (Exception err) {
                throw new RuntimeException("Error setting SimulationSettings. Details: " + err, err);
            }
        }

	public static final int MAX_VELOCITY = 1000;
	public static final int MAX_DISTANCE = 5000;
	public static final int MAX_ACCELERATION = 300;
	
}
