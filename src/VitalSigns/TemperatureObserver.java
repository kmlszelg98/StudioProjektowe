package VitalSigns;

import Schemes.Human.VitalSings.Temperature;
import java.util.Random;

/**
 * Created by anka on 25.04.17.
 */
public class TemperatureObserver {
    private String previousTemperature = Temperature.NORMAL;

    public String generateTemperature(){
        Random r = new Random();
        int lottery = r.nextInt(100);
        // IS IN_DANGER
        if (previousTemperature == Temperature.HYPERPYREXIA)
            return previousTemperature;
        // CAN BE IN_DANGER
        if (previousTemperature == Temperature.HIGH_FEVER) if (lottery == 0){
            previousTemperature = Temperature.HYPERPYREXIA;
            return Temperature.HYPERPYREXIA;
        }
        // STATE DOWN
        if (lottery < 2) {
            int index = Temperature.Temperatures.indexOf(previousTemperature);
            if (index == 0) return previousTemperature;
            else {
                previousTemperature = Temperature.Temperatures.get(index-1);
                return previousTemperature;
            }
        }
        // STATE STABLE
        else if (lottery >= 2 && lottery <= 97){
            return previousTemperature;
        }
        // STATE UP
        else{
            int index = Temperature.Temperatures.indexOf(previousTemperature);
            if (index == Temperature.Temperatures.size() - 2) return previousTemperature;
            else {
                previousTemperature = Temperature.Temperatures.get(index + 1);
                return previousTemperature;
            }
        }
    }

    public void setPreviousTemperature(String previousTemperature) {
        this.previousTemperature = previousTemperature;
    }
}