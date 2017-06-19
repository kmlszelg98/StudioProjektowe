package VitalSigns;

import Schemes.Human.VitalSings.Pressure;
import Schemes.Human.VitalSings.Pulse;
import Schemes.Human.VitalSings.Temperature;

import java.util.ArrayList;

/**
 * Created by anka on 25.04.17.
 */

public class SignsGenerator {
    private PressureObserver pressureObserver = new PressureObserver();
    private PulseObserver pulseObserver = new PulseObserver();
    private TemperatureObserver temperatureObserver = new TemperatureObserver();

    public String[] generateSigns(){
        String[] signs = new String[3];
        signs[0] = pulseObserver.generatePulse();
        signs[1] = temperatureObserver.generateTemperature();
        signs[2] = pressureObserver.generatePressure();
        return signs;
    }

    public void stabilizeAll() {
        pulseObserver.setPreviousPulse(Pulse.NORMAL);
        temperatureObserver.setPreviousTemperature(Temperature.NORMAL);
        pressureObserver.setPreviousPressure(Pressure.NORMAL);
    }

    public void stabilizeSigns(ArrayList<String> signs){
        for (String sign : signs) {
            if (sign.equals("pulse"))
                pulseObserver.setPreviousPulse(Pulse.NORMAL);
            if (sign.equals("temperature"))
                temperatureObserver.setPreviousTemperature(Temperature.NORMAL);
            if (sign.equals("pressure"))
                pressureObserver.setPreviousPressure(Pressure.NORMAL);
        }
    }
}
