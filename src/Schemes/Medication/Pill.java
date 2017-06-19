package Schemes.Medication;

/**
 * Created by anka on 11.06.17.
 */
public class Pill {
    private String name;
    private String stabilizes;

    public Pill(String name, String stabilizes) {
        this.name = name;
        this.stabilizes = stabilizes;
    }

    public String getName() {
        return name;
    }
    public String getStabilizes() {
        return stabilizes;
    }
}
