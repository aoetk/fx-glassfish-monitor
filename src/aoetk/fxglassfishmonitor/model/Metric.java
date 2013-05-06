package aoetk.fxglassfishmonitor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The metric.
 * @author aoetk
 */
public class Metric {

    private StringProperty property = new SimpleStringProperty();

    private StringProperty value = new SimpleStringProperty();

    private MetricType metricType;

    public Metric(String property, String value, MetricType metricType) {
        this.property.set(property);
        this.value.set(value);
        this.metricType = metricType;
    }

    public StringProperty propertyProprety() {
        return property;
    }

    public StringProperty valueProperty() {
        return value;
    }

    public MetricType getMetricType() {
        return metricType;
    }

}
