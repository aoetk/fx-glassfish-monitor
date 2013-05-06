package aoetk.fxglassfishmonitor.view;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import aoetk.fxglassfishmonitor.model.GlassFishMonitor;
import aoetk.fxglassfishmonitor.model.ResourceHolder;
import aoetk.fxglassfishmonitor.serviceclient.ConnectFailedException;
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
        monitor = new GlassFishMonitor();
        try {
            monitor.initialize();
            drawView(monitor.getServerResource());
        } catch (ConnectFailedException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void handleBtnExitAction(ActionEvent event) {
        Platform.exit();
    }

    private void drawView(ResourceHolder rootResource) {
        // TODO 描画処理
    }

}
