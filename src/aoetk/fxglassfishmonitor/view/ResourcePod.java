package aoetk.fxglassfishmonitor.view;

import aoetk.fxglassfishmonitor.model.Resource;
import javafx.scene.CacheHint;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.LineBuilder;

/**
 * Base class of resouce pod.
 * @author aoetk
 */
public class ResourcePod extends Region {

    protected Resource resourceModel;

    private Line horizontalLine;

    private Line verticalLine;

    public ResourcePod(Resource resourceModel) {
        this.resourceModel = resourceModel;
        createLine();
    }

    public Resource getResourceModel() {
        return resourceModel;
    }

    public void setResourceModel(Resource resourceModel) {
        this.resourceModel = resourceModel;
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

    public Line getVerticalLine() {
        return verticalLine;
    }

    public void setVerticalLine(Line verticalLine) {
        this.verticalLine = verticalLine;
    }

    private void createLine() {
        final Resource parent = resourceModel.getParent();
        final DropShadow shadow = DropShadowBuilder.create().blurType(BlurType.GAUSSIAN).color(Color.WHITE).build();
        if (parent != null) {
            int depth = resourceModel.depthProperty().get();
            int siblingIndex = resourceModel.siblingIndexProperty().get();
            int verticaldiff = siblingIndex - parent.siblingIndexProperty().get();
            if (verticaldiff == 0) {
                horizontalLine = LineBuilder.create()
                        .startX(depth * 150.0 - 25.0).startY(siblingIndex * 100.0 + 50.0)
                        .endX(depth * 150.0 + 50.0).endY(siblingIndex * 100.0 + 50.0)
                        .stroke(Color.WHITE).strokeWidth(3.0)
                        .effect(shadow).visible(false)
                        .cache(true).cacheHint(CacheHint.SPEED)
                        .build();
            } else {
                verticalLine = new Line(depth * 150.0, parent.siblingIndexProperty().get() * 100.0 + 50.0,
                        depth * 150.0, siblingIndex * 100.0 + 50.0);
                verticalLine = LineBuilder.create()
                        .startX(depth * 150.0).startY(siblingIndex * 100.0 - 50.0)
                        .endX(depth * 150.0).endY(siblingIndex * 100.0 + 50.0)
                        .stroke(Color.WHITE).strokeWidth(3.0)
                        .effect(shadow).visible(false)
                        .cache(true)
                        .build();
                horizontalLine = LineBuilder.create()
                        .startX(depth * 150.0).startY(siblingIndex * 100.0 + 50.0)
                        .endX(depth * 150.0 + 50.0).endY(siblingIndex * 100.0 + 50.0)
                        .stroke(Color.WHITE).strokeWidth(3.0)
                        .effect(shadow).visible(false)
                        .cache(true).cacheHint(CacheHint.SPEED)
                        .build();
            }
        }
    }

}
