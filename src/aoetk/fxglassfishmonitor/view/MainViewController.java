package aoetk.fxglassfishmonitor.view;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import aoetk.fxglassfishmonitor.model.GlassFishMonitor;
import aoetk.fxglassfishmonitor.model.ResourceHolder;
import aoetk.fxglassfishmonitor.serviceclient.ConnectFailedException;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Controller for MainView.
 * @author aoetk
 */
public class MainViewController extends DraggableViewBase implements Initializable {

    class ExpandHandler implements EventHandler<MouseEvent> {
        @Override
        public void handle(MouseEvent event) {
            Node eventTarget = (Node) event.getTarget();
            ResourceHolderPod resourceHolderPod = (ResourceHolderPod) eventTarget.getParent();
            if (resourceHolderPod.openProperty().get()) {
                collapseResouce(resourceHolderPod.getResourceModel().getName());
            } else {
                expandResource(resourceHolderPod.getResourceModel().getName());
            }
        }
    }

    @FXML
    BorderPane containerPane;

    @FXML
    HBox boxTitle;

    @FXML
    Button btnExit;

    @FXML
    Pane drawRegion;

    private GlassFishMonitor monitor;

    private Map<String, ResourcePod> resourcePods = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boxTitle.setCursor(Cursor.OPEN_HAND);
        monitor = new GlassFishMonitor();
        try {
            monitor.initialize();
            drawRoot(monitor.getServerResource());
        } catch (ConnectFailedException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void handleBtnExitAction(ActionEvent event) {
        Platform.exit();
    }

    private void drawRoot(ResourceHolder rootResource) {
        ResourceHolderPod rootPod = new ResourceHolderPod(rootResource, false);
        resourcePods.put(rootResource.getName(), rootPod);
        rootPod.getExpander().setOnMouseClicked(new ExpandHandler());
        rootPod.setLayoutX(0);
        rootPod.setLayoutY(0);
        drawRegion.getChildren().add(rootPod);
    }

    void expandResource(String resourceName) {
        try {
            monitor.traceChildResource((ResourceHolder) resourcePods.get(resourceName).getResourceModel());
        } catch (ConnectFailedException ex) {
            Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void collapseResouce(String resouceName) {

    }

}
