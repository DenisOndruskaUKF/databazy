package sk.ukf.robot;

import javafx.scene.paint.Color;

public class Rover extends Robot {
    private static final double SIRKA = 50;
    private static final double VYSKA = 30;
    private static final double RYCHLOST = 2;

    private final double podielY = Math.random();   // vyska na ploche (0 = hore, 1 = dole)
    private double x;
    private boolean prvyKrok = true;

    public Rover() {
        super(SIRKA, VYSKA);

        vykresli();
    }

    @Override
    protected void vykresliRobota() {
        gc.setFill(farba);
        gc.fillRect(0, 0, getWidth(), 22);

        gc.setFill(Color.BLACK);
        gc.fillOval(8, 18, 12, 12);
        gc.fillOval(30, 18, 12, 12);
    }

    @Override
    protected void posun(double sirkaPlochy, double vyskaPlochy) {
        double maxX = sirkaPlochy - getWidth();

        if (prvyKrok) {
            x = Math.random() * Math.max(0, maxX);
            prvyKrok = false;
        }

        x += smer * RYCHLOST;

        // po narazeni na lavy alebo pravy okraj sa odrazi
        if (x < 0 || x > maxX) {
            smer = -smer;
            x = Math.max(0, Math.min(x, maxX));
        }

        relocate(x, podielY * (vyskaPlochy - getHeight()));
    }
}
