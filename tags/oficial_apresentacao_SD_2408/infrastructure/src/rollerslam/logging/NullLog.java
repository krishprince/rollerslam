package rollerslam.logging;

import java.io.Serializable;
import java.util.Properties;

import orcas.logcomponents.basiclog.AbstractLog;

public class NullLog extends AbstractLog {

	protected void doLog(Serializable message) {

	}

	protected void init(Properties logProperties) {

	}

}
