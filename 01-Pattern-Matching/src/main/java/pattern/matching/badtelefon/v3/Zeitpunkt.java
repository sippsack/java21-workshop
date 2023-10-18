package pattern.matching.badtelefon.v3;

import java.util.Objects;

public final class Zeitpunkt {
    public static final int MONDSCHEINZEIT_ENDE = 9;
    public static final int MONDSCHEINZEIT_ANFANG = 18;
    private final int stunde;
    private final int minute;

    public Zeitpunkt(int stunde, int minute) {
        this.stunde = stunde;
        this.minute = minute;
    }

    public int getMinute() {
        return minute;
    }

    public int getStunde() {
        return stunde;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Zeitpunkt zeitpunkt = (Zeitpunkt) o;
        return stunde == zeitpunkt.stunde && minute == zeitpunkt.minute;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stunde, minute);
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d Uhr", stunde, minute);
    }

    public boolean isMondschein() {
        return getStunde() < MONDSCHEINZEIT_ENDE || getStunde() > MONDSCHEINZEIT_ANFANG;
    }
}