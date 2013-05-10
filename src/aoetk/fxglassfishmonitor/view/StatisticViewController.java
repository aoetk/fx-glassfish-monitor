package aoetk.fxglassfishmonitor.view;

import java.net.URL;
import java.util.ResourceBundle;

import aoetk.fxglassfishmonitor.model.Statistic;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author aoetk
 */
public class StatisticViewController extends DraggableViewBase implements Initializable {

    private Statistic statisticModel;

    public void setStatisticModel(Statistic statisticModel) {
        this.statisticModel = statisticModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void handleBtnCloseAction(ActionEvent event) {
        parentStage.close();
    }

}
