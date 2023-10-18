package pattern.matching.badtelefon.v3;

import java.util.Arrays;
import java.util.Random;

import pattern.matching.badtelefon.v3.tarif.PrivatTarif;
import pattern.matching.badtelefon.v3.tarif.BusinessTarif;
import pattern.matching.badtelefon.v3.tarif.ProfiTarif;
import pattern.matching.badtelefon.v3.tarif.Tarif;

public class TarifeRunner {
    public static void main(String args[]) {
        Random random = new Random();

        for (Tarif tarif : Arrays.asList(new PrivatTarif(0.1), new BusinessTarif(), new ProfiTarif())) {
            System.out.println(String.format("\nVerarbeitung von Tarif %s", tarif.getClass().getSimpleName()));
            Kunde k = new Kunde(tarif);

            for (int i = 0; i < 10; i++) {
                k.account(random.nextInt(5 + 1), new Zeitpunkt(random.nextInt(24), random.nextInt(60)));
            }

            System.out.println("Abrechnung: " + k.getGebuehr());
        }
    }
}
