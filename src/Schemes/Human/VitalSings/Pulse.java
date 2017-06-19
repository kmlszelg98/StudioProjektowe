package Schemes.Human.VitalSings;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Kamil on 23.04.2017.
 */
public class Pulse {
    public static final String NORMAL = "pulse_normal";
    public static final String SLOW = "pulse_slow";
    public static final String FAST = "pulse_fast";
    public static final String ARITHMETIC = "pulse_arithmetic";
    public static final String NONE = "pulse_none";
    public static final String VERY_SLOW = "pulse_very_slow";
    public static final String VERY_FAST = "pulse_very_fast";
    public static final String SLIGHTLY_SLOW = "pulse_slightly_slower";
    public static final String SLIGHTLY_FAST = "pulse_slightly_fast";

    public static final List<String> Pulses = Arrays.asList(new String[] {NONE,VERY_SLOW,SLOW,SLIGHTLY_SLOW,NORMAL,SLIGHTLY_FAST,FAST,VERY_FAST,ARITHMETIC});
}