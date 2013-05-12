package aoetk.fxglassfishmonitor.view;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import aoetk.fxglassfishmonitor.model.Statistic;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.StringConverter;

/**
 * View Controller for ChartView.
 * @author aoetk
 */
public class ChartViewController extends DraggableViewBase implements Initializable {

    @FXML
    LineChart<Number, Number> chart;

    @FXML
    NumberAxis xAxis;

    @FXML
    NumberAxis yAxis;

    private Statistic statisticModel;

    private String targetMetric;

    private XYChart.Series<Number, Number> series = new XYChart.Series<>();

    private int maxValue;

    private int minValue;

    private long lastUpdatedTime;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number number) {
                long longValue = number.longValue();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                return formatter.format(new Date(longValue));
            }
            @Override
            public Number fromString(String string) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                long parsed;
                try {
                    parsed = formatter.parse(string).getTime();
                } catch (ParseException ex) {
                    Logger.getLogger(ChartViewController.class.getName()).log(Level.SEVERE, null, ex);
                    return null;
                }
                return parsed;
            }
        });
    }

    public void setStatisticModel(Statistic statisticModel) {
        this.statisticModel = statisticModel;
    }

    public void setTargetMetric(String targetMetric) {
        this.targetMetric = targetMetric;
    }

    public void initializeData() {
        chart.setTitle(statisticModel.getName());
        yAxis.setLabel(targetMetric + " (" + statisticModel.getUnit() + ")");
        final StringProperty lastUpdated = statisticModel.getLastUpdated();
        lastUpdated.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String valueProperty) {
                updateChart(Integer.valueOf(statisticModel.getMetricByProperty(targetMetric).valueProperty().get()));
            }
        });
        int metricValue = Integer.valueOf(statisticModel.getMetricByProperty(targetMetric).valueProperty().get());
        maxValue = minValue = metricValue;
        lastUpdatedTime = Long.valueOf(lastUpdated.get());
        chart.getData().add(series);
        updateChart(metricValue);
    }

    @FXML
    void handleBtnCloseAction(ActionEvent event) {
        parentStage.close();
    }

    private void updateChart(int metricValue) {
        long lastUpdated = Long.valueOf(statisticModel.getLastUpdated().get());
        updateXAxisBound(lastUpdated);
        updateYAxisBound(metricValue);
        XYChart.Data<Number, Number> data = new XYChart.Data<Number, Number>(lastUpdated, metricValue);
        if (series.getData().size() > 19) {
            series.getData().remove(0);
        }
        series.getData().add(data);
    }

    private void updateYAxisBound(int metricValue) {
        if (metricValue > maxValue) {
            maxValue = metricValue;
        } else if (metricValue < minValue) {
            minValue = metricValue;
        }
        int diff = maxValue - minValue;
        if (diff > 0) {
            yAxis.setTickUnit(diff / 10.0);
            yAxis.setLowerBound(minValue - diff / 10.0);
            yAxis.setUpperBound(maxValue + diff / 10.0);
        } else {
            yAxis.setLowerBound(minValue - 10);
            yAxis.setUpperBound(maxValue + 10);
        }
    }

    private void updateXAxisBound(long lastUpdated) {
        lastUpdatedTime = lastUpdated;
        xAxis.setLowerBound(lastUpdatedTime - 100000);
        xAxis.setUpperBound(lastUpdatedTime + 5000);
    }

}
