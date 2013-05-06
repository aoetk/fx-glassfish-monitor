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

    protected Resource parent;

    public Resource(String name, int depth, int siblingIndex) {
        this.name = name;
        this.depth.set(depth);
        this.siblingIndex.set(siblingIndex);
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

    public Resource getParent() {
        return parent;
    }

    public void setParent(Resource parent) {
        this.parent = parent;
    }

}
