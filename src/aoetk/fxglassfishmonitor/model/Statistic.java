package aoetk.fxglassfishmonitor.model;

import java.util.List;

/**
 * The statistic of GlassFish monitoring resource.
 * @author aoetk
 */
public class Statistic extends Resource {

    private List<Metric> metrics;

    private StatisticType type;

    public Statistic(String name, int depth, int siblingIndex, StatisticType type, List<Metric> metrics) {
        super(name, depth, siblingIndex);
        this.type = type;
        this.metrics = metrics;
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public StatisticType getType() {
        return type;
    }

}
