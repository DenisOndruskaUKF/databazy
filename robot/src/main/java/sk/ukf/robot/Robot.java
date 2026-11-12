package sk.ukf.robot;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public abstract class Robot extends Canvas {
    private static final Color[] FARBY = {
            Color.ORANGE, Color.BROWN, Color.BLUE, Color.YELLOW, Color.PINK, Color.GREEN
    };

    protected final GraphicsContext gc;

    protected Color farba;
    protected int smer;   // 1 alebo -1, obratenie smeru = zmena znamienka

    private final Timeline timer;

    public Robot(double sirka, double vyska) {
        super(sirka, vyska);

        farba = nahodnaFarba();
        smer = Math.random() < 0.5 ? 1 : -1;

        gc = getGraphicsContext2D();

        // kazdy robot ma vlastny casovac, preto sa hybe nezavisle od ostatnych
        timer = new Timeline(new KeyFrame(Duration.millis(20), event -> krok()));
        timer.setCycleCount(Animation.INDEFINITE);

        setFocusTraversable(true);

        // kliknutie: zmena farby + focus
        setOnMouseClicked(event -> {
            requestFocus();
            zmenFarbu();
        });

        // pri ziskani aj strate focusu prekreslime (cerveny ramik)
        focusedProperty().addListener((obs, oldValue, newValue) -> vykresli());

        setOnKeyPressed(event -> spracujKlaves(event));
    }

    private static Color nahodnaFarba() {
        return FARBY[(int) (Math.random() * FARBY.length)];
    }

    private void zmenFarbu() {
        Color nova;
        do {
            nova = nahodnaFarba();
        } while (nova.equals(farba));

        farba = nova;
        vykresli();
    }

    protected void vykresli() {
        gc.clearRect(0, 0, getWidth(), getHeight());

        vykresliRobota();

        if (isFocused()) {
            gc.setStroke(Color.RED);
            gc.setLineWidth(3);
            gc.strokeRect(1.5, 1.5, getWidth() - 3, getHeight() - 3);
        }
    }

    // Predok nevie, ako konkretny robot vyzera - kazdy potomok to musi implementovat.
    protected abstract void vykresliRobota();

    // Predok nevie, ako sa konkretny robot pohybuje - kazdy potomok to musi implementovat.
    protected abstract void posun(double sirkaPlochy, double vyskaPlochy);

    // Jeden krok pohybu. Verejne, aby sa dal zavolat aj raz po vlozeni robota na plochu.
    public void krok() {
        if (getParent() instanceof Pane plocha) {
            posun(plocha.getWidth(), plocha.getHeight());
        }
    }

    private void spracujKlaves(KeyEvent event) {
        switch (event.getCode()) {
            case SPACE:
                if (jeSpusteny()) zastav();
                else startuj();
                break;
            case DELETE:
                odstran();
                break;
        }
    }

    public void obratSmer() {
        smer = -smer;
    }

    public void startuj() {
        timer.play();
    }

    public void zastav() {
        timer.stop();
    }

    public boolean jeSpusteny() {
        return timer.getStatus() == Animation.Status.RUNNING;
    }

    // Pri odstraneni najskor zastavime casovac, potom robota vymazeme z Pane.
    public void odstran() {
        timer.stop();
        if (getParent() instanceof Pane pane) {
            pane.getChildren().remove(this);
        }
    }
}
