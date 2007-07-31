package testfield.environment;

public class Test {
	public static void main(String[] args) {
		EnvironmentRamification ram = new EnvironmentRamification();
		ram.apply(ram.state_power_3, 1, 0, 0);
		ram.apply(ram.state_power_3, 2, 0, 0);
		ram.apply(ram.state_position_3, 1, 10, 10);
		ram.apply(ram.state_speed_3, 1, 1, 1);
	
		ram.solve();
		
		System.out.println("" + ram.toString());
	}
}
