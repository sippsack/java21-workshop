package pattern.matching.badtelefon.v1.tarif;

import pattern.matching.badtelefon.v1.Zeitpunkt;

public class BusinessTarif extends Tarif {

    @Override
    protected double getMinutenPreis(Zeitpunkt zeitpunkt) {
        return zeitpunkt.isMondschein() ? 0.79 : 1.29;
    }

}
