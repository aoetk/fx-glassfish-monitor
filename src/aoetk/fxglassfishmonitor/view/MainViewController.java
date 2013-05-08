package aoetk.fxglassfishmonitor.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import aoetk.fxglassfishmonitor.model.GlassFishMonitor;
import aoetk.fxglassfishmonitor.model.Resource;
import aoetk.fxglassfishmonitor.model.ResourceChangeEvent;
import aoetk.fxglassfishmonitor.model.ResourceHolder;
import aoetk.fxglassfishmonitor.model.Statistic;
import aoetk.fxglassfishmonitor.serviceclient.ConnectFailedException;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
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
import javafx.scene.shape.Line;
import javafx.util.Duration;

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
        monitor.setOnResourceChanged(new EventHandler<ResourceChangeEvent>() {
            @Override
            public void handle(ResourceChangeEvent event) {
                moveResoucePod(event.getBaseResourceName(), event.getAddedOrRemovedResources(),
                        event.getMovedResources(), event.getEventType() == ResourceChangeEvent.ADD);
            }
        });
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

    private void moveResoucePod(String baseResouceName, List<Resource> addedOrRemovedResources,
            List<Resource> movedResources, boolean added) {
        Timeline timeline = new Timeline();
        ResourcePod basePod = resourcePods.get(baseResouceName);
        if (added) {
            // 追加されたPodを新たに作り、シーングラフに追加後KeyValueを作成する
            final List<KeyValue> keyValues = new ArrayList<>();
            final List<Line> visualizeLines = new ArrayList<>();
            for (Resource addedResource : addedOrRemovedResources) {
                ResourcePod addedPod;
                if (resourcePods.get(addedResource.getName()) != null) {
                    addedPod = resourcePods.get(addedResource.getName());
                } else {
                    if (addedResource instanceof Statistic) {
                        addedPod = new StatisticPod((Statistic) addedResource);
                    } else {
                        addedPod = new ResourceHolderPod((ResourceHolder) addedResource, false);
                        ((ResourceHolderPod) addedPod).getExpander().setOnMouseClicked(new ExpandHandler());
                    }
                    resourcePods.put(addedResource.getName(), basePod);
                }
                addedPod.setOpacity(0);
                addedPod.setLayoutX(basePod.getLayoutX());
                addedPod.setLayoutY(basePod.getLayoutY());
                drawRegion.getChildren().add(addedPod);
                keyValues.add(new KeyValue(addedPod.opacityProperty(), 1.0));
                keyValues.add(new KeyValue(addedPod.layoutXProperty(), 150.0 * addedResource.depthProperty().get()));
                keyValues.add(new KeyValue(addedPod.layoutYProperty(),
                        100.0 * addedResource.siblingIndexProperty().get()));
                visualizeLines.add(addedPod.getHorizontalLine());
                if (addedPod.getVertialLine() != null) {
                    visualizeLines.add(addedPod.getVertialLine());
                }
            }

            // 移動するPodに対してKeyValueを作成する
            for (Resource movedResource : movedResources) {
                ResourcePod movedPod = resourcePods.get(movedResource.getName());
                int newDepth = movedResource.depthProperty().get();
                keyValues.add(new KeyValue(movedPod.layoutYProperty(), 100.0 * newDepth));
                keyValues.add(new KeyValue(
                        movedPod.getHorizontalLine().layoutYProperty(), 100.0 * newDepth + 50.0));
                keyValues.add(new KeyValue(movedPod.getVertialLine().endYProperty(), 100.0 * newDepth + 50.0));
            }
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.3), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    // Lineを可視化する処理
                    for (Line line : visualizeLines) {
                        line.setVisible(true);
                        drawRegion.getChildren().add(line);
                    }
                }
            }, keyValues.toArray(new KeyValue[keyValues.size()])));

        } else {
            // 削除されるPodをシーングラフから取り除く
        }
        timeline.play();
    }

}
