package rollerslam.fluxinferenceengine.realization.flux;


public final class ADDRESS_FluxFiles {	
	
	private ADDRESS_FluxFiles(){}

	public static final String ADDRESS_FLUENT_FILE = ADDRESS_FluxFiles.class.getResource("fluent.pl").getFile();
	public static final String ADDRESS_FLUX_FILE = ADDRESS_FluxFiles.class.getResource("flux.pl").getFile();
	public static final String ADDRESS_OOFLUX_FILE = ADDRESS_FluxFiles.class.getResource("ooflux.pl").getFile();
	
}
