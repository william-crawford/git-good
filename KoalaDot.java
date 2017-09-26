import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.Pane;


public class KoalaDot extends Circle {

    private Pane pane;
    private Color color;
    private KoalaDot[] children;

    private long nanoseconds;
    private boolean disabled = false;
    private boolean ready = false;

    public void makeReady() {
        ready = true;
        nanoseconds = System.nanoTime();
    }

    public void enable() {
        disabled = false;
    }

    private KoalaDot() {
        setOpacity(0);
        this.setOnMouseEntered((MouseEvent m) -> split());
        this.setOnMouseExited((MouseEvent m) -> disabled = false);
    }

    public KoalaDot(Color color, Pane pane, int x, int y, int r) {
        this();
        setCenterX(x);
        setCenterY(y);
        setRadius(r);
        this.color = color;
        this.pane = pane;
        setFill(color);
    }

    public KoalaDot(KoalaDot[] dots) {
        this();
        this.pane = dots[0].pane;
        this.setCenterX((dots[0].getCenterX() + dots[3].getCenterX()) / 2);
        this.setCenterY((dots[0].getCenterY() + dots[3].getCenterY()) / 2);
        this.setRadius(dots[0].getRadius() * 2);
        double r = 0;
        double g = 0;
        double b = 0;
        for (KoalaDot dot: dots) {
            r += dot.color.getRed();
            g += dot.color.getGreen();
            b += dot.color.getBlue();
        }
        r /= dots.length;
        g /= dots.length;
        b /= dots.length;
        this.color = Color.color(r, g, b);
        setFill(color);
        this.children = dots;
    }

    public void split() {
        if (System.nanoTime() - nanoseconds < 10000000) disabled = true;

        if (children != null && !disabled && ready) {
            pane.getChildren().remove(this);
            pane.getChildren().addAll(this.children);
            for (KoalaDot dot: children) {
                dot.setOpacity(1.0);
                dot.makeReady();
                if (!dot.isHover()) {
                    dot.disabled = false;
                }
            }
        }
    }
}
