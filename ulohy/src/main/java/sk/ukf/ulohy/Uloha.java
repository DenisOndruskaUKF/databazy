package sk.ukf.ulohy;

public class Uloha {
    public static final String ROZPRACOVANA = "rozpracovaná";
    public static final String HOTOVA = "hotová";

    private String nazov;
    private String priorita;
    private String stav;

    public Uloha(String nazov, String priorita, String stav) {
        this.nazov = nazov;
        this.priorita = priorita;
        this.stav = stav;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getPriorita() {
        return priorita;
    }

    public void setPriorita(String priorita) {
        this.priorita = priorita;
    }

    public String getStav() {
        return stav;
    }

    public void setStav(String stav) {
        this.stav = stav;
    }

    // Takto sa uloha zobrazi v ListView.
    @Override
    public String toString() {
        return nazov + " (" + priorita + "): " + stav;
    }
}
