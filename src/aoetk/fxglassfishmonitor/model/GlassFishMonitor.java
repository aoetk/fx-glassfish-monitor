package aoetk.fxglassfishmonitor.model;

import aoetk.fxglassfishmonitor.event.ResourceChangeEvent;
import static aoetk.fxglassfishmonitor.serviceclient.JsonProperyNames.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import aoetk.fxglassfishmonitor.serviceclient.ConnectFailedException;
import aoetk.fxglassfishmonitor.serviceclient.GlassFishData;
import aoetk.fxglassfishmonitor.serviceclient.GlassFishServiceClient;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;

/**
 * The monitor for GlassFish resources.
 * @author aoetk
 */
public class GlassFishMonitor implements EventTarget {

    public static final String ROOT_RESOURCE_NAME = "server";

    private int maxDepth;

    private int maxSiblingIndex;

    // root resource
    private ResourceHolder serverResource;

    private GlassFishServiceClient serviceClient;

    private EventHandler<ResourceChangeEvent> onResourceChanged;

    public GlassFishMonitor() {
        serviceClient = GlassFishServiceClient.getInstance();
        maxDepth = 0;
        maxSiblingIndex = 0;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public int getMaxSiblingIndex() {
        return maxSiblingIndex;
    }

    public ResourceHolder getServerResource() {
        return serverResource;
    }

    public void setOnResourceChanged(
            EventHandler<ResourceChangeEvent> onResourceChanged) {
        this.onResourceChanged = onResourceChanged;
    }

    public void initialize() throws ConnectFailedException {
        serverResource = new ResourceHolder(ROOT_RESOURCE_NAME, 0, 0, null);
    }

    public void removeChildResource(ResourceHolder resourceHolder) {
        final List<Resource> removedResources = new ArrayList<>();
        final List<Resource> movedResources = new ArrayList<>();

        // moved resources
        int delta = resourceHolder.getChildStatistics().size() + resourceHolder.getChildResources().size() - 1;
        final int baseIndex = resourceHolder.siblingIndexProperty().get();
        addResourcesSiblingIndex(baseIndex, -delta, resourceHolder, movedResources);

        // deleted resources
        for (Statistic childStatistic : resourceHolder.getChildStatistics()) {
            removedResources.add(childStatistic);
        }
        for (ResourceHolder childResouceHolder : resourceHolder.getChildResources()) {
            removedResources.add(childResouceHolder);
        }
        resourceHolder.getChildStatistics().clear();
        resourceHolder.getChildResources().clear();
        resourceHolder.childTracedProperty().set(false);
        if (removedResources.size() > 0 || movedResources.size() > 0) {
            onResourceChanged.handle(
                    new ResourceChangeEvent(ResourceChangeEvent.REMOVE, resourceHolder.getFullName(),
                    removedResources, movedResources));
        }
    }

    public void traceChildResource(ResourceHolder resourceHolder) throws ConnectFailedException {
        GlassFishData gotData = serviceClient.getResource(resourceHolder.getFullName());
        addChildResource(resourceHolder, gotData);
    }

    private void addChildResource(ResourceHolder resourceHolder, GlassFishData glassFishData) {
        Map<String, Object> extraProperties = glassFishData.getExtraProperties();
        Map<String, Object> entites = (Map<String, Object>) extraProperties.get(ENTITY);
        Map<String, Object> childResources = (Map<String, Object>) extraProperties.get(CHILD_RESOURCES);

        List<Resource> addedResources = new ArrayList<>();
        List<Resource> movedResouces = new ArrayList<>();
        int idx = 0;
        if (!ROOT_RESOURCE_NAME.equals(resourceHolder.getName()) && !entites.isEmpty()) {
            // create Statistics
            for (String key : entites.keySet()) {
                Map<String, Object> metricMap = (Map<String, Object>) entites.get(key);
                StatisticType statisticType = getStatisticType(metricMap);
                List<Metric> metrics = new ArrayList<>();
                for (String metricProp : metricMap.keySet()) {
                    MetricType type = Metric.getMetricType(metricMap.get(metricProp));
                    metrics.add(new Metric(
                            metricProp, Metric.getMetricValueAsString(metricMap.get(metricProp), type), type));
                }
                Statistic newStatistic = new Statistic(key, resourceHolder.depthProperty().get() + 1,
                        resourceHolder.siblingIndexProperty().get() + idx, statisticType, metrics, resourceHolder);
                resourceHolder.getChildStatistics().add(newStatistic);
                addedResources.add(newStatistic);
                idx++;
            }
            if (resourceHolder.depthProperty().get() + 1 > maxDepth) {
                maxDepth++;
            }
            maxSiblingIndex = maxSiblingIndex + entites.size() - 1;
        }
        if (!childResources.isEmpty()) {
            // add ChildResource names
            for (String key : childResources.keySet()) {
                ResourceHolder newResouceHolder = new ResourceHolder(key, resourceHolder.depthProperty().get() + 1,
                        resourceHolder.siblingIndexProperty().get() + idx, resourceHolder);
                resourceHolder.getChildResources().add(newResouceHolder);
                addedResources.add(newResouceHolder);
                idx++;
            }
            if (resourceHolder.depthProperty().get() + 1 > maxDepth) {
                maxDepth++;
            }
            maxSiblingIndex = maxSiblingIndex + childResources.size() - 1;
        }
        final int baseIndex = resourceHolder.siblingIndexProperty().get();
        final int diff = resourceHolder.getChildStatistics().size()
                + resourceHolder.getChildResources().size() - 1;
        addResourcesSiblingIndex(baseIndex, diff, resourceHolder, movedResouces);
        resourceHolder.childTracedProperty().set(true);
        if (addedResources.size() > 0 || movedResouces.size() > 0) {
            onResourceChanged.handle(
                    new ResourceChangeEvent(ResourceChangeEvent.ADD, resourceHolder.getFullName(),
                    addedResources, movedResouces));
        }
    }

    private String getMetricValueAsString(Map<String, Object> metricMap, String propName, MetricType type) {
        switch (type) {
        case STRING:
            return (String) metricMap.get(propName);
        case INTEGER:
            return ((Integer) metricMap.get(propName)).toString();
        case DATETIME:
            return ((Long) metricMap.get(propName)).toString();
        default:
            return null;
        }
    }

    private StatisticType getStatisticType(Map<String, Object> metricMap) {
        if ("String".equals(metricMap.get(UNIT))) {
            return StatisticType.STRING;
        } else if (metricMap.containsKey(COUNT)) {
            if (metricMap.containsKey(TOTALTIME)) {
                return StatisticType.TIME;
            } else if (metricMap.containsKey(LOWERBOUND)) {
                return StatisticType.RANGE;
            } else {
                return StatisticType.COUNT;
            }
        } else {
            return StatisticType.UNKNOWN;
        }
    }

    private void addResourcesSiblingIndex(int baseIndex, int diff, ResourceHolder baseResouceHolder,
            List<Resource> movedResouces) {
        final ResourceHolder parent = baseResouceHolder.getParent();
        if (parent != null) {
            List<ResourceHolder> childResouces = parent.getChildResources();
            for (ResourceHolder resourceHolder : childResouces) {
                if (resourceHolder.siblingIndexProperty().get() > baseIndex) {
                    changeSiblingIndex(resourceHolder, diff, movedResouces);
                }
            }
            addResourcesSiblingIndex(baseIndex, diff, parent, movedResouces);
        }
    }

    private void changeSiblingIndex(ResourceHolder resourceHolder, int diff, List<Resource> movedResouces) {
        resourceHolder.siblingIndexProperty().set(resourceHolder.siblingIndexProperty().get() + diff);
        movedResouces.add(resourceHolder);
        for (Statistic statistic : resourceHolder.getChildStatistics()) {
            statistic.siblingIndexProperty().set(statistic.siblingIndexProperty().get() + diff);
            movedResouces.add(statistic);
        }
        for (ResourceHolder childHolder : resourceHolder.getChildResources()) {
            changeSiblingIndex(childHolder, diff, movedResouces);
        }
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain edc) {
        return edc;
    }

}
