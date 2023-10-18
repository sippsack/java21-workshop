package pattern.matching.badtelefon.v3.tarif;

public final class BusinessTarif implements Tarif {
    public static final double PREISPROMINUTE = 1.29;
    public static final double MONDSCHEINPREISPROMINUTE = 0.79;
    private final boolean vipKunde;

    public BusinessTarif() {
        this(false);
    }

    public BusinessTarif(boolean vipKunde) {
        this.vipKunde = vipKunde;
    }

    public boolean isVipKunde() {
        return vipKunde;
    }

}
