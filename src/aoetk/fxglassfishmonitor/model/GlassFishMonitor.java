package aoetk.fxglassfishmonitor.model;

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

    // root resource
    private ResourceHolder serverResource;

    private GlassFishServiceClient serviceClient;

    private EventHandler<ResourceChangeEvent> onResourceChanged;

    public GlassFishMonitor() {
        serviceClient = new GlassFishServiceClient();
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

    public void traceChildResource(ResourceHolder resourceHolder) throws ConnectFailedException {
        GlassFishData gotData = serviceClient.getResource(
                GlassFishServiceClient.BASE_URL + getFullName(resourceHolder, resourceHolder.getName())
                + GlassFishServiceClient.EXTENSION);
        addChildResource(resourceHolder, gotData);
    }

    private void addChildResource(ResourceHolder resourceHolder, GlassFishData glassFishData) {
        Map<String, Object> extraProperties = glassFishData.getExtraProperties();
        Map<String, Object> entites = (Map<String, Object>) extraProperties.get(ENTITY);
        Map<String, Object> childResources = (Map<String, Object>) extraProperties.get(CHILD_RESOURCES);

        List<String> addedResourceNames = new ArrayList<>();
        List<String> movedResouceNames = new ArrayList<>();
        int idx = 0;
        if (!ROOT_RESOURCE_NAME.equals(resourceHolder.getName()) && !entites.isEmpty()) {
            // create Statistics
            for (String key : entites.keySet()) {
                Map<String, String> metricMap = (Map<String, String>) entites.get(key);
                StatisticType statisticType = getStatisticType(metricMap);
                List<Metric> metrics = new ArrayList<>();
                for (String metricProp : metricMap.keySet()) {
                    metrics.add(new Metric(metricProp, metricMap.get(metricProp),
                            getMetricTypeByPropertyName(metricProp)));
                }
                resourceHolder.getChildStatistics().add(
                        new Statistic(key, resourceHolder.depthProperty().get() + 1,
                        resourceHolder.siblingIndexProperty().get() + idx, statisticType, metrics, resourceHolder));
                addedResourceNames.add(key);
                idx++;
            }
        }
        if (!childResources.isEmpty()) {
            // add ChildResource names
            for (String key : childResources.keySet()) {
                resourceHolder.getChildResources().add(
                        new ResourceHolder(key, resourceHolder.depthProperty().get() + 1,
                        resourceHolder.siblingIndexProperty().get() + idx, resourceHolder));
                idx++;
            }
        }
        updateResoucesSiblingIndex(resourceHolder, movedResouceNames);
        resourceHolder.childTracedProperty().set(true);
        if (addedResourceNames.size() > 0 || movedResouceNames.size() > 0) {
            onResourceChanged.handle(
                    new ResourceChangeEvent(ResourceChangeEvent.ADD, addedResourceNames, movedResouceNames));
        }
    }

    private String getResourceNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    private StatisticType getStatisticType(Map<String, String> metricMap) {
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

    private MetricType getMetricTypeByPropertyName(String propName) {
        switch (propName) {
        case STARTTIME:
        case LASTSAMPLETIME:
            return MetricType.DATETIME;
        case COUNT:
        case CURRENT:
        case LOWWATERMARK:
        case HIGHWATERMARK:
        case LOWERBOUND:
        case UPPERBOUND:
        case TOTALTIME:
        case MAXTIME:
        case MINTIME:
            return MetricType.LONG;
        default:
            return MetricType.STRING;
        }
    }

    private void updateResoucesSiblingIndex(ResourceHolder baseResouceHolder, List<String> movedResouceNames) {
        final ResourceHolder parent = baseResouceHolder.getParent();
        final int baseIndex = baseResouceHolder.siblingIndexProperty().get();
        final int diff = baseResouceHolder.getChildStatistics().size()
                + baseResouceHolder.getChildResources().size() - 1;
        if (parent != null) {
            List<ResourceHolder> childResouces = parent.getChildResources();
            for (ResourceHolder resourceHolder : childResouces) {
                if (resourceHolder.siblingIndexProperty().get() > baseIndex) {
                    addSiblingIndex(resourceHolder, diff, movedResouceNames);
                }
            }
        }
    }

    private void addSiblingIndex(ResourceHolder resourceHolder, int diff, List<String> movedResouceNames) {
        resourceHolder.siblingIndexProperty().set(resourceHolder.siblingIndexProperty().get() + diff);
        movedResouceNames.add(resourceHolder.getName());
        for (Statistic statistic : resourceHolder.getChildStatistics()) {
            statistic.siblingIndexProperty().set(statistic.siblingIndexProperty().get() + diff);
            movedResouceNames.add(statistic.getName());
        }
        for (ResourceHolder childHolder : resourceHolder.getChildResources()) {
            addSiblingIndex(childHolder, diff, movedResouceNames);
        }
    }

    private String getFullName(ResourceHolder resourceHolder, String startName) {
        if (resourceHolder.getParent() == null) {
            return startName;
        } else {
            ResourceHolder parent = resourceHolder.getParent();
            return getFullName(parent, parent.getName() + "/" + startName);
        }
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain edc) {
        return edc;
    }

}
