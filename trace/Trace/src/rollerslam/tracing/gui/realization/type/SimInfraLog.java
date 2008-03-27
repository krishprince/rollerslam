/**
 * 
 */
package rollerslam.tracing.gui.realization.type;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import rollerslam.infrastructure.specification.service.Message;
import rollerslam.infrastructure.specification.type.AgentID;
import rollerslam.tracing.gui.specification.service.TraceLog;
import rollerslam.tracing.gui.specification.type.Log;

import com.parctechnologies.eclipse.CompoundTerm;

/**
 * @author Rafael Oliveira
 *
 */
public class SimInfraLog extends Log implements TraceLog{

	private Set<AgentID> receiver = new HashSet<AgentID>();
	private String action;
	private String method;
	private CompoundTerm compoundTerm;
	private String boxScore = "";
	private String boxScoreParameters = "";
	
	public SimInfraLog(String method, Message message, String action, String additionalInfo) {
		super(message.getSender(), additionalInfo);
		this.receiver = message.getReceiver();
		this.action = action;
		this.method = method;
	}
	
	public SimInfraLog(String method, Message message, String action, Object additionalInfo) {
		this(method, message, action, ToStringPrinterUtility.toString(additionalInfo));
		String base = ToStringPrinterUtility.toString(additionalInfo); 
		
		boxScore = base.substring(boxScore.indexOf('[')!= -1 ? boxScore.indexOf('['):0);
		boxScoreParameters = boxScore;
		boxScore = boxScore.indexOf('(') == -1 ? "" : boxScore.substring(0, boxScore.indexOf('('));
		if (boxScore.isEmpty())
			boxScoreParameters = "";
	}	

	public Set<AgentID> getReceiver() {
		return receiver;
	}

	public void setReceiver(Set<AgentID> receiver) {
		this.receiver = receiver;
	}

	@Override
	public String generateLog(Collection<String> selectionParameters) {
		String textLog = "";
		if (selectionParameters.contains("InterfaceOpCalled"))
			textLog += this.getReceiver().toString();
		if (selectionParameters.contains("InterfaceOpName"))
			textLog = this.getMethod();
		if (selectionParameters.contains("InterfaceOpParam"))
			textLog += this.getAction();
		if (selectionParameters.contains("InterfaceOpResult"))
			textLog += this.getAdditionInfo();
		if (!(textLog.isEmpty()))
			textLog = "[" + this.getAgentID()+ "] --> " + textLog; 
		return textLog;
	}


	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getBoxScore() {
		return boxScore;
	}

	public void setBoxScore(String boxScore) {
		this.boxScore = boxScore;
	}

	public String getBoxScoreParameters() {
		return boxScoreParameters;
	}

	public void setBoxScoreParameters(String boxScoreParameters) {
		this.boxScoreParameters = boxScoreParameters;
	}

}
