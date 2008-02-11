package rollerslam.infrastructure.eclipse;

import java.io.File;
import rollerslam.infrastructure.server.PrintTrace;
import com.parctechnologies.eclipse.EclipseConnection;
import com.parctechnologies.eclipse.EclipseEngineOptions;
import com.parctechnologies.eclipse.EmbeddedEclipse;
import rollerslam.infrastructure.settings.GeneralSettings;
import rollerslam.infrastructure.settings.GeneralSettingsImpl;

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
            
            GeneralSettings gs = GeneralSettingsImpl.getInstance();
            String folder = null;
            
            try {
                System.setProperty("eclipse.directory", (String)gs.getSetting("ECLIPSE_HOME"));
                folder = (String)gs.getSetting("ENV_FLUX_CODE_HOME");
            } catch (Exception err) {
                throw new RuntimeException("Error intializing eclipse properties. Details: " + err, err);
            }
            
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
