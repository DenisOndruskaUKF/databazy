package sk.ukf.robot;

import javafx.scene.paint.Color;

public class Dron extends Robot {
    private static final double VELKOST = 40;
    private static final double RYCHLOST = 0.02;   // uhol v radianoch za jeden krok

    private double uhol = Math.random() * 2 * Math.PI;

    public Dron() {
        super(VELKOST, VELKOST);

        vykresli();
    }

    @Override
    protected void vykresliRobota() {
        gc.setFill(farba);
        gc.fillOval(0, 0, getWidth(), getHeight());

        gc.setFill(Color.WHITE);
        gc.fillOval(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);
    }

    @Override
    protected void posun(double sirkaPlochy, double vyskaPlochy) {
        uhol += smer * RYCHLOST;

        double polomer = Math.max(0, Math.min(sirkaPlochy, vyskaPlochy) / 2 - getWidth());

        // stred robota lezi na kruznici so stredom v strede plochy
        double x = sirkaPlochy / 2 + polomer * Math.cos(uhol) - getWidth() / 2;
        double y = vyskaPlochy / 2 + polomer * Math.sin(uhol) - getHeight() / 2;

        relocate(x, y);
    }
}
