package pattern.matching.badtelefon.v3;

import pattern.matching.badtelefon.v3.tarif.PrivatTarif;
import pattern.matching.badtelefon.v3.tarif.BusinessTarif;
import pattern.matching.badtelefon.v3.tarif.ProfiTarif;
import pattern.matching.badtelefon.v3.tarif.Tarif;

public class Kunde {
    private final Tarif tarif;

    private double gebuehr;

    public Kunde(Tarif tarif) {
        this.tarif = tarif;
    }

    public void account(int minuten, Zeitpunkt zeitpunkt) {
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

    public double getGebuehr() {
        return gebuehr;
    }
}
