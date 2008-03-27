/**
 * 
 */
package rollerslam.tracing.gui.specification.service;

import rollerslam.tracing.gui.specification.type.Log;

/**
 * @author Rafael Oliveira
 *
 */
public interface TraceDriver {
	
	void add(TraceGUI traceGUI);
	void remove(TraceGUI traceGUI);
	void doTrace(Log log);
}
