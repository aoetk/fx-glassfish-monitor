package aoetk.fxglassfishmonitor.view;

import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * Base ViewController for draggable stage.
 * @author aoetk
 */
public class DraggableViewBase {

    private double dx;

    private double dy;

    protected Stage parentStage;

    /**
     * Set the parent Stage.
     * @param parentStage the parent Stage
     */
    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    public void handleMousePressed(MouseEvent event) {
        final Scene scene = parentStage.getScene();
        dx = event.getSceneX() + scene.getX();
        dy = event.getSceneY() + scene.getY();
    }

    public void handleMouseDragged(MouseEvent event) {
        parentStage.setX(event.getScreenX() - dx);
        parentStage.setY(event.getScreenY() - dy);
    }

}
