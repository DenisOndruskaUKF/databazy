package sk.ukf.banka;

public class BankovyUcet {
    private double zostatok;

    public BankovyUcet(double zostatok) {
        this.zostatok = zostatok;
    }

    // Ucet je zdielany medzi viacerymi vláknami. Bez synchronized by si vlakna
    // navzajom prepisovali zmeny (zostatok += suma nie je atomicka operacia).
    public synchronized double getZostatok() {
        return zostatok;
    }

    public synchronized void vloz(double suma) {
        zostatok += suma;
    }

    // Kontrola zostatku a odpocitanie musia prebehnut ako jeden celok.
    public synchronized boolean vyber(double suma) {
        if (suma > zostatok) {
            return false;
        }
        zostatok -= suma;
        return true;
    }
}
