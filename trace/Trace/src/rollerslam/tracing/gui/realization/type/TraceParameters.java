/**
 * 
 */
package rollerslam.tracing.gui.realization.type;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Rafael Oliveira
 * 
 */
public class TraceParameters{
	
	private static Object[] scoreBoxParameters = new Object[] {		
		"ScoreBox", 
				"dash", "Shoot", "Pass", "Try", "ManHandling", "ShootStreak", "LooseBall", "Steal", "CouterTackle", "WinBall", "KeepBall", "Divert", "Screem", "Block", "StolenPass", "Tackle", "BallHandling"};
	
	private static Object[] prologParameters = new Object[] {		
		"Prolog",
			new Object[] { "BasicFCBIP", 
				"holds/2", "equals/2", "plus/3", "minus/3", "update/4", "state_update/4", "do/3", "poss/2", "not_poss/2", "non_executable/2"},  
			new Object[] { "MetaFCBIP",
				"state_update/3", "kwhether/3", "is_fluent/1", "init/1", "result/4", "do/5", "primitive_action/1", "non_executable/3"},
			new Object[] { "RamyfyingFCBIP",
				"causes/3", "causes/6", "not_causes/3", "ramify/4"},
			new Object[] { "ConcurrentFCBIP",
				"dir_effect/4"},
			new Object[] { "DomPred",
				"..."}};
	
	private static Object[] CHRParameters = new Object[] {		
		"CHR",
			new Object[] { "FCCHR", 
				"neq/2", "neq_all/3", "not_holds/2", "not_holds_all/3", "duplicate_free/2"},  
			new Object[] { "DomCHR",
				"BICS", "RDCS"}};	

	private static Object[] simInfraParamenters = new Object[] {
		"Simulation Infrastructure",
			"InterfaceOpName", "InterfaceOpParam", "InterfaceOpResult", "InterfaceOpCalled"  };
	
	public static DefaultMutableTreeNode getPlayerLogParametersAsTreeNode() {
		return processHierarchy(new Object[] {"Parameters", CHRParameters, prologParameters, scoreBoxParameters});
	}
	
	public static DefaultMutableTreeNode getSimInfraParametersAsTreeNode() {
		return processHierarchy(new Object[] {"Parameters", simInfraParamenters, scoreBoxParameters});
	}	

	public static DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
		DefaultMutableTreeNode node = new DefaultMutableTreeNode(hierarchy[0]);
		DefaultMutableTreeNode child;
		for (int i = 1; i < hierarchy.length; i++) {
			Object nodeSpecifier = hierarchy[i];
			if (nodeSpecifier instanceof Object[] // Ie node with children
			) {
				child = processHierarchy((Object[]) nodeSpecifier);
			} else {
				child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf
			}
			node.add(child);
		}
		return node;
	}

	public static Object[] getScoreBoxParameters() {
		return scoreBoxParameters;
	}

	public static void setScoreBoxParameters(Object[] scoreBoxParameters) {
		TraceParameters.scoreBoxParameters = scoreBoxParameters;
	}

	public static Object[] getPrologParameters() {
		return prologParameters;
	}

	public static void setPrologParameters(Object[] prologParameters) {
		TraceParameters.prologParameters = prologParameters;
	}

	public static Object[] getCHRParameters() {
		return CHRParameters;
	}

	public static void setCHRParameters(Object[] parameters) {
		CHRParameters = parameters;
	}

	public static Object[] getSimInfraParamenters() {
		return simInfraParamenters;
	}

	public static void setSimInfraParamenters(Object[] simInfraParamenters) {
		TraceParameters.simInfraParamenters = simInfraParamenters;
	}

}
