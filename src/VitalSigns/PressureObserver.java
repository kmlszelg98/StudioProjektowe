package VitalSigns;

import Schemes.Human.VitalSings.Pressure;
import java.util.Random;

/**
 * Created by anka on 25.04.17.
 */

public class PressureObserver {
    private String previousPressure = Pressure.NORMAL;

    public String generatePressure(){
        Random r = new Random();
        int lottery = r.nextInt(100);
        // IS IN_DANGER
        if (previousPressure == Pressure.CRITIC)
            return previousPressure;
        // CAN BE IN_DANGER
        if (previousPressure == Pressure.VERY_HIGH) if (lottery == 0){
            previousPressure = Pressure.CRITIC;
            return previousPressure;
        }
        // STATE DOWN
        if (lottery < 2) {
            int index = Pressure.Pressures.indexOf(previousPressure);
            if (index == 0) return previousPressure;
            else {
                previousPressure = Pressure.Pressures.get(index - 1);
                return previousPressure;
            }
        }
        // STATE STABLE
        else if (lottery >= 2 && lottery <= 97){
            return previousPressure;
        }
        // STATE UP
        else{
            int index = Pressure.Pressures.indexOf(previousPressure);
            if (index == Pressure.Pressures.size() - 2) return previousPressure;
            else {
                previousPressure = Pressure.Pressures.get(index + 1);
                return previousPressure;
            }
        }
    }

    public void setPreviousPressure(String previousPressure) {
        this.previousPressure = previousPressure;
    }
}
