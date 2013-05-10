package aoetk.fxglassfishmonitor.model;

import static aoetk.fxglassfishmonitor.model.MetricType.*;

import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The metric.
 * @author aoetk
 */
public class Metric {

    private StringProperty property = new SimpleStringProperty();

    private StringProperty value = new SimpleStringProperty();

    private ReadOnlyBooleanWrapper prottable = new ReadOnlyBooleanWrapper();

    private MetricType metricType;

    public Metric(String property, String value, MetricType metricType) {
        this.property.set(property);
        this.value.set(value);
        this.metricType = metricType;
        this.prottable.set(metricType == INTEGER);
    }

    public StringProperty propertyProprety() {
        return property;
    }

    public StringProperty valueProperty() {
        return value;
    }

    public ReadOnlyBooleanProperty prottableProperty() {
        return prottable.getReadOnlyProperty();
    }

    public MetricType getMetricType() {
        return metricType;
    }

    public static MetricType getMetricType(Object value) {
        if (value instanceof Integer) {
            return MetricType.INTEGER;
        } else if (value instanceof Long) {
            return MetricType.DATETIME;
        } else {
            return MetricType.STRING;
        }
    }

    public static String getMetricValueAsString(Object metricValue, MetricType type) {
        switch (type) {
        case STRING:
            return (String) metricValue;
        case INTEGER:
            return ((Integer) metricValue).toString();
        case DATETIME:
            return ((Long) metricValue).toString();
        default:
            return null;
        }
    }

}
