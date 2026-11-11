package sk.ukf.ot;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public abstract class Hmyz extends Canvas {
    protected final GraphicsContext gc;

    protected Color farba;
    protected double rychlostX, rychlostY;

    private final Timeline timer;

    public Hmyz(double x, double y, double sirka, double vyska, Color farba, double rychlostX, double rychlostY) {
        super(sirka, vyska);

        setLayoutX(x);
        setLayoutY(y);

        this.farba = farba;
        this.rychlostX = rychlostX;
        this.rychlostY = rychlostY;

        gc = getGraphicsContext2D();

        timer = new Timeline(new KeyFrame(Duration.millis(20), event -> posun()));
        timer.setCycleCount(Animation.INDEFINITE);

        startuj();
    }

    protected void vykresli() {
        gc.clearRect(0, 0, getWidth(), getHeight());
        vykresliHmyz();
    }

    // Predok nevie, ako konkretny hmyz vyzera - kazdy potomok to musi implementovat.
    protected abstract void vykresliHmyz();

    // Hook: potomok moze pred kazdym krokom zmenit rychlost (mucha). Standardne nerobi nic.
    protected void zmenSmer() {
    }

    private void posun() {
        if (!(getParent() instanceof Pane plocha)) return;

        zmenSmer();

        double x = getLayoutX() + rychlostX;
        double y = getLayoutY() + rychlostY;

        // odraz od okrajov plochy (spolocne pre vsetok hmyz)
        if (x < 0 || x + getWidth() > plocha.getWidth()) {
            rychlostX = -rychlostX;
            x = Math.max(0, Math.min(x, plocha.getWidth() - getWidth()));
        }
        if (y < 0 || y + getHeight() > plocha.getHeight()) {
            rychlostY = -rychlostY;
            y = Math.max(0, Math.min(y, plocha.getHeight() - getHeight()));
        }

        relocate(x, y);
    }

    public void posunNahor(double pixely) {
        relocate(getLayoutX(), Math.max(0, getLayoutY() - pixely));
        startuj();
    }

    public void startuj() {
        timer.play();
    }

    public void zastav() {
        timer.stop();
    }

    public void odstran() {
        timer.stop();
        if (getParent() instanceof Pane pane) {
            pane.getChildren().remove(this);
        }
    }
}
