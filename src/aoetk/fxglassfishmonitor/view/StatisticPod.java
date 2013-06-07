package aoetk.fxglassfishmonitor.view;

import aoetk.fxglassfishmonitor.model.Statistic;
import javafx.geometry.Pos;
import javafx.scene.CacheHint;
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
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;
import javafx.scene.text.Font;

/**
 * Pod icon for statistic.
 * @author aoetk
 */
public class StatisticPod extends ResourcePod {

    private Rectangle pod;

    private ImageView icon;

    private DropShadow shadow;

    private Label label;

    public StatisticPod(Statistic statisticModel) {
        super(statisticModel);
        createChildren(statisticModel.getName());
    }

    public Rectangle getPod() {
        return pod;
    }

    private void createChildren(String name) {
        shadow = DropShadowBuilder.create().blurType(BlurType.GAUSSIAN).color(Color.LIME).build();
        pod = RectangleBuilder.create()
                .width(50.0).height(50.0)
                .arcWidth(10.0).arcHeight(10.0)
                .stroke(Color.LIME).strokeWidth(3.0)
                .fill(Color.TRANSPARENT).effect(shadow)
                .cursor(Cursor.HAND)
                .cache(true).cacheHint(CacheHint.SPEED)
                .build();
        label = LabelBuilder.create()
                .text(name).textFill(Color.LIME)
                .alignment(Pos.CENTER)
                .font(new Font(16.0))
                .prefWidth(150.0)
                .tooltip(new Tooltip(name))
                .build();
        icon = ImageViewBuilder.create()
                .image(new Image("aoetk/fxglassfishmonitor/asset/monitor.gif")).build();
        this.getChildren().addAll(label, icon, pod);
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        pod.setLayoutX(50.0);
        pod.setLayoutY(25.0);
        label.setLayoutX(0);
        label.setLayoutY(80.0);
        icon.setLayoutX(67.0);
        icon.setLayoutY(42.0);
    }
}
