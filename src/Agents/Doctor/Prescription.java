package Agents.Doctor;

import Schemes.Medication.Pill;
import Schemes.Medication.Pills;

import java.util.Random;

/**
 * Created by anka on 12.05.17.
 */

public class Prescription {
    private Pill pill;
    private int time;

    public Prescription(String disease){
        generatePill(disease);
        generateTime();
    }

    private void generatePill(String disease){
        for (Pill pill : Pills.Pills)
            if (pill.getName().equals(disease))
                this.pill = pill;
    }

    private void generateTime(){
        Random r = new Random();
        int time = r.nextInt(4);
        this.time = (5 + 5*time)*2;
    }

    public int getTime() {
        return time;
    }

    public Pill getPill() {
        return pill;
    }

    public void setTime(int time) {
        this.time = time;
    }
}