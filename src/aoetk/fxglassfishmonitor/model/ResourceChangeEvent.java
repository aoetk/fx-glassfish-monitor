package aoetk.fxglassfishmonitor.model;

import java.util.List;
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author aoetk
 */
public class ResourceChangeEvent extends Event {

    public static final EventType<ResourceChangeEvent> ADD = new EventType<>("add");

    public static final EventType<ResourceChangeEvent> REMOVE = new EventType<>("remove");

    private String baseResourceName;

    private List<Resource> addedOrRemovedResources;

    private List<Resource> movedResources;

    public ResourceChangeEvent(
            EventType<ResourceChangeEvent> eventType,
            String baseResouceName,
            List<Resource> addedOrRemovedResources,
            List<Resource> movedResources) {
        super(eventType);
        this.baseResourceName = baseResouceName;
        this.addedOrRemovedResources = addedOrRemovedResources;
        this.movedResources = movedResources;
    }

    public String getBaseResourceName() {
        return baseResourceName;
    }

    public List<Resource> getAddedOrRemovedResources() {
        return addedOrRemovedResources;
    }

    public List<Resource> getMovedResources() {
        return movedResources;
    }

}
