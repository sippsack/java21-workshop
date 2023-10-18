package pattern.matching.badtelefon.v2;

import pattern.matching.badtelefon.v2.tarif.PreisBerechnungsVisitor;
import pattern.matching.badtelefon.v2.tarif.Tarif;

public class Kunde {
    private final Tarif tarif;
    private final PreisBerechnungsVisitor<Tarif> visitor = new PreisBerechnungsVisitor<>();

    public Kunde(Tarif tarif) {
        this.tarif = tarif;
    }

    public void account(int minuten, Zeitpunkt zeitpunkt) {
        tarif.accept(visitor, minuten, zeitpunkt);
    }

    public double getGebuehr() {
        return visitor.getGebuehr();
    }
}
