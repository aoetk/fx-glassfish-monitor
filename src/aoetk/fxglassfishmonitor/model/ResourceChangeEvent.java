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

    private List<String> addedOrRemovedResourceNames;

    private List<String> movedResourceNames;

    public ResourceChangeEvent(
            EventType<ResourceChangeEvent> eventType,
            List<String> addedResourceNames,
            List<String> movedResourceNames) {
        super(eventType);
        this.addedOrRemovedResourceNames = addedResourceNames;
        this.movedResourceNames = movedResourceNames;
    }

    public List<String> getAddedOrRemovedResourceNames() {
        return addedOrRemovedResourceNames;
    }

    public List<String> getMovedResourceNames() {
        return movedResourceNames;
    }

}
