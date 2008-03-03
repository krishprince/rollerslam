package rollerslam.fluxcommunicativeagent.realization.type;

import java.util.Iterator;
import java.util.Set;

import rollerslam.agent.communicative.specification.type.object.ObjectState;
import rollerslam.fluxinferenceengine.realization.type.EclipsePrologFluent;
import rollerslam.fluxinferenceengine.specification.type.Fluent;

public class FluxOOState extends ObjectState {
	public Set<Fluent> fluents;

	public FluxOOState(Set<Fluent> fluents) {
		super();
		this.fluents = fluents;
	}

	public String toString() {
		StringBuffer retorno = new StringBuffer();
		Iterator<Fluent> iterator = this.fluents.iterator();
		for(int i = 0;i<this.fluents.size();i++){
			Fluent fluent = iterator.next();
			String item = "";
			if(fluent instanceof EclipsePrologFluent){
				EclipsePrologFluent eclipsePrologFluent = (EclipsePrologFluent)fluent;
				item = ToStringPrinterUtility.toString(eclipsePrologFluent.getTerm());
			} else {
				item = fluent.toString();
			}
			if(i<this.fluents.size()-1){
				item = item + ", ";
			}
			retorno.append(item);
		}
		return retorno.toString();
	}
}
