package aoetk.fxglassfishmonitor.view;

import java.net.URL;
import java.util.ResourceBundle;

import aoetk.fxglassfishmonitor.model.Metric;
import aoetk.fxglassfishmonitor.model.Statistic;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author aoetk
 */
public class StatisticViewController extends DraggableViewBase implements Initializable {

    @FXML
    Label lblMericName;

    @FXML
    TableView<Metric> tblMetrics;

    @FXML
    TableColumn<Metric, String> clmProperty;

    @FXML
    TableColumn<Metric, String> clmValue;

    @FXML
    TableColumn<Metric, Boolean> clmOp;

    private Statistic statisticModel;

    public void setStatisticModel(Statistic statisticModel) {
        this.statisticModel = statisticModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // setup table
        clmProperty.setCellValueFactory(new PropertyValueFactory<Metric, String>("property"));
        clmValue.setCellValueFactory(new PropertyValueFactory<Metric, String>("value"));
        clmOp.setCellValueFactory(new PropertyValueFactory<Metric, Boolean>("prottable"));
        // TODO CellFactory

    }

    public void initializeData() {
        lblMericName.setText(statisticModel.getName());
        tblMetrics.setItems(FXCollections.observableArrayList(statisticModel.getMetrics()));
    }

    @FXML
    void handleBtnCloseAction(ActionEvent event) {
        parentStage.close();
    }

}
