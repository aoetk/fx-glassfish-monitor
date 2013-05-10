package aoetk.fxglassfishmonitor.view;

import java.net.URL;
import java.util.ResourceBundle;

import aoetk.fxglassfishmonitor.event.ChartOpenEvent;
import aoetk.fxglassfishmonitor.model.Metric;
import aoetk.fxglassfishmonitor.model.Statistic;
import aoetk.fxglassfishmonitor.serviceclient.JsonProperyNames;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventDispatchChain;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;

/**
 *
 * @author aoetk
 */
public class StatisticViewController extends DraggableViewBase implements Initializable, EventTarget {

    @FXML
    Label lblMericName;

    @FXML
    TableView<Metric> tblMetrics;

    @FXML
    TableColumn<Metric, String> clmProperty;

    @FXML
    TableColumn<Metric, String> clmValue;

    @FXML
    TableColumn<Metric, String> clmOp;

    private Statistic statisticModel;

    private EventHandler<ChartOpenEvent> onChartOpened;

    public void setStatisticModel(Statistic statisticModel) {
        this.statisticModel = statisticModel;
    }

    public void setOnChartOpened(EventHandler<ChartOpenEvent> onChartOpened) {
        this.onChartOpened = onChartOpened;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // setup table
        clmProperty.setCellValueFactory(new PropertyValueFactory<Metric, String>("property"));
        clmValue.setCellValueFactory(new PropertyValueFactory<Metric, String>("value"));
        clmOp.setCellValueFactory(new PropertyValueFactory<Metric, String>("property"));
        clmOp.setCellFactory(new Callback<TableColumn<Metric, String>, TableCell<Metric, String>>() {
            @Override
            public TableCell<Metric, String> call(TableColumn<Metric, String> column) {
                TableCell<Metric, String> cell = new TableCell<Metric, String>(){
                    @Override
                    protected void updateItem(final String item, final boolean empty) {
                        if (JsonProperyNames.COUNT.equals(item) || JsonProperyNames.CURRENT.equals(item)) {
                            Button btnChart = ButtonBuilder.create()
                                    .text("...").styleClass("chart-button").build();
                            btnChart.setOnAction(new EventHandler<ActionEvent>() {
                                @Override
                                public void handle(ActionEvent event) {
                                    dispatchEvent(item);
                                }
                            });
                            setGraphic(btnChart);
                        }
                    }
                };
                return cell;
            }
        });

    }

    public void initializeData() {
        lblMericName.setText(statisticModel.getName());
        tblMetrics.setItems(FXCollections.observableArrayList(statisticModel.getMetrics()));
    }

    @FXML
    void handleBtnCloseAction(ActionEvent event) {
        parentStage.close();
    }

    @Override
    public EventDispatchChain buildEventDispatchChain(EventDispatchChain edc) {
        return edc;
    }

    private void dispatchEvent(String metricProp) {
        onChartOpened.handle(new ChartOpenEvent(statisticModel, metricProp));
    }

}
