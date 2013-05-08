package aoetk.fxglassfishmonitor.view;

import aoetk.fxglassfishmonitor.model.Resource;
import javafx.scene.layout.Region;
import javafx.scene.shape.Line;

/**
 * Base class of resouce pod.
 * @author aoetk
 */
public class ResourcePod extends Region {

    protected Resource resourceModel;

    private Line horizontalLine;

    private Line vertialLine;

    public ResourcePod(Resource resourceModel) {
        this.resourceModel = resourceModel;
        createLine();
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

    public Line getHorizontalLine() {
        return horizontalLine;
    }

    public void setHorizontalLine(Line horizontalLine) {
        this.horizontalLine = horizontalLine;
    }

    public Line getVertialLine() {
        return vertialLine;
    }

    public void setVertialLine(Line vertialLine) {
        this.vertialLine = vertialLine;
    }

    private void createLine() {
        Resource parent = resourceModel.getParent();
        if (parent != null) {
            int depth = resourceModel.depthProperty().get();
            int siblingIndex = resourceModel.siblingIndexProperty().get();
            int verticaldiff = siblingIndex - parent.siblingIndexProperty().get();
            if (verticaldiff == 0) {
                horizontalLine = new Line(depth * 150.0 - 25.0, depth * 150.0 + 50.0,
                        siblingIndex * 100.0 - 50.0, siblingIndex * 100.0 - 50.0);
                horizontalLine.setVisible(false);
            } else {
                vertialLine = new Line(depth * 150.0, depth * 150.0,
                        parent.siblingIndexProperty().get() * 100.0 - 50.0, siblingIndex * 100.0 - 50.0);
                vertialLine.setVisible(false);
                horizontalLine = new Line(depth * 150.0, depth * 150.0 + 50.0,
                        siblingIndex * 100.0 - 50.0, siblingIndex * 100.0 - 50.0);
                horizontalLine.setVisible(false);
            }
        }
    }

}
