package rollerslam.fluxcommunicativeagent.realization.service;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
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
			final long cycleLength) throws Exception {
		super(port, cycleLength);

		this.inferenceEngine = new EclipsePrologFluxEngine();
		this.fluxSpec = new EclipsePrologFluxSpecification(fluxSpec, agentTerm);

		startThread();
	}

	protected void processSpecificMessage(Message message) {
		if (inferenceEngine == null || fluxSpec == null)
			return;

		if (message instanceof FluxAction) {
			try {
				State s = inferenceEngine.getReasoningFacade().processAction(
						fluxSpec, fluxState, ((FluxAction) message).action);
				if (s != null) {
					fluxState = s;
				}
			} catch (ReasoningException e) {
				e.printStackTrace();
			}
		}
	}

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

					msg.oids.add(new FluxOID((CompoundTerm) eac.getActionTerm()
							.arg(1)));

					msg.setSender(this.agent.getAgentID());
					msg.getReceiver().add(
							new FluxAgentID(new Atom("gamePhysics")));

					ret.add(msg);
				} else if (eac.getActionTerm().functor().equals("askAll")) {
					Message msg = new AskAllAction();

					msg.setSender(this.agent.getAgentID());
					msg.getReceiver().add(
							new FluxAgentID(new Atom("gamePhysics")));

					ret.add(msg);
				} else {
					Message msg = new FluxAction(eac);

					msg.setSender(this.agent.getAgentID());
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
	}

	private void updateOOState(OOState newKB, EclipsePrologFluent ef) {
		FluxOID oid = new FluxOID((CompoundTerm) ef.getTerm().arg(1));
		WorldObject obj = newKB.objects.get(oid);
		if (obj == null) {
			obj = new WorldObject(oid, new FluxOOState(new HashSet<Fluent>()));
		}

		((FluxOOState) obj.state).fluents.add(ef);

		newKB.objects.put(oid, obj);
	}

	private void updateFluxState() {
		Vector<Fluent> fluents = new Vector<Fluent>();

		for (WorldObject object : kb.objects.values()) {
			fluents.addAll(((FluxOOState) object.state).fluents);
		}

		for (AgentID agent : agentKB.keySet()) {
			for (WorldObject object : ((OOState) agentKB.get(agent)).objects
					.values()) {
				for (Fluent fluent : ((FluxOOState) object.state).fluents) {
					fluents.add(new EclipsePrologFluent(new CompoundTermImpl(
							"k", ((FluxAgentID) agent).term,
							((EclipsePrologFluent) fluent).getTerm())));
				}
			}
		}

		fluxState = new State(fluents.toArray(new Fluent[0]));
	}

	protected Fluent makeFluent(FluxOID oid, String att, CompoundTerm value) {
		CompoundTerm vl = new CompoundTermImpl("->", new Atom(att), value);

		EclipsePrologFluent f = new EclipsePrologFluent(new CompoundTermImpl(
				"@", oid.term, vl));
		return f;
	}

	protected void declareFAtom(FluxOID oid, String attributeName,
			String attributeValue) {

		WorldObject wobject = kb.objects.get(oid);

		if (wobject == null) {
			wobject = new WorldObject(oid, new FluxOOState(
					new HashSet<Fluent>()));
			kb.objects.put(oid, wobject);
		}

		FluxOOState wostate = (FluxOOState) wobject.state;

		wostate.fluents.add(makeFluent(oid, attributeName, new Atom(
				attributeValue)));
	}

	protected void declareFAtom(FluxOID oid, String attributeName,
			CompoundTermImpl attributeValue) {

		WorldObject wobject = kb.objects.get(oid);

		if (wobject == null) {
			wobject = new WorldObject(oid, new FluxOOState(
					new HashSet<Fluent>()));
			kb.objects.put(oid, wobject);
		}

		FluxOOState wostate = (FluxOOState) wobject.state;

		wostate.fluents.add(makeFluent(oid, attributeName, attributeValue));
	}

}
