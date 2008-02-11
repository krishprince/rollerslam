package rollerslam.agent.goalbased;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import rollerslam.environment.model.perceptions.GameStartedPerception;
import rollerslam.environment.model.PlayerTeam;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.agent.automata.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelInitializationComponent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedAgent;
import rollerslam.infrastructure.agent.goalbased.GoalBasedEnvironmentStateModel;
import rollerslam.infrastructure.agent.goalbased.GoalInitializationComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;

@SuppressWarnings(value = "serial")
public class RollerslamGoalBasedAgent extends GoalBasedAgent {

    public ClientFacade facade = null;
    public Agent remoteThis = null;

    public RollerslamGoalBasedAgent(final PlayerTeam team) throws Exception {
        facade = ClientFacadeImpl.getInstance();
        remoteThis = (Agent) facade.getClientInitialization().exportObject(this);
        facade.getAgentRegistry().register(remoteThis);

        this.sensor = facade.getAgentSensor(remoteThis);
        this.effector = facade.getAgentEffector(remoteThis);

        this.worldModel = new AgentWorldModel(null);

        this.goalInitializationComponent = new GoalInitializationComponent() {

            public void initialize(GoalBasedEnvironmentStateModel goal) {
                ((AgentWorldModel) goal).currentGoal = AgentGoal.JOIN_GAME;
                ((AgentWorldModel) goal).myTeam = team;
            }
        };

        this.goalUpdateComponent = new AgentGoalUpdater(remoteThis);

        this.initializationComponent = new ModelInitializationComponent() {

            public void initialize(EnvironmentStateModel model) {
                ((AgentWorldModel) model).gameStarted = false;
            }
        };

        this.interpretationComponent = new ActionInterpretationComponent() {

            public void processAction(EnvironmentStateModel w, Message m) {
                if (m instanceof StateMessage) {
                    ((AgentWorldModel) w).environmentStateModel = ((StateMessage) m).model;
                } else if (m instanceof GameStartedPerception) {
                    if (((GameStartedPerception) m).receiver.equals(remoteThis)) {
                        ((AgentWorldModel) w).myID = ((GameStartedPerception) m).playerID;
                    }
                }
            }
        };

        this.ramificationComponent = new AgentRamificator();

        this.strategyComponent = new AgentActionGenerator(remoteThis);

        this.run();
        startSimulation();
    }

    public static void main(String... args) throws Exception {
        PlayerTeam team = PlayerTeam.TEAM_A;

        String teamStr = "";
        String hostStr = "";

        if (args.length == 0) {
            JComboBox jcb = new JComboBox(new String[]{"A", "B"});
            JOptionPane.showMessageDialog(null, jcb, "Which team? [A | B]", JOptionPane.QUESTION_MESSAGE);
            teamStr = (String)jcb.getSelectedItem();
            hostStr = JOptionPane.showInputDialog("Host?", "auto");
        } else {
            teamStr = args[0].toUpperCase();
            hostStr = args[1];
        }

        if (teamStr.equals("A")) {
            team = PlayerTeam.TEAM_A;
        } else {
            team = PlayerTeam.TEAM_B;
        }

        if (hostStr.toLowerCase().equals("auto")) {
            ClientFacadeImpl.getInstance().getClientInitialization().init();
        } else {
            ClientFacadeImpl.getInstance().getClientInitialization().init(hostStr);
        }

        new RollerslamGoalBasedAgent(team);
    }
}