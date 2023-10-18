package pattern.matching.badtelefon.v2.tarif;

import pattern.matching.badtelefon.v2.Zeitpunkt;

public class PreisBerechnungsVisitor<T extends Tarif> implements TarifVisitor<T> {
    private double gebuehr = 0.0;

    public double getGebuehr() {
        return gebuehr;
    }

    @Override
    public void visit(T tarif, int minuten, Zeitpunkt zeitpunkt) {
        if (tarif instanceof PrivatTarif) {
            var privatTarif = (PrivatTarif) tarif;
            var factor = (100 - privatTarif.getRabatt()) / 100;
            var minutenPreis = zeitpunkt.isMondschein() ? PrivatTarif.MONDSCHEINPREISPROMINUTE : PrivatTarif.PREISPROMINUTE;
            var nettoMinuten = privatTarif.getNettoMinuten(minuten);
            gebuehr += factor * nettoMinuten * minutenPreis;
        } else if (tarif instanceof BusinessTarif) {
            var businessTarif = (BusinessTarif) tarif;
            var factor = businessTarif.isVipKunde() ? 0.8 : 1.0;
            double minutenPreis = zeitpunkt.isMondschein() ? BusinessTarif.MONDSCHEINPREISPROMINUTE : BusinessTarif.PREISPROMINUTE;
            gebuehr += factor * minuten * minutenPreis;
        } else if (tarif instanceof ProfiTarif) {
            gebuehr += minuten * ProfiTarif.PREISPROMINUTE;
        } else {
            gebuehr += 60;
        }
    }
}