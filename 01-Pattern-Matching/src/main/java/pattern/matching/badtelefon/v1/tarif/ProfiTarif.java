package pattern.matching.badtelefon.v1.tarif;

import pattern.matching.badtelefon.v1.Zeitpunkt;

public class ProfiTarif extends Tarif {

    protected double getMinutenPreis(Zeitpunkt zeitpunkt) {
        return 0.69;
    }

}
