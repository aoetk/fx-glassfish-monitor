package aoetk.fxglassfishmonitor.view;

import aoetk.fxglassfishmonitor.model.ResourceHolder;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;

/**
 * Pod icon for resource holder.
 * @author aoetk
 */
public class ResourceHolderPod extends ResourcePod {

    private Rectangle pod;

    private Circle expander;

    private Label label;

    private ImageView icon;

    private DropShadow shadow;

    private Label plus;

    private Label minus;

    private BooleanProperty open = new SimpleBooleanProperty(false);

    public BooleanProperty openProperty() {
        return open;
    }

    public ResourceHolderPod(ResourceHolder resourceHolderModel, boolean open) {
        super(resourceHolderModel);
        createChildren(resourceHolderModel.getName(), open);
    }

    private void createChildren(String name, boolean open) {
        this.open.set(open);
        shadow = DropShadowBuilder.create().blurType(BlurType.GAUSSIAN).color(Color.WHITE).build();
        pod = RectangleBuilder.create()
                .width(50.0).height(50.0)
                .arcWidth(10.0).arcHeight(10.0)
                .stroke(Color.WHITE).strokeWidth(3.0)
                .fill(Color.TRANSPARENT).effect(shadow)
                .build();
        expander = CircleBuilder.create()
                .radius(12.5)
                .stroke(Color.WHITE).strokeWidth(3.0)
                .fill(Color.TRANSPARENT).effect(shadow)
                .cursor(Cursor.HAND)
                .build();
        plus = LabelBuilder.create()
                .text("+").textFill(Color.WHITE)
                .prefWidth(25.0).prefHeight(25.0)
                .alignment(Pos.CENTER)
                .font(new Font(16.0)).effect(shadow)
                .build();
        plus.visibleProperty().bind(this.open.not());
        minus = LabelBuilder.create()
                .text("-").textFill(Color.WHITE)
                .prefWidth(25.0).prefHeight(25.0)
                .alignment(Pos.CENTER)
                .font(new Font(16.0)).effect(shadow)
                .build();
        minus.visibleProperty().bind(this.open);
        label = LabelBuilder.create()
                .text(name).textFill(Color.WHITE)
                .alignment(Pos.CENTER)
                .font(new Font(16.0))
                .prefWidth(150.0)
                .tooltip(new Tooltip(name))
                .build();
        icon = ImageViewBuilder.create()
                .image(new Image("aoetk/fxglassfishmonitor/asset/configurations.gif")).build();
        this.getChildren().addAll(pod, plus, minus, expander, label, icon);
    }

    public Circle getExpander() {
        return expander;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        pod.setLayoutX(50.0);
        pod.setLayoutY(25.0);
        expander.setCenterX(112.5);
        expander.setCenterY(50.0);
        plus.setLayoutX(100.0);
        plus.setLayoutY(37.5);
        minus.setLayoutX(100.0);
        minus.setLayoutY(37.5);
        label.setLayoutX(0);
        label.setLayoutY(80.0);
        icon.setLayoutX(67.0);
        icon.setLayoutY(42.0);
    }

}
