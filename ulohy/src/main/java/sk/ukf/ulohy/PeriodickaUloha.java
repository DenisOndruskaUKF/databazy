package sk.ukf.ulohy;

import javafx.application.Platform;

// Jedna trieda pre obe automaticke cinnosti: kazdych intervalMs vykona danu akciu.
public class PeriodickaUloha implements Runnable {
    private final long intervalMs;
    private final Runnable akcia;

    public PeriodickaUloha(long intervalMs, Runnable akcia) {
        this.intervalMs = intervalMs;
        this.akcia = akcia;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                System.out.println(e);
                return;
            }

            // Akcia pracuje so zoznamom a komponentmi GUI, preto sa musi
            // vykonat v JavaFX Application Thread.
            Platform.runLater(akcia);
        }
    }
}
