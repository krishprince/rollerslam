package rollerslam.fluxcommunicativeagent.realization.type;

import com.parctechnologies.eclipse.Atom;
import com.parctechnologies.eclipse.CompoundTerm;

public class ToStringPrinterUtility {
	
	public static String toString(Object object) {
		String retorno = "null";
		
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
			} 
		} else if(object!=null) {
			retorno = object.toString();
		}
		
		return retorno;
	}

}
