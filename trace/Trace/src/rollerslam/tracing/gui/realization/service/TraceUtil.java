/**
 * 
 */
package rollerslam.tracing.gui.realization.service;

import rollerslam.tracing.gui.specification.service.TraceDriver;

/**
 * @author Rafael Oliveira
 *
 */
public class TraceUtil {
	
	protected static TraceDriver traceDriver;
	
	/**
	 * Construtor static.
	 */
	static {
		try {
			traceDriver = new TraceDriverImpl();
		} catch (Throwable e) {
			System.err.println("Falha na inicialização do TracerDriver: " + e);
			throw new ExceptionInInitializerError(e);
		}
	}

	public static TraceDriver getTraceDriver() {
		return traceDriver;
	}	

}
