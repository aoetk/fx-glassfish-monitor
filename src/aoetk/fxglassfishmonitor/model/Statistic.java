package aoetk.fxglassfishmonitor.model;

import static aoetk.fxglassfishmonitor.serviceclient.JsonProperyNames.*;

import java.util.List;
import java.util.Map;

import aoetk.fxglassfishmonitor.serviceclient.ConnectFailedException;
import aoetk.fxglassfishmonitor.serviceclient.GlassFishData;
import aoetk.fxglassfishmonitor.serviceclient.GlassFishServiceClient;
import javafx.application.Platform;

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
        final GlassFishData gotData = serviceClient.getResource(
                GlassFishServiceClient.BASE_URL + getFullName() + GlassFishServiceClient.EXTENSION);
        if (Platform.isFxApplicationThread()) {
            updateMetrics(gotData);
        } else {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    updateMetrics(gotData);
                }
            });
        }
    }

    public long getLastUpdated() {
        for (Metric metric : metrics) {
            if (metric.getMetricType() == MetricType.DATETIME && LASTSAMPLETIME.equals(metric.propertyProperty().get())) {
                return Long.parseLong(metric.valueProperty().get());
            }
        }
        return -1;
    }

    public String getUnit() {
        for (Metric metric : metrics) {
            if (UNIT.equals(metric.propertyProperty().get())) {
                return metric.valueProperty().get();
            }
        }
        return null;
    }

    public Metric getMetricByProperty(String property) {
        for (Metric metric : metrics) {
            if (metric.propertyProperty().get().equals(property)) {
                return metric;
            }
        }
        return null;
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
