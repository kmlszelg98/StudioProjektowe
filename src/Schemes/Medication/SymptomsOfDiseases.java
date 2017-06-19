package Schemes.Medication;

import Schemes.Human.VitalSings.Pressure;
import Schemes.Human.VitalSings.Pulse;
import Schemes.Human.VitalSings.Temperature;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anka on 27.04.17.
 */
public class SymptomsOfDiseases {
    public static final List<String> allSymptomsFromBand;
    public static final List<String> allSymptomsFromHuman;
    public static final HashMap<String, ArrayList<String>> observedByBand;
    public static final HashMap<String, ArrayList<String>> observedByHuman;


    static {
        String[] symptoms = new String[] {Pulse.NONE, Pulse.VERY_SLOW, Pulse.SLOW, Pulse.FAST, Pulse.VERY_FAST, Pulse.ARITHMETIC, Temperature.FEVER, Temperature.HIGH_FEVER, Temperature.HYPERPYREXIA, Pressure.HIGH, Pressure.VERY_HIGH, Pressure.CRITIC};
        allSymptomsFromBand = Arrays.asList(symptoms);
    }

    static {
        String[] symptoms = new String[] {"chest_pain", "dizziness", "pounding", "fatigue", "cough", "sweat", "headache", "nosebleed"};
        allSymptomsFromHuman = Arrays.asList(symptoms);
    }

    // add list of symptoms for disease and remember to add disease to Schemes.Medication.Diseases
    static {
        observedByBand = new HashMap<>();

        ArrayList<String> heartAttackSymptoms = new ArrayList<>(
                Arrays.asList(Pulse.NONE, Pulse.VERY_SLOW, Pulse.SLOW));

        ArrayList<String> arrhythmiaSymptoms = new ArrayList<>(
                Arrays.asList(Pulse.FAST, Pulse.VERY_FAST, Pulse.ARITHMETIC));

        ArrayList<String> pneumoniaSymptoms = new ArrayList<>(
                Arrays.asList(Temperature.FEVER, Temperature.HIGH_FEVER, Temperature.HYPERPYREXIA));

        ArrayList<String> hypertensionSymptoms = new ArrayList<>(
                Arrays.asList(Pressure.HIGH, Pressure.VERY_HIGH, Pressure.CRITIC));

        observedByBand.put(Diseases.ARRHYTHMIA, arrhythmiaSymptoms);
        observedByBand.put(Diseases.PNEUMONIA, pneumoniaSymptoms);
        observedByBand.put(Diseases.HYPERTENSION, hypertensionSymptoms);
        observedByBand.put(Diseases.HEART_ATTACK, heartAttackSymptoms);
    }

    static {
        observedByHuman = new HashMap<>();

        ArrayList<String> heartAttackSymptoms = new ArrayList<>(
                Arrays.asList("chest_pain", "dizziness"));

        ArrayList<String> arrhythmiaSymptoms = new ArrayList<>(
                Arrays.asList("pounding", "fatigue"));

        ArrayList<String> pneumoniaSymptoms = new ArrayList<>(
                Arrays.asList("cough", "sweat"));

        ArrayList<String> hypertensionSymptoms = new ArrayList<>(
                Arrays.asList("headache", "nosebleed"));

        observedByHuman.put(Diseases.ARRHYTHMIA, arrhythmiaSymptoms);
        observedByHuman.put(Diseases.PNEUMONIA, pneumoniaSymptoms);
        observedByHuman.put(Diseases.HYPERTENSION, hypertensionSymptoms);
        observedByHuman.put(Diseases.HEART_ATTACK, heartAttackSymptoms);
    }
}