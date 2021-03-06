package aoetk.fxglassfishmonitor.view;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import aoetk.fxglassfishmonitor.event.ChartOpenEvent;
import aoetk.fxglassfishmonitor.model.GlassFishMonitor;
import aoetk.fxglassfishmonitor.model.Resource;
import aoetk.fxglassfishmonitor.event.ResourceChangeEvent;
import aoetk.fxglassfishmonitor.model.ResourceHolder;
import aoetk.fxglassfishmonitor.model.Statistic;
import aoetk.fxglassfishmonitor.serviceclient.ConnectFailedException;
import aoetk.fxglassfishmonitor.task.UpdateStatisticsTask;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
                collapseResouce(resourceHolderPod.getResourceModel().getFullName());
            } else {
                expandResource(resourceHolderPod.getResourceModel().getFullName());
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

    private Map<String, Stage> statisticViews = new HashMap<>();

    private Map<String, Stage> chartViews = new HashMap<>();

    private UpdateStatisticsTask updateTask;

    private ScheduledExecutorService scheduler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        boxTitle.setCursor(Cursor.OPEN_HAND);
        monitor = new GlassFishMonitor();
        monitor.setOnResourceChanged(new EventHandler<ResourceChangeEvent>() {
            @Override
            public void handle(ResourceChangeEvent event) {
                moveResoucePod(event.getBaseResourceFullName(), event.getAddedOrRemovedResources(),
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
        if (scheduler != null) {
            scheduler.shutdown();
            try {
                scheduler.awaitTermination(20, TimeUnit.SECONDS);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.INFO, null, ex);
            }
        }
        Platform.exit();
    }

    private void addStatisticToScheduler(Statistic statistic) {
        if (scheduler == null) {
            updateTask = new UpdateStatisticsTask();
            scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleWithFixedDelay(updateTask, 5, 5, TimeUnit.SECONDS);
        }
        updateTask.addStatistic(statistic);
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
        monitor.removeChildResource((ResourceHolder) resourcePods.get(resouceName).getResourceModel());
    }

    private void moveResoucePod(final String baseResouceName, final List<Resource> addedOrRemovedResources,
            final List<Resource> movedResources, final boolean added) {
        Timeline timeline = new Timeline();
        final List<KeyValue> keyValues = new ArrayList<>();
        final ResourceHolderPod basePod = (ResourceHolderPod) resourcePods.get(baseResouceName);
        if (added) {
            // 追加されたPodを新たに作り、シーングラフに追加後KeyValueを作成する
            final List<Line> visualizeLines = new ArrayList<>();
            for (Resource addedResource : addedOrRemovedResources) {
                ResourcePod addedPod;
                if (resourcePods.get(addedResource.getFullName()) != null) {
                    addedPod = resourcePods.get(addedResource.getFullName());
                    addedPod.setResourceModel(addedResource);
                } else {
                    if (addedResource instanceof Statistic) {
                        addedPod = new StatisticPod((Statistic) addedResource);
                        ((StatisticPod) addedPod).getPod().setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                StatisticPod targetPod = (StatisticPod) ((Rectangle) event.getTarget()).getParent();
                                openMetricWindow((Statistic) targetPod.getResourceModel());
                            }
                        });
                    } else {
                        addedPod = new ResourceHolderPod((ResourceHolder) addedResource, false);
                        ((ResourceHolderPod) addedPod).getExpander().setOnMouseClicked(new ExpandHandler());
                    }
                    resourcePods.put(addedResource.getFullName(), addedPod);
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
                if (addedPod.getVerticalLine() != null) {
                    visualizeLines.add(addedPod.getVerticalLine());
                }
            }
            movePodsVertically(movedResources, keyValues);

            // Paneの広さを再計算
            keyValues.add(new KeyValue(drawRegion.prefWidthProperty(), (monitor.getMaxDepth() + 1) * 150.0));
            keyValues.add(new KeyValue(drawRegion.prefHeightProperty(), (monitor.getMaxSiblingIndex() + 1) * 100.0));

            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.3), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    // Lineを可視化する処理
                    for (Line line : visualizeLines) {
                        line.setVisible(true);
                        drawRegion.getChildren().add(line);
                    }
                    basePod.openProperty().set(true);
                }
            }, keyValues.toArray(new KeyValue[keyValues.size()])));

        } else {
            // 移動処理後にシーングラフから取り除く
            for (Resource removedResouce : addedOrRemovedResources) {
                final ResourcePod removedPod = resourcePods.get(removedResouce.getFullName());
                removedPod.setResourceModel(null);
                final int parentDepth = removedResouce.getParent().depthProperty().get();
                final int parentSiblingIndex = removedResouce.getParent().siblingIndexProperty().get();
                keyValues.add(new KeyValue(removedPod.layoutXProperty(), 150.0 * parentDepth));
                keyValues.add(new KeyValue(removedPod.layoutYProperty(), 100.0 * parentSiblingIndex));
                keyValues.add(new KeyValue(removedPod.opacityProperty(), 0));
                drawRegion.getChildren().remove(removedPod.getHorizontalLine());
                drawRegion.getChildren().remove(removedPod.getVerticalLine());
            }
            movePodsVertically(movedResources, keyValues);
            timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(0.3), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    // シーングラフからPodを取り除く
                    for (Resource removedResource : addedOrRemovedResources) {
                        drawRegion.getChildren().remove(resourcePods.get(removedResource.getFullName()));
                    }
                    basePod.openProperty().set(false);
                }
            }, keyValues.toArray(new KeyValue[keyValues.size()])));
        }
        timeline.play();
    }

    private void openMetricWindow(Statistic statistic) {
        String fullName = statistic.getFullName();
        Stage statisticView = statisticViews.get(fullName);
        if (statisticView != null && !statisticView.isShowing()) {
            statisticView.show();
        } else {
            try {
                final FXMLLoader loader = new FXMLLoader(getClass().getResource("StatisticView.fxml"));
                loader.load();
                Parent root = loader.getRoot();
                StatisticViewController controller = loader.getController();
                controller.setStatisticModel(statistic);
                statisticView = new Stage(StageStyle.TRANSPARENT);
                statisticView.setScene(new Scene(root, 360, 360, Color.TRANSPARENT));
                controller.setParentStage(statisticView);
                controller.initializeData();
                controller.setOnChartOpened(new EventHandler<ChartOpenEvent>() {
                    @Override
                    public void handle(ChartOpenEvent event) {
                        openChartWindow(event.getStatisticModel(), event.getMetricProperty());
                    }
                });
                statisticViews.put(fullName, statisticView);
                statisticView.show();

                addStatisticToScheduler(statistic);

            } catch (IOException ioe) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ioe);
            }
        }
    }

    private void openChartWindow(Statistic statistic, String metricProperty) {
        String fullName = statistic.getFullName();
        Stage chartView = chartViews.get(fullName + "/" + metricProperty);
        if (chartView != null && !chartView.isShowing()) {
            chartView.show();
        } else {
            try {
                final FXMLLoader loader = new FXMLLoader(getClass().getResource("ChartView.fxml"));
                loader.load();
                Parent root = loader.getRoot();
                ChartViewController controller = loader.getController();
                chartView = new Stage(StageStyle.TRANSPARENT);
                chartView.setScene(new Scene(root, 360, 360, Color.TRANSPARENT));
                controller.setParentStage(chartView);
                controller.setStatisticModel(statistic);
                controller.setTargetMetric(metricProperty);
                controller.initializeData();
                chartViews.put(fullName, chartView);
                chartView.show();

            } catch (IOException ex) {
                Logger.getLogger(MainViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void movePodsVertically(List<Resource> movedResources, final List<KeyValue> keyValues) {
        // 移動するPodに対してKeyValueを作成する
        for (Resource movedResource : movedResources) {
            ResourcePod movedPod = resourcePods.get(movedResource.getFullName());
            int newIndex = movedResource.siblingIndexProperty().get();
            keyValues.add(new KeyValue(movedPod.layoutYProperty(), 100.0 * newIndex));
            keyValues.add(new KeyValue(movedPod.getHorizontalLine().startYProperty(), 100.0 * newIndex + 50.0));
            keyValues.add(new KeyValue(movedPod.getHorizontalLine().endYProperty(), 100.0 * newIndex + 50.0));
            int brotherIndex = 0;
            if (movedResource.getBrotherResource() != null) {
                brotherIndex = movedResource.getBrotherResource().siblingIndexProperty().get();
            }
            if (movedPod.getVerticalLine() != null) {
                keyValues.add(new KeyValue(movedPod.getVerticalLine().startYProperty(),
                        100.0 * brotherIndex + 50.0));
                keyValues.add(new KeyValue(movedPod.getVerticalLine().endYProperty(), 100.0 * newIndex + 50.0));
            }
        }
    }

}
