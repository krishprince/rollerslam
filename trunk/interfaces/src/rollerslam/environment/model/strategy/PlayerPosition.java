package rollerslam.environment.model.strategy;

public enum PlayerPosition {
	GOALKEEPER(0),
	
	//BACKS
	ENDBACK(1),
	RIGHTBACK(2),
	LEFTBACK(3),
	CENTERBACK(4),
	FREEBACKS1(5),
	FREEBACKS2(6),
	
	//MIDFIELDER
	RIGHTTRACKER(7),
	HALFBACK(8),
	CENTERTRACKER(9),
	HALFFORWARD(10),
	LEFTTRACKER(11),
	ROVER1(12),
	ROVER2(13),
	
	//FORWARDS
	RIGHTFORWARD(14),
	CENTERFORWARD(15),
	LEFTFORWARD(16),
	FREEFORWARD1(17),
	FREEFORWARD2(18),
	ENDFORWARD(19);

	private final int value;
	PlayerPosition(int value) {
        this.value = value;
    }
    public int getValue() {
        return this.value;
    }
}