/**
 * 
 */
package rollerslam.tracing.gui.realization.type;

import java.util.Collection;
import java.util.LinkedList;

import rollerslam.infrastructure.specification.type.AgentID;
import rollerslam.tracing.gui.specification.service.TraceLog;
import rollerslam.tracing.gui.specification.type.Log;

import com.parctechnologies.eclipse.Atom;
import com.parctechnologies.eclipse.CompoundTerm;

/**
 * @author Rafael Oliveira
 *
 */
public class PlayerLog extends Log implements TraceLog{

	private CompoundTerm compoundTerm;
	
	public PlayerLog(AgentID agentID, CompoundTerm compoundTerm, String additionInfo) {
		super(agentID, additionInfo);
		this.compoundTerm = compoundTerm;
	}


	
	private String generateLogAux(Object object){
		String retorno = "???";
		
		if(object instanceof Atom){
			Atom atom = (Atom)object;
			retorno = atom.functor();
		} else if (object instanceof CompoundTerm) {
			CompoundTerm compoundTerm = (CompoundTerm)object;
			final String functor = compoundTerm.functor();
			if("@".equals(functor)){
				retorno = generateLogAux(compoundTerm.arg(1)) + "[" + generateLogAux(compoundTerm.arg(2)) + "]" ;
			} else if("->".equals(functor)){
				retorno = generateLogAux(compoundTerm.arg(1)) + "->" + generateLogAux(compoundTerm.arg(2)) ;
			} else {
				retorno = functor + "(" + printParameters(compoundTerm)+")";
			}
		} else if (object instanceof LinkedList) {
			LinkedList l = (LinkedList) object;
			
			if (l.size() == 1) {
				retorno = generateLogAux(l.getFirst());
			} else {
				retorno = l.toString();				
			}
		} else if(object!=null) {
			retorno = object.toString();
		}
		
		return retorno;
	}
	
	private String printParameters(CompoundTerm compoundTerm) {
		String ret = "";
		
		for(int i=1;i<=compoundTerm.arity();++i) {
			if (i!=1) {
				ret += ", ";
			}
			ret += generateLogAux(compoundTerm.arg(i));
		}
		return ret;
	}

	public CompoundTerm getCompoundTerm() {
		return compoundTerm;
	}

	public void setCompoundTerm(CompoundTerm compoundTerm) {
		this.compoundTerm = compoundTerm;
	}



	@Override
	public String generateLog(Collection<String> selectionParameters) {
		String textLog = "???";		
		textLog = generateLogAux(compoundTerm);			
        return textLog + this.getAdditionInfo();
	}	


}
