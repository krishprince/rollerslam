package rollerslam.display.realization.service.gui;

import rollerslam.agent.communicative.specification.type.object.OOState;
import rollerslam.display.realization.service.gui.mvc.Model;


public class ModelImpl implements Model {

    private OOState model;

    public OOState getModel() {
        return model;
    }

    public void setModel(OOState w) {
        this.model = w;
    }
}
