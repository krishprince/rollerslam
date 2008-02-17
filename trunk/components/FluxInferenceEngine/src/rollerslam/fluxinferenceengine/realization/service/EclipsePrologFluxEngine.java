package rollerslam.fluxinferenceengine.realization.service;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import rollerslam.fluxinferenceengine.realization.eclipseprolog.EclipsePrologHandlerImpl;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluent;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluxAction;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluxSpecification;
import rollerslam.fluxinferenceengine.specification.service.FluxInferenceEngine;
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

public class EclipsePrologFluxEngine extends FluxInferenceEngine {

	private static EclipseConnection connection = EclipsePrologHandlerImpl.getInstance().getEclipseConnection();
	
	static {
		String folder = "/documents/rollerslam/workspace/FluxInferenceEngine/src/rollerslam/fluxinferenceengine/realization/flux/";
		
		try {
			connection.compile(new File(folder + "fluent.pl"));
			connection.compile(new File(folder + "flux.pl"));
			connection.compile(new File(folder + "ooflux.pl"));		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private Set<EclipsePrologFluxSpecification> compiledSpecs = new HashSet<EclipsePrologFluxSpecification>();
	
	public EclipsePrologFluxEngine() throws Exception {
		this.reasoningFacade = new ReasoningFacade() {

			@Override
			public Action computeNextAction(FluxSpecification spec, State state) throws ReasoningException {
				if (spec instanceof EclipsePrologFluxSpecification) {
					EclipsePrologFluxSpecification espec = (EclipsePrologFluxSpecification) spec;

					try {
						compileSpec(espec);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					Collection col = getEclipsePrologState(state.fluents);

					try {
						CompoundTerm ret = connection.rpc(espec.agentName + "ComputeNextAction",
								col, 
								null);
						return new EclipsePrologFluxAction((CompoundTerm) ret
								.arg(2));
					} catch (Fail e) {
						return null;						
					} catch (Exception e) {
						throw new ReasoningException(e);
					}
				}
				return null;
			}

			@Override
			public State updateModel(FluxSpecification spec, State state) throws ReasoningException {
				if (spec instanceof EclipsePrologFluxSpecification) {
					EclipsePrologFluxSpecification espec = (EclipsePrologFluxSpecification) spec;
										
					try {
						compileSpec(espec);
					} catch (Exception e) {
						throw new ReasoningException(e);
					}
					Collection col = getEclipsePrologState(state.fluents);

					try {
						CompoundTerm ret = connection.rpc(espec.agentName + "UpdateModel",
								col, 
								null);
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
			public State processAction(FluxSpecification spec, State state, Action action) throws ReasoningException {
				if (spec instanceof EclipsePrologFluxSpecification
						&& action instanceof EclipsePrologFluxAction) {
					
					EclipsePrologFluxSpecification espec = (EclipsePrologFluxSpecification) spec;
					EclipsePrologFluxAction		   eaction  = (EclipsePrologFluxAction) action;
										
					try {
						compileSpec(espec);
						Collection col = getEclipsePrologState(state.fluents);
						
						CompoundTerm ret = connection.rpc(espec.agentName + "ProcessAction",
								col, 
								eaction.actionTerm, 
								null);
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
			
		};
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
		
		for(int i=0;i<fluents.length;++i) {
			estate.add((CompoundTermImpl) ((EclipsePrologFluent)fluents[i]).term);
		}
		
		return estate;
	}

	protected void compileSpec(EclipsePrologFluxSpecification spec) throws Exception{
		if (!compiledSpecs.contains(spec)) {
			connection.compile(spec.fluxFile);
			compiledSpecs.add(spec);
		}
	}
}
