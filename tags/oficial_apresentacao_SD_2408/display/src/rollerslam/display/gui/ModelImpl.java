package rollerslam.display.gui;

import rollerslam.display.gui.mvc.Model;
import rollerslam.environment.model.World;

public class ModelImpl implements Model {

    public World model;

    public World getModel() {
        return model;
    }

    public void setModel(World w) {
        this.model = w;
    }
}
