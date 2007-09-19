package rollerslam.infrastructure.eclipse;

import java.io.File;

import rollerslam.infrastructure.server.PrintTrace;

import com.parctechnologies.eclipse.EclipseConnection;
import com.parctechnologies.eclipse.EclipseEngineOptions;
import com.parctechnologies.eclipse.EmbeddedEclipse;

public class EclipsePrologHandlerImpl implements EclipsePrologHandler {

	private static EclipsePrologHandler instance = null;
	
	public static EclipsePrologHandler getInstance() {
		if (instance == null) {
			instance = new EclipsePrologHandlerImpl();
		}
		return instance;
	}
	
    private EclipseConnection eclipse = null;
	public EclipseConnection getEclipseConnection() {
    	if (eclipse == null) {
			System.setProperty("eclipse.directory", "d:\\ECLiPSe 5.10");
			String folder = "c:\\temp\\maas\\1909\\rollerslam_workspace\\referee\\flux\\";

			EclipseEngineOptions eclipseEngineOptions = new EclipseEngineOptions();
			File eclipseProgram;

			eclipseEngineOptions.setUseQueues(false);
			try {
				eclipse = EmbeddedEclipse.getInstance(eclipseEngineOptions);
			    
				eclipseProgram = new File(folder + "flux.pl");
			    eclipse.compile(eclipseProgram);
			    
			    eclipseProgram = new File(folder + "fluent.chr");
			    eclipse.compile(eclipseProgram);
			} catch (Exception e) {
				if (PrintTrace.TracePrint) {
					e.printStackTrace();					
				}
			}				
		}
		return eclipse;
	}

}
