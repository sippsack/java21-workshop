package pattern.matching.badtelefon.v1.tarif;

import pattern.matching.badtelefon.v1.Zeitpunkt;

public abstract class Tarif {
	public static Tarif of(Type type) {
		return switch(type) {
			case PRIVAT -> new PrivatTarif();
			case BUSINESS -> new BusinessTarif();
			case PROFI -> new ProfiTarif();
		};
	}

	public final double berechnePreis(int minuten, Zeitpunkt zeitpunkt) {
		return getNettoMinuten(minuten) * getMinutenPreis(zeitpunkt);
	}

	protected abstract double getMinutenPreis(Zeitpunkt zeitpunkt);

	protected int getNettoMinuten(int minuten) {
		return minuten;
	}
}
