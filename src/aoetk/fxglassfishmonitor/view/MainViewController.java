package aoetk.fxglassfishmonitor.view;

import java.net.URL;
import java.util.ResourceBundle;

import aoetk.fxglassfishmonitor.model.GlassFishMonitor;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * Controller for MainView.
 * @author aoetk
 */
public class MainViewController extends DraggableViewBase implements Initializable {

    @FXML
    BorderPane containerPane;

    @FXML
    Button btnExit;

    @FXML
    Pane drawRegion;

    private GlassFishMonitor monitor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        containerPane.setCursor(Cursor.OPEN_HAND);
    }

    @FXML
    void handleBtnExitAction(ActionEvent event) {
        Platform.exit();
    }

}
