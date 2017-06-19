package VitalSigns;

import Schemes.Human.VitalSings.Pressure;
import Schemes.Human.VitalSings.Pulse;
import Schemes.Human.VitalSings.Temperature;

import java.io.Serializable;

/**
 * Created by anka on 16.05.17.
 */
public class SignsObserved implements Serializable{
    private String pulse;
    private String temperature;
    private String pressure;

    public SignsObserved(String[] strings) {
        pulse = strings[0];
        temperature = strings[1];
        pressure = strings[2];
    }

    public boolean checkIfInDanger(){
        if (pulse == Pulse.NONE || temperature == Temperature.HYPERPYREXIA || pressure == Pressure.CRITIC) {
            return true;
        }
        return false;
    }

    public void stabilize(){
        pulse = Pulse.NORMAL;
        temperature = Temperature.NORMAL;
        pressure = Pressure.NORMAL;
    }

    @Override
    public String toString() {
        return  pulse + ';' + temperature + ';'  + pressure;
    }

    public String getPulse() {
        return pulse;
    }
    public String getTemperature() {
        return temperature;
    }
    public String getPressure() {
        return pressure;
    }
}