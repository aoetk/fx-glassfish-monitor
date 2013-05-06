package aoetk.fxglassfishmonitor.view;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

/**
 *
 * @author aoetk
 */
public class StatisticViewController extends DraggableViewBase implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    @FXML
    void handleBtnCloseAction(ActionEvent event) {
        parentStage.close();
    }

}
