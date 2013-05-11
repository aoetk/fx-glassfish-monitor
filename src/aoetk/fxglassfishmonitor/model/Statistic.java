package aoetk.fxglassfishmonitor.model;

import static aoetk.fxglassfishmonitor.serviceclient.JsonProperyNames.*;

import java.util.List;
import java.util.Map;

import aoetk.fxglassfishmonitor.serviceclient.ConnectFailedException;
import aoetk.fxglassfishmonitor.serviceclient.GlassFishData;
import aoetk.fxglassfishmonitor.serviceclient.GlassFishServiceClient;

/**
 * The statistic of GlassFish monitoring resource.
 * @author aoetk
 */
public class Statistic extends Resource {

    private List<Metric> metrics;

    private StatisticType type;

    private GlassFishServiceClient serviceClient;

    /**
     * Create new instance.
     * @param name the name of the resource
     * @param depth the depth value of the resouce
     * @param siblingIndex the sibling index value of the resouce
     * @param type the type of statistic
     * @param metrics the metircs of the statistic
     * @param parent the parent recource
     */
    public Statistic(String name, int depth, int siblingIndex, StatisticType type, List<Metric> metrics,
            ResourceHolder parent) {
        super(name, depth, siblingIndex, parent);
        this.type = type;
        this.metrics = metrics;
        this.serviceClient = GlassFishServiceClient.getInstance();
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public StatisticType getType() {
        return type;
    }

    public void update() throws ConnectFailedException {
        GlassFishData gotData = serviceClient.getResource(
                GlassFishServiceClient.BASE_URL + getFullName() + GlassFishServiceClient.EXTENSION);
        updateMetrics(gotData);
    }

    private void updateMetrics(GlassFishData gotData) {
        Map<String, Object> extraProperties = gotData.getExtraProperties();
        Map<String, Object> entities = (Map<String, Object>) extraProperties.get(ENTITY);
        Map<String, Object> metricMap = (Map<String, Object>) entities.get(this.name);
        for (Metric metric : metrics) {
            metric.valueProperty().set(
                    Metric.getMetricValueAsString(metricMap.get(metric.propertyProperty().get()), metric.getMetricType()));
        }
    }

}
