package rollerslam.fluxinferenceengine.realization.eclipseprolog;

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
            try {
                System.setProperty("eclipse.directory", "C:\\Program Files\\ECLiPSe 5.10");
            } catch (Exception err) {
                throw new RuntimeException("Error intializing eclipse properties. Details: " + err, err);
            }
            
            EclipseEngineOptions eclipseEngineOptions = new EclipseEngineOptions();

            eclipseEngineOptions.setUseQueues(false);
            try {
                eclipse = EmbeddedEclipse.getInstance(eclipseEngineOptions);
            } catch (Exception e) {
				e.printStackTrace();
			}
        }
        return eclipse;
    }
}
