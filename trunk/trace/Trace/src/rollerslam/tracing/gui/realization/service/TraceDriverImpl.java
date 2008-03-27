/**
 * 
 */
package rollerslam.tracing.gui.realization.service;

import java.util.ArrayList;
import java.util.Collection;

import rollerslam.tracing.gui.realization.type.SimInfraLog;
import rollerslam.tracing.gui.specification.service.TraceDriver;
import rollerslam.tracing.gui.specification.service.TraceGUI;
import rollerslam.tracing.gui.specification.type.Log;

/**
 * @author Rafael Oliveira
 *
 */
public class TraceDriverImpl implements TraceDriver {

	private Collection<TraceGUI> tracesGUI = new ArrayList<TraceGUI>();
	
	@SuppressWarnings("unchecked")
	public void doTrace(Log log) {
		
		Collection <TraceGUI> tg;
		
        synchronized (this) {
        	tg = (Collection)(((ArrayList)tracesGUI).clone());
        }                
        loop:
        for (TraceGUI t : tg) {
        	String textLog = "";
        	if (t.getAgentIDs().contains(log.getAgentID())){
        		if (log instanceof SimInfraLog){
        			Collection<String> selectionParameters = t.getParameters();
        			SimInfraLog simInfraLog = (SimInfraLog) log;
        			if (selectionParameters.contains("InterfaceOpCalled"))
        				textLog += simInfraLog.getReceiver().toString();
        			if (selectionParameters.contains("InterfaceOpName"))
        				textLog += simInfraLog.getMethod();
        			if (selectionParameters.contains("InterfaceOpParam"))
        				textLog += simInfraLog.getAction();
        			if (selectionParameters.contains("InterfaceOpResult")){
        				if (!(simInfraLog.getBoxScore().isEmpty())){
        					if (selectionParameters.contains(simInfraLog.getBoxScore()))
        						textLog += simInfraLog.getBoxScoreParameters();
        				}
        				else{
        					textLog += simInfraLog.getAdditionInfo();
        				}
        			}
        			if (!(textLog.isEmpty()))
        				textLog = "[" + simInfraLog.getAgentID()+ "] --> " + textLog;        			
        		}
        	}
        	if (!(textLog.isEmpty()))
        		t.appendOutput(textLog);        		
        }
		
	}

	public void add(TraceGUI traceGUI) {
		if (!tracesGUI.contains(traceGUI))
			tracesGUI.add(traceGUI);	
		
	}

	public void remove(TraceGUI traceGUI) {
		tracesGUI.remove(traceGUI);
		
	}

}
