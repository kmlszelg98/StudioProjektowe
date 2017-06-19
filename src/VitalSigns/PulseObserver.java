package VitalSigns;

import Schemes.Human.VitalSings.Pulse;
import java.util.Random;

/**
 * Created by Kamil on 23.04.2017.
 */

public class PulseObserver {
    private String previousPulse = Pulse.NORMAL;

    public String generatePulse(){
        Random r = new Random();
        int lottery = r.nextInt(100);
        // IS IN_DANGER
        if (previousPulse == Pulse.NONE) return previousPulse;
        // CAN BE IN_DANGER
        if (previousPulse == Pulse.VERY_SLOW) if (lottery == 0) {
            previousPulse = Pulse.NONE;
            return Pulse.NONE;
        }
        // START OF ARITHMIA
        if (previousPulse == Pulse.VERY_FAST) if (lottery == 0) {
            previousPulse = Pulse.ARITHMETIC;
            return Pulse.ARITHMETIC;
        }
        // END OF ARITHMIA
        if (previousPulse == Pulse.ARITHMETIC){
            if (lottery < 10){
                return previousPulse;
            }
            else {
                previousPulse = Pulse.FAST;
                return Pulse.FAST;
            }
        }
        // STATE DOWN
        if (lottery < 2) {
            int index = Pulse.Pulses.indexOf(previousPulse);
            if (index == 1) return previousPulse;
            else {
                previousPulse = Pulse.Pulses.get(index - 1);
                return previousPulse;
            }
        }
        // STATE STABLE
        else if (lottery >= 2 && lottery <= 97){
            return previousPulse;
        }
        // STATE UP
        else{
            int index = Pulse.Pulses.indexOf(previousPulse);
            if (index == Pulse.Pulses.size() - 2) return previousPulse;
            else {
                previousPulse = Pulse.Pulses.get(index + 1);
                return previousPulse;
            }
        }
    }

    public void setPreviousPulse(String previousPulse) {
        this.previousPulse = previousPulse;
    }
}
