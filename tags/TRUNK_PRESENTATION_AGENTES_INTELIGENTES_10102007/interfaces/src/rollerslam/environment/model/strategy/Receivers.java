package rollerslam.environment.model.strategy;

public enum Receivers {
    
    //read team
    COACH_A("COACH_A"),
    COACH_B("COACH_B"),
    TEAM_A("TEAM_A"),
    TEAM_B("TEAM_B");
    
    private final String value;
    Receivers(String value) {
        this.value = value;
    }
    public String getValue() {
        return this.value;
    }
}
