package aoetk.fxglassfishmonitor.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * The information node of GlassFish resource.
 * @author aoetk
 */
public class Resource {

    protected IntegerProperty depth = new SimpleIntegerProperty();

    protected IntegerProperty siblingIndex = new SimpleIntegerProperty();

    protected String name;

    protected ResourceHolder parent;

    public Resource(String name, int depth, int siblingIndex, ResourceHolder parent) {
        this.name = name;
        this.depth.set(depth);
        this.siblingIndex.set(siblingIndex);
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public IntegerProperty depthProperty() {
        return depth;
    }

    public IntegerProperty siblingIndexProperty() {
        return siblingIndex;
    }

    public ResourceHolder getParent() {
        return parent;
    }

    public void setParent(ResourceHolder parent) {
        this.parent = parent;
    }

}
