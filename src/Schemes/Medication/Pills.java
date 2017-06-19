package Schemes.Medication;

/**
 * Created by anka on 05.05.17.
 */
public class Pills {
    public static final Pill FOR_ARRHYTHMIA = new Pill("arrhythmia", "pulse");
    public static final Pill FOR_PNEUMONIA = new Pill("pneumonia", "temperature");
    public static final Pill FOR_HYPERTENSION = new Pill("hypertension", "pressure");
    public static final Pill FOR_HEART_ATTACK = new Pill("heart_attack", "pulse");

    public static final Pill[] Pills = {
            FOR_ARRHYTHMIA, FOR_PNEUMONIA, FOR_HYPERTENSION, FOR_HEART_ATTACK
    };
}