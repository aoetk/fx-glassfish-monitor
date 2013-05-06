package aoetk.fxglassfishmonitor.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    void handleBtnCloseAction(ActionEvent event) {
        parentStage.close();
    }

}
