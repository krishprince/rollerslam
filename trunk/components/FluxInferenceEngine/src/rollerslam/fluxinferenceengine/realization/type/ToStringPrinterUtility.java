package rollerslam.fluxinferenceengine.realization.type;

import java.util.LinkedList;

import com.parctechnologies.eclipse.Atom;
import com.parctechnologies.eclipse.CompoundTerm;

class ToStringPrinterUtility {
	
	public static String toString(Object object) {
		String retorno = "???";
		
		if(object instanceof Atom){
			Atom atom = (Atom)object;
			retorno = atom.functor();
		} else if (object instanceof CompoundTerm) {
			CompoundTerm compoundTerm = (CompoundTerm)object;
			final String functor = compoundTerm.functor();
			if("@".equals(functor)){
				retorno = ToStringPrinterUtility.toString(compoundTerm.arg(1)) + "[" + ToStringPrinterUtility.toString(compoundTerm.arg(2)) + "]" ;
			} else if("->".equals(functor)){
				retorno = ToStringPrinterUtility.toString(compoundTerm.arg(1)) + "->" + ToStringPrinterUtility.toString(compoundTerm.arg(2)) ;
			} else {
				retorno = functor + "(" + printParameters(compoundTerm)+")";
			}
		} else if (object instanceof LinkedList) {
			LinkedList l = (LinkedList) object;
			
			if (l.size() == 1) {
				retorno = ToStringPrinterUtility.toString(l.getFirst());
			} else {
				retorno = l.toString();				
			}
		} else if(object!=null) {
			retorno = object.toString();
		}
		
		return retorno;
	}

	private static String printParameters(CompoundTerm compoundTerm) {
		String ret = "";
		
		for(int i=1;i<=compoundTerm.arity();++i) {
			if (i!=1) {
				ret += ", ";
			}
			ret += ToStringPrinterUtility.toString(compoundTerm.arg(i));
		}
		return ret;
	}

}
