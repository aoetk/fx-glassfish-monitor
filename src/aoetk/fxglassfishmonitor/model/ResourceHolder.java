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

    public ResourceHolder(String name, int depth, int siblingIndex) {
        super(name, depth, siblingIndex);
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
