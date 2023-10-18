package pattern.matching.badtelefon.v1.tarif;

import pattern.matching.badtelefon.v1.Zeitpunkt;

public class PrivatTarif extends Tarif {

    protected double getMinutenPreis(Zeitpunkt zeitpunkt) {
        return zeitpunkt.isMondschein() ? 0.69 : 1.99;
    }

    protected int getNettoMinuten(int minuten) {
        return Math.max(minuten - 1, 0);
    }
}
