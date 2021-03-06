package aoetk.fxglassfishmonitor.event;

import aoetk.fxglassfishmonitor.model.Resource;
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

    private String baseResourceFullName;

    private List<Resource> addedOrRemovedResources;

    private List<Resource> movedResources;

    public ResourceChangeEvent(
            EventType<ResourceChangeEvent> eventType,
            String baseResourceFullName,
            List<Resource> addedOrRemovedResources,
            List<Resource> movedResources) {
        super(eventType);
        this.baseResourceFullName = baseResourceFullName;
        this.addedOrRemovedResources = addedOrRemovedResources;
        this.movedResources = movedResources;
    }

    public String getBaseResourceFullName() {
        return baseResourceFullName;
    }

    public List<Resource> getAddedOrRemovedResources() {
        return addedOrRemovedResources;
    }

    public List<Resource> getMovedResources() {
        return movedResources;
    }

}
