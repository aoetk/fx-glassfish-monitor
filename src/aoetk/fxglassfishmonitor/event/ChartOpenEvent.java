package aoetk.fxglassfishmonitor.event;

import aoetk.fxglassfishmonitor.model.Statistic;
import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author aoetk
 */
public class ChartOpenEvent extends Event {

    private Statistic statisticModel;

    private String metricProperty;

    public ChartOpenEvent(Statistic statisticModel, String metricProperty) {
        super(EventType.ROOT);
        this.statisticModel = statisticModel;
        this.metricProperty = metricProperty;
    }

    public Statistic getStatisticModel() {
        return statisticModel;
    }

    public String getMetricProperty() {
        return metricProperty;
    }

}
