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
        gebuehr += switch (tarif) {
            case PrivatTarif privatTarif -> {
                var factor = (100 - privatTarif.rabatt()) / 100;
                var minutenPreis = zeitpunkt.isMondschein() ? PrivatTarif.MONDSCHEINPREISPROMINUTE : PrivatTarif.PREISPROMINUTE;
                var nettoMinuten = privatTarif.getNettoMinuten(minuten);
                yield factor * nettoMinuten * minutenPreis;
            }
            case BusinessTarif businessTarif -> {
                var factor = businessTarif.isVipKunde() ? 0.8 : 1.0;
                double minutenPreis = zeitpunkt.isMondschein() ? BusinessTarif.MONDSCHEINPREISPROMINUTE : BusinessTarif.PREISPROMINUTE;
                yield factor * minuten * minutenPreis;
            }
            case ProfiTarif _ -> minuten * ProfiTarif.PREISPROMINUTE;
            case null, default -> 60;
        };
    }

    public double getGebuehr() {
        return gebuehr;
    }
}
