package pattern.matching.badtelefon.v2.tarif;

import pattern.matching.badtelefon.v2.Zeitpunkt;

public interface TarifVisitor<T extends Tarif> {
    void visit(T tarif, int minuten, Zeitpunkt zeitpunkt);
}