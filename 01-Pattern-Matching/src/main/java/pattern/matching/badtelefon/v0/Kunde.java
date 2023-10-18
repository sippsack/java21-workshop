package pattern.matching.badtelefon.v0;

public class Kunde {
	double gebuehr = 0.0;
	Tarif tarif;

	public Kunde(int tarifArt) {
		this.tarif = new Tarif(tarifArt);
	}

	public void account(int minuten, int stunde, int minute) {
		String message1 = String.format("Berechne Gespräch mit %02d min um %02d:%02d mit Tarif %s", minuten, stunde, minute, tarif.tarif);
		System.out.println(message1);
		boolean mondschein = false;
		double preis = 0;

		// Mondscheinzeit ?
		if (stunde < 9 || stunde > 18)
			mondschein = true;

		// Gespraechspreis ermitteln
		switch (tarif.tarif) {
		case Tarif.PRIVAT:
			minuten = minuten - 1;
			minuten = minuten < 0 ? 0 : minuten;
			if (mondschein)
				preis = minuten * 0.69;
			else
				preis = minuten * 1.99;
			break;

		case Tarif.BUSINESS:
			if (mondschein)
				preis = minuten * 0.79;
			else
				preis = minuten * 1.29;
			break;

		case Tarif.PROFI:
			preis = minuten * 0.69;
			break;

		}
		String message2 = String.format("Preis für das Gespräch: %.2f", preis);
		System.out.println(message2);
		
		gebuehr += preis;
		String message3 = String.format("Gesamtgebühr nach Gespräch um %02d:%02d (Mondscheinzeit: %s): %.2f", stunde, minute, mondschein, gebuehr);
		System.out.println(message3);
	}

	public double getGebuehr() {
		return gebuehr;
	}
}
