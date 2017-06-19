package Schemes.Human.VitalSings;

import java.util.Arrays;
import java.util.List;

/**
 * Created by anka on 25.04.17.
 */
public class Temperature {
    public static final String NORMAL = "temperature_normal";
    public static final String FEVERISHNESS = "temperature_feverishness";
    public static final String LOW_FEVER = "temperature_low_fever";
    public static final String FEVER = "temperature_fever";
    public static final String HIGH_FEVER = "temperature_high_fever";
    public static final String HYPERPYREXIA = "temperature_hyperpyrexia";

    public static final List<String> Temperatures = Arrays.asList(new String[] {NORMAL,FEVERISHNESS,LOW_FEVER,FEVER,HIGH_FEVER, HYPERPYREXIA});
}
