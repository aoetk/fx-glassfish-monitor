package aoetk.fxglassfishmonitor.view;

import javafx.scene.layout.Region;

/**
 * Base class of resouce pod.
 * @author aoetk
 */
public class ResourcePod extends Region {

    @Override
    protected double computePrefWidth(double d) {
        return 150.0;
    }

    @Override
    protected double computePrefHeight(double d) {
        return 100.0;
    }

}
