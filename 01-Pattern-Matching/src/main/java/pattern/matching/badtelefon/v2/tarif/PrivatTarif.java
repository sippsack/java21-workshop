package pattern.matching.badtelefon.v2.tarif;

public class PrivatTarif extends Tarif {
    public static final double PREISPROMINUTE = 1.99;
    public static final double MONDSCHEINPREISPROMINUTE = 0.69;

    private final double rabatt;

    public PrivatTarif() {
        this(0);
    }

    public PrivatTarif(double rabatt) {
        if (rabatt < 0 || rabatt > 100) {
            throw new IllegalArgumentException("Rabatt muss zwischen 0 und 100 % liegen");
        }

        this.rabatt = rabatt;
    }

    public double getRabatt() {
        return rabatt;
    }

    public int getNettoMinuten(int minuten) {
        return Math.max(minuten - 1, 0);
    }
}
