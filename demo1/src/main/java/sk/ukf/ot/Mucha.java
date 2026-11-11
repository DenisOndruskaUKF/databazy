package sk.ukf.ot;

import javafx.scene.paint.Color;

public class Mucha extends Hmyz {
    public static final double VELKOST = 20;

    public Mucha(double x, double y) {
        super(x, y, VELKOST, VELKOST, Color.BLACK, nahodnaRychlost(), nahodnaRychlost());

        // kliknutim na muchu ju odstranime z plochy a zastavime jej pohyb
        setOnMouseClicked(event -> odstran());

        vykresli();
    }

    private static double nahodnaRychlost() {
        return Math.random() * 4 - 2;   // <-2, 2)
    }

    @Override
    protected void zmenSmer() {
        // obcas (priblizne 1x za sekundu) si mucha vyberie novy nahodny smer
        if (Math.random() < 0.05) {
            rychlostX = nahodnaRychlost();
            rychlostY = nahodnaRychlost();
        }
    }

    @Override
    protected void vykresliHmyz() {
        gc.setFill(farba);
        gc.fillOval(0, 0, getWidth(), getHeight());
    }
}
