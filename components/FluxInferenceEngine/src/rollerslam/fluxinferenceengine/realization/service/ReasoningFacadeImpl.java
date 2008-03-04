package rollerslam.fluxinferenceengine.realization.service;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import rollerslam.fluxinferenceengine.realization.eclipseprolog.EclipsePrologHandlerImpl;
import rollerslam.fluxinferenceengine.realization.flux.ADDRESS_FluxFiles;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluent;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluxAction;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluxSpecification;
import rollerslam.fluxinferenceengine.specification.service.ReasoningFacade;
import rollerslam.fluxinferenceengine.specification.service.type.ReasoningException;
import rollerslam.fluxinferenceengine.specification.type.Action;
import rollerslam.fluxinferenceengine.specification.type.Fluent;
import rollerslam.fluxinferenceengine.specification.type.FluxSpecification;
import rollerslam.fluxinferenceengine.specification.type.State;

import com.parctechnologies.eclipse.CompoundTerm;
import com.parctechnologies.eclipse.CompoundTermImpl;
import com.parctechnologies.eclipse.EclipseConnection;
import com.parctechnologies.eclipse.Fail;

public class ReasoningFacadeImpl extends ReasoningFacade {
	private static EclipseConnection connection = EclipsePrologHandlerImpl
			.getInstance().getEclipseConnection();

	private Set<EclipsePrologFluxSpecification> compiledSpecs = new HashSet<EclipsePrologFluxSpecification>();

	static {
		try {
			connection.compile(new File(ADDRESS_FluxFiles.ADDRESS_FLUENT_FILE));
			connection.compile(new File(ADDRESS_FluxFiles.ADDRESS_FLUX_FILE));
			connection.compile(new File(ADDRESS_FluxFiles.ADDRESS_OOFLUX_FILE));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void compileSpec(EclipsePrologFluxSpecification spec)
			throws Exception {
		if (!compiledSpecs.contains(spec)) {
			connection.compile(spec.getFluxFile());
			compiledSpecs.add(spec);
		}
	}

	protected State getGenericState(Object object) {
		Collection estate = (Collection) object;
		State gstate = null;
		Vector<Fluent> fluents = new Vector<Fluent>();

		for (Object oterm : estate) {
			CompoundTerm term = (CompoundTerm) oterm;
			fluents.add(new EclipsePrologFluent(term));
		}

		gstate = new State(fluents.toArray(new Fluent[0]));
		return gstate;
	}

	protected Collection getEclipsePrologState(Fluent[] fluents) {
		Vector<CompoundTermImpl> estate = new Vector<CompoundTermImpl>();

		for (int i = 0; i < fluents.length; ++i) {
			estate
					.add((CompoundTermImpl) ((EclipsePrologFluent) fluents[i]).getTerm());
		}

		return estate;
	}
	
	@Override
	public Action computeNextAction(FluxSpecification spec, State state)
			throws ReasoningException {
		if (spec instanceof EclipsePrologFluxSpecification) {
			EclipsePrologFluxSpecification espec = (EclipsePrologFluxSpecification) spec;

			try {
				compileSpec(espec);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			Collection col = getEclipsePrologState(state.getFluents());

			try {
				CompoundTerm ret = connection.rpc(espec.getAgentName()
						+ "ComputeNextAction", col, null);
				return new EclipsePrologFluxAction((CompoundTerm) ret.arg(2));
			} catch (Fail e) {
				return null;
			} catch (Exception e) {
				throw new ReasoningException(e);
			}
		}
		return null;
	}

	@Override
	public State updateModel(FluxSpecification spec, State state)
			throws ReasoningException {
		if (spec instanceof EclipsePrologFluxSpecification) {
			EclipsePrologFluxSpecification espec = (EclipsePrologFluxSpecification) spec;

			try {
				compileSpec(espec);
			} catch (Exception e) {
				throw new ReasoningException(e);
			}
			Collection col = getEclipsePrologState(state.getFluents());

			try {
				CompoundTerm ret = connection.rpc(espec.getAgentName()
						+ "UpdateModel", col, null);
				return getGenericState(ret.arg(2));
			} catch (Fail e) {
				return state;
			} catch (Exception e) {
				throw new ReasoningException(e);
			}

		}
		return state;
	}

	@Override
	public State processAction(FluxSpecification spec, State state,
			Action action) throws ReasoningException {
		if (spec instanceof EclipsePrologFluxSpecification
				&& action instanceof EclipsePrologFluxAction) {

			EclipsePrologFluxSpecification espec = (EclipsePrologFluxSpecification) spec;
			EclipsePrologFluxAction eaction = (EclipsePrologFluxAction) action;

			try {
				compileSpec(espec);
				Collection col = getEclipsePrologState(state.getFluents());

				CompoundTerm ret = connection.rpc(espec.getAgentName()
						+ "ProcessAction", col, eaction.getActionTerm(), null);
				return getGenericState(ret.arg(2));
			} catch (Fail e) {
				e.printStackTrace();

				return state;
			} catch (Exception e) {
				throw new ReasoningException(e);
			}

		}
		return state;
	}
}
