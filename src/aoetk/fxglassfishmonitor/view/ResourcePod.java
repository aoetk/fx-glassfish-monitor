package aoetk.fxglassfishmonitor.view;

import aoetk.fxglassfishmonitor.model.Resource;
import javafx.scene.layout.Region;

/**
 * Base class of resouce pod.
 * @author aoetk
 */
public class ResourcePod extends Region {

    protected Resource resourceModel;

    public ResourcePod(Resource resourceModel) {
        this.resourceModel = resourceModel;
    }

    public Resource getResourceModel() {
        return resourceModel;
    }

    @Override
    protected double computePrefWidth(double d) {
        return 150.0;
    }

    @Override
    protected double computePrefHeight(double d) {
        return 100.0;
    }

}
