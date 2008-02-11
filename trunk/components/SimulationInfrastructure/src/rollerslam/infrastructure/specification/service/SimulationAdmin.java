package rollerslam.infrastructure.specification.service;

public interface SimulationAdmin {
		SimulationState    getState();
		void			   setState(SimulationState s);
}
