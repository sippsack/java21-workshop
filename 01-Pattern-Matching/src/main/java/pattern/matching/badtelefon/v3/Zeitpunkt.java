package pattern.matching.badtelefon.v3;

public record Zeitpunkt(int stunde, int minute) {
    public static final int MONDSCHEINZEIT_ENDE = 9;
    public static final int MONDSCHEINZEIT_ANFANG = 18;

    @Override
    public String toString() {
        return String.format("%02d:%02d Uhr", stunde, minute);
    }

    public boolean isMondschein() {
        return stunde() < MONDSCHEINZEIT_ENDE || stunde() > MONDSCHEINZEIT_ANFANG;
    }
}