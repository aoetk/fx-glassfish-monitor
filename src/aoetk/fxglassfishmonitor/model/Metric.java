package aoetk.fxglassfishmonitor.model;

import static aoetk.fxglassfishmonitor.model.MetricType.*;

import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringPropertyBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The metric.
 * @author aoetk
 */
public class Metric {

    private StringProperty property = new SimpleStringProperty();

    private StringProperty value = new SimpleStringProperty();

    private ReadOnlyStringProperty formattedValue = new ReadOnlyStringPropertyBase() {
        {
            value.addListener(new InvalidationListener() {
                @Override
                public void invalidated(Observable o) {
                    fireValueChangedEvent();
                }
            });
        }
        @Override
        public String get() {
            if (metricType == DATETIME) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                return formatter.format(new Date(Long.valueOf(value.get())));
            } else {
                return value.get();
            }
        }
        @Override
        public Object getBean() {
            return Metric.this;
        }
        @Override
        public String getName() {
            return "formattedValue";
        }
    };

    private MetricType metricType;

    public Metric(String property, String value, MetricType metricType) {
        this.property.set(property);
        this.value.set(value);
        this.metricType = metricType;
    }

    public StringProperty propertyProperty() {
        return property;
    }

    public StringProperty valueProperty() {
        return value;
    }

    public ReadOnlyStringProperty formattedValueProperty() {
        return formattedValue;
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
