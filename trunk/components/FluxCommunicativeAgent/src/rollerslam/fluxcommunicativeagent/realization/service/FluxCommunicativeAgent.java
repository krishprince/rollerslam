package rollerslam.fluxcommunicativeagent.realization.service;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import rollerslam.agent.communicative.realization.service.CommunicativeAgentImpl;
import rollerslam.agent.communicative.specification.type.action.AskAction;
import rollerslam.agent.communicative.specification.type.action.AskAllAction;
import rollerslam.agent.communicative.specification.type.object.OOState;
import rollerslam.agent.communicative.specification.type.object.WorldObject;
import rollerslam.fluxcommunicativeagent.realization.type.FluxAction;
import rollerslam.fluxcommunicativeagent.realization.type.FluxAgentID;
import rollerslam.fluxcommunicativeagent.realization.type.FluxOID;
import rollerslam.fluxcommunicativeagent.realization.type.FluxOOState;
import rollerslam.fluxinferenceengine.realization.service.EclipsePrologFluxEngine;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluent;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluxAction;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluxSpecification;
import rollerslam.fluxinferenceengine.specification.service.FluxInferenceEngine;
import rollerslam.fluxinferenceengine.specification.service.type.ReasoningException;
import rollerslam.fluxinferenceengine.specification.type.Action;
import rollerslam.fluxinferenceengine.specification.type.Fluent;
import rollerslam.fluxinferenceengine.specification.type.FluxSpecification;
import rollerslam.fluxinferenceengine.specification.type.State;
import rollerslam.infrastructure.specification.service.Agent;
import rollerslam.infrastructure.specification.service.Message;
import rollerslam.infrastructure.specification.type.AgentID;

import com.parctechnologies.eclipse.Atom;
import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.CompoundTermImpl;

// TODO remove this dependency to Eclipse Prolog LIB's
public class FluxCommunicativeAgent extends CommunicativeAgentImpl {

	private FluxInferenceEngine inferenceEngine;
	private FluxSpecification fluxSpec;

	private State fluxState = new State();

	public FluxCommunicativeAgent(Agent port, File fluxSpec, String agentTerm,
			final long cycleLength, List<Object> initialData) throws Exception {
		super(port, cycleLength);

		this.inferenceEngine = new EclipsePrologFluxEngine();
		this.fluxSpec = new EclipsePrologFluxSpecification(fluxSpec, agentTerm);

		fluxState = inferenceEngine.getReasoningFacade().initializeModel(this.fluxSpec, initialData);
		updateInternalModel(fluxState);
		
		startThread();
	}

	protected void processSpecificMessage(Message message) {
		if (inferenceEngine == null || fluxSpec == null)
			return;
		if (message instanceof FluxAction) {
			try {
				State s = inferenceEngine.getReasoningFacade().processAction(
						fluxSpec, fluxState, ((FluxAction) message).getAction());
				if (s != null) {
					fluxState = s;
				}
			} catch (ReasoningException e) {
				e.printStackTrace();
			}
		}
	}

	// TODO why is gamePhysics being added by default?
	protected Set<Message> processCycle(Set<Message> perceptions) {
		if (inferenceEngine == null || fluxSpec == null)
			return null;

		Set<Message> ret = super.processCycle(perceptions);

		updateFluxState();

		try {
			fluxState = inferenceEngine.getReasoningFacade().updateModel(fluxSpec,
					fluxState);

			updateInternalModel(fluxState);

			Action ac = inferenceEngine.getReasoningFacade().computeNextAction(
					fluxSpec, fluxState);

			if (ac != null) {
				EclipsePrologFluxAction eac = ((EclipsePrologFluxAction) ac);

				if (eac.getActionTerm().functor().equals("ask")) {
					AskAction msg = new AskAction();

					msg.getOids().add(new FluxOID((CompoundTerm) eac.getActionTerm()
							.arg(1)));

					msg.setSender(this.getAgent().getAgentID());
					msg.getReceiver().add(
							new FluxAgentID(new Atom("gamePhysics")));

					ret.add(msg);
				} else if (eac.getActionTerm().functor().equals("askAll")) {
					Message msg = new AskAllAction();

					msg.setSender(this.getAgent().getAgentID());
					msg.getReceiver().add(
							new FluxAgentID(new Atom("gamePhysics")));

					ret.add(msg);
				} else if (eac.getActionTerm().functor().equals("noAction")) {
					
				} else {
					Message msg = new FluxAction(eac);

					msg.setSender(this.getAgent().getAgentID());
					msg.getReceiver().add(
							new FluxAgentID(new Atom("gamePhysics")));

					ret.add(msg);
				}
			}
		} catch (ReasoningException e) {
			e.printStackTrace();
		}
		return ret;
	}

	private void updateInternalModel(State state) {
		OOState newKB = new OOState();
		HashMap<AgentID, OOState> newAgentKB = new HashMap<AgentID, OOState>();

		for (Fluent f : state.getFluents()) {
			EclipsePrologFluent ef = (EclipsePrologFluent) f;

			if (ef.getTerm().functor().equals("k")) {
				FluxAgentID agId = new FluxAgentID((CompoundTerm) ef.getTerm()
						.arg(1));

				OOState as = newAgentKB.get(agId);

				if (as == null) {
					as = new OOState();
				}

				updateOOState(as, new EclipsePrologFluent((CompoundTerm) ef
						.getTerm().arg(2)));
			} else if (ef.getTerm().functor().equals("@")) {
				updateOOState(newKB, ef);
			}
		}
		
		this.setKb(newKB);
		this.setAgentKB(newAgentKB);
	}

	private void updateFluxState() {
		Vector<Fluent> fluents = new Vector<Fluent>();

		for (WorldObject object : this.getKb().getObjects().values()) {
			fluents.addAll(((FluxOOState) object.getState()).getFluents());
		}

		for (AgentID agent : this.getAgentKB().keySet()) {
			for (WorldObject object : ((OOState) this.getAgentKB().get(agent)).getObjects()
					.values()) {
				for (Fluent fluent : ((FluxOOState) object.getState()).getFluents()) {
					fluents.add(new EclipsePrologFluent(new CompoundTermImpl(
							"k", ((FluxAgentID) agent).getTerm(),
							((EclipsePrologFluent) fluent).getTerm())));
				}
			}
		}

		fluxState = new State(fluents.toArray(new Fluent[0]));
	}

	private void updateOOState(OOState newKB, EclipsePrologFluent ef) {
		FluxOID oid = new FluxOID((CompoundTerm) ef.getTerm().arg(1));
		WorldObject obj = newKB.getObjects().get(oid);
		if (obj == null) {
			obj = new WorldObject(oid, new FluxOOState(new HashSet<Fluent>()));
		}

		((FluxOOState) obj.getState()).getFluents().add(ef);

		newKB.getObjects().put(oid, obj);
	}

	protected Fluent makeFluent(FluxOID oid, String att, CompoundTerm value) {
		CompoundTerm vl = new CompoundTermImpl("->", new Atom(att), value);

		EclipsePrologFluent f = new EclipsePrologFluent(new CompoundTermImpl(
				"@", oid.getTerm(), vl));
		return f;
	}

}
