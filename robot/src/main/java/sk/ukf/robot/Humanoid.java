package sk.ukf.robot;

public class Humanoid extends Robot {
    private static final double VELKOST = 40;
    private static final double RYCHLOST = 2;

    // vzdialenost prejdena po obvode obdlznikovej trasy (zaciatok je nahodny)
    private double poloha = Math.random() * 2000;

    public Humanoid() {
        super(VELKOST, VELKOST);

        vykresli();
    }

    @Override
    protected void vykresliRobota() {
        gc.setFill(farba);
        gc.fillRect(0, 0, getWidth(), getHeight());

        gc.setFill(javafx.scene.paint.Color.WHITE);
        gc.fillRect(getWidth() / 4, getHeight() / 4, getWidth() / 2, getHeight() / 2);
    }

    @Override
    protected void posun(double sirkaPlochy, double vyskaPlochy) {
        // a, b = rozmery trasy (lavy horny roh robota sa pohybuje po jej okraji)
        double a = sirkaPlochy - getWidth();
        double b = vyskaPlochy - getHeight();
        double obvod = 2 * (a + b);
        if (obvod <= 0) return;

        poloha += smer * RYCHLOST;
        double s = ((poloha % obvod) + obvod) % obvod;   // 0 <= s < obvod

        double x, y;
        if (s < a) {                 // horna hrana, doprava
            x = s;
            y = 0;
        } else if (s < a + b) {      // prava hrana, nadol
            x = a;
            y = s - a;
        } else if (s < 2 * a + b) {  // dolna hrana, dolava
            x = a - (s - a - b);
            y = b;
        } else {                     // lava hrana, nahor
            x = 0;
            y = b - (s - 2 * a - b);
        }

        relocate(x, y);
    }
}
