package sk.ukf.ot;

import javafx.scene.paint.Color;

public class Chrobak extends Hmyz {
    public static final double VELKOST = 30;

    public Chrobak(double x, double y) {
        // pohyb iba vodorovne (rychlostY = 0), odraz od okraja zabezpeci predok
        super(x, y, VELKOST, VELKOST, Color.GREEN, 1.5, 0);

        vykresli();
    }

    @Override
    protected void vykresliHmyz() {
        gc.setFill(farba);
        gc.fillRect(0, 0, getWidth(), getHeight());
    }
}
