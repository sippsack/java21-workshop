package pattern.matching.badtelefon.v3.tarif;

public record PrivatTarif(double rabatt) implements Tarif {
    public static final double PREISPROMINUTE = 1.99;
    public static final double MONDSCHEINPREISPROMINUTE = 0.69;


    public PrivatTarif {
        if (rabatt < 0 || rabatt > 100) {
            throw new IllegalArgumentException("Rabatt muss zwischen 0 und 100 % liegen");
        }
    }

    public int getNettoMinuten(int minuten) {
        return Math.max(minuten - 1, 0);
    }
}
