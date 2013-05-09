package aoetk.fxglassfishmonitor.model;

import java.util.List;

/**
 * The statistic of GlassFish monitoring resource.
 * @author aoetk
 */
public class Statistic extends Resource {

    private List<Metric> metrics;

    private StatisticType type;

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
    }

    public List<Metric> getMetrics() {
        return metrics;
    }

    public StatisticType getType() {
        return type;
    }

}
