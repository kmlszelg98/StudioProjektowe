package Schemes.Human.VitalSings;

import java.util.Arrays;
import java.util.List;

/**
 * Created by anka on 25.04.17.
 */
public class Pressure {
    public static final String VERY_LOW = "pressure_very_low";
    public static final String LOW = "pressure_low";
    public static final String NORMAL = "pressure_normal";
    public static final String PREHYPERTENSIVE = "pressure_prehypertensive";
    public static final String HIGH = "pressure_high";
    public static final String VERY_HIGH = "pressure_very_high";
    public static final String CRITIC = "pressure_critic";

    public static final List<String> Pressures = Arrays.asList(new String[] {VERY_LOW,LOW,NORMAL,PREHYPERTENSIVE,HIGH,VERY_HIGH,CRITIC});
}
