package rollerslam.agent.referee;


import java.io.File;

import rollerslam.environment.model.Fact;
import rollerslam.environment.model.actions.UpdateScoreAction;
import rollerslam.environment.model.actions.voice.SendMsgAction;
import rollerslam.infrastructure.agent.Agent;
import rollerslam.infrastructure.agent.Message;
import rollerslam.infrastructure.agent.StateMessage;
import rollerslam.infrastructure.agent.automata.ActionInterpretationComponent;
import rollerslam.infrastructure.agent.automata.AutomataAgent;
import rollerslam.infrastructure.agent.automata.EnvironmentStateModel;
import rollerslam.infrastructure.agent.automata.ModelBasedBehaviorStrategyComponent;
import rollerslam.infrastructure.agent.automata.ModelInitializationComponent;
import rollerslam.infrastructure.client.ClientFacade;
import rollerslam.infrastructure.client.ClientFacadeImpl;
import rollerslam.infrastructure.client.ClientInitialization;
import rollerslam.infrastructure.server.PrintTrace;
import rollerslam.infrastructure.settings.GeneralSettings;
import rollerslam.infrastructure.settings.GeneralSettingsImpl;

import com.parctechnologies.eclipse.EclipseConnection;

@SuppressWarnings("serial")

public class RefereeAgent extends AutomataAgent {

    public ClientFacade facade = null;
    public Agent remoteThis = null;

    public EclipseConnection eclipse;

    public RefereeAgent() throws Exception {
    	this("localhost");
    }
    public RefereeAgent(String nameServer) throws Exception {
        facade = ClientFacadeImpl.getInstance();

        ClientInitialization clientInitialization = facade.getClientInitialization();
        clientInitialization.init(nameServer);

        initializeEclipseConnection();
        remoteThis = (Agent) clientInitialization.exportObject(this);

        facade.getAgentRegistry().register(remoteThis);

        this.sensor = facade.getAgentSensor(remoteThis);
        this.effector = facade.getAgentEffector(remoteThis);

        this.worldModel = new RefereeWorldModel(null);

        this.initializationComponent = new ModelInitializationComponent() {

            public void initialize(EnvironmentStateModel model) {
                ((RefereeWorldModel) worldModel).setMessage(new UpdateScoreAction(remoteThis));
            }
        };

        this.interpretationComponent = new ActionInterpretationComponent() {

            public void processAction(EnvironmentStateModel w, Message m) {
                if (m instanceof StateMessage) {
                    ((RefereeWorldModel) w).setEnvironmentStateModel(((StateMessage) m).model);
                }
            }
        };

        this.ramificationComponent = new RefereeRamificator(eclipse);

        this.strategyComponent = new ModelBasedBehaviorStrategyComponent() {

            public Message computeAction(EnvironmentStateModel w) {
                try {
                    if (w instanceof RefereeWorldModel) {

                        RefereeWorldModel refereeWorld = (RefereeWorldModel) w;
                        if (refereeWorld.needsToSayGoal) {
                            refereeWorld.needsToSayGoal = false;
                            return refereeWorld.getMessage();
                        } else {
                            if (refereeWorld.goalFact != null) {
                                Fact fact = refereeWorld.goalFact;
                                refereeWorld.goalFact = null;

                                return new SendMsgAction(fact);
                            } else {
                                return null;
                            }
                        }
                    }
                    return null;
                } catch (Exception e) {
                    if (PrintTrace.TracePrint) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };

        this.run();
        startSimulation();
    }
    
    private void initializeEclipseConnection() throws Exception {
        String folder = (String)GeneralSettingsImpl.getInstance().getSetting(GeneralSettings.FLUX_CODE_HOME); 
        
        eclipse = facade.getClientInitialization().getEclipseConnection();
        File eclipseProgram = new File(folder + "referee.pl");
        eclipse.compile(eclipseProgram);
    }
    public static void main(String[] args) throws Exception {
        new RefereeAgent();
    }
}
