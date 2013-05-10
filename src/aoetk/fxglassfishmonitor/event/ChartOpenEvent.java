package aoetk.fxglassfishmonitor.event;

import javafx.event.Event;
import javafx.event.EventType;

/**
 *
 * @author aoetk
 */
public class ChartOpenEvent extends Event {

    private String statisticFullName;

    private String metricProperty;

    public ChartOpenEvent(String statisticFullName, String metricProperty) {
        super(EventType.ROOT);
        this.statisticFullName = statisticFullName;
        this.metricProperty = metricProperty;
    }

    public String getStatisticFullName() {
        return statisticFullName;
    }

    public String getMetricProperty() {
        return metricProperty;
    }

}
