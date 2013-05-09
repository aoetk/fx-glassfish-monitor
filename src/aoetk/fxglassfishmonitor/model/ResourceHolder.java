package aoetk.fxglassfishmonitor.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

/**
 * The resource holder.
 * @author aoetk
 */
public class ResourceHolder extends Resource {

    private BooleanProperty childTraced = new SimpleBooleanProperty(false);

    private List<Statistic> childStatistics = new ArrayList<>();

    private List<ResourceHolder> childResources = new ArrayList<>();

    /**
     * Create new instance.
     * @param name the name of the resource
     * @param depth the depth value of the resouce
     * @param siblingIndex the sibling index value of the resouce
     * @param parent the parent recource
     */
    public ResourceHolder(String name, int depth, int siblingIndex, ResourceHolder parent) {
        super(name, depth, siblingIndex, parent);
    }

    public BooleanProperty childTracedProperty() {
        return childTraced;
    }

    public List<Statistic> getChildStatistics() {
        return childStatistics;
    }

    public List<ResourceHolder> getChildResources() {
        return childResources;
    }

}
