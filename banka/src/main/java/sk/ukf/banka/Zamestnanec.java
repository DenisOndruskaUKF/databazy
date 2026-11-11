package sk.ukf.banka;

import java.util.function.Consumer;

public class Zamestnanec implements Runnable {
    private static int dalsieId = 1;

    private final int id;
    private final BankovyUcet ucet;
    private final String typ;
    private final double suma;
    private final Consumer<String> vypis;

    public Zamestnanec(BankovyUcet ucet, String typ, double suma, Consumer<String> vypis) {
        this.id = dalsieId++;
        this.ucet = ucet;
        this.typ = typ;
        this.suma = suma;
        this.vypis = vypis;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        try {
            // simulacia "premyslania" zamestnanca - vlakna sa tak navzajom prelinaju
            Thread.sleep((long) (Math.random() * 1000));
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        if (typ.equals("Vklad")) {
            ucet.vloz(suma);
            vypis.accept(String.format("Zamestnanec %d: vklad %.2f €", id, suma));
        } else {
            if (ucet.vyber(suma)) {
                vypis.accept(String.format("Zamestnanec %d: výber %.2f €", id, suma));
            } else {
                vypis.accept(String.format("Zamestnanec %d: výber %.2f € ZAMIETNUTÝ (nedostatok prostriedkov)", id, suma));
            }
        }
    }
}
