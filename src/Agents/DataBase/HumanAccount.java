package Agents.DataBase;

import Agents.Time.Date;
import Agents.Doctor.Prescription;
import Schemes.Medication.Diseases;
import VitalSigns.SignsObserved;
import jade.core.AID;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by anka on 26.04.17.
 */
public class HumanAccount implements Serializable {
    private AID human;
    private boolean alive;

    private ArrayList<String> currentDiseases;
    private ArrayList<String> pastDiseases;

    private HashMap<Date, SignsObserved> signsObserved; //for long period of time
    private HashMap<Date, String> signsEntered;

    private ArrayList<Prescription> assignedPrescriptions;
    private boolean used;

    public HumanAccount(AID human) {
        this.human = human;
        alive = true;
        currentDiseases = new ArrayList<>();
        pastDiseases = new ArrayList<>();
        signsObserved = new HashMap<>();
        signsEntered = new HashMap<>();
        assignedPrescriptions = new ArrayList<>();
        used = false;

        pastDiseases.add(Diseases.ARRHYTHMIA);
        pastDiseases.add(Diseases.HEART_ATTACK);
        pastDiseases.add(Diseases.HYPERTENSION);
        pastDiseases.add(Diseases.PNEUMONIA);

//        currentDiseases.add(Diseases.PNEUMONIA);
    }
    
    public void printSigns(){
        List<SignsObserved> l = new ArrayList<SignsObserved>(signsObserved.values());
        List<String> l2 = new ArrayList<>(signsEntered.values());
//        System.out.println(l);
        System.out.println(human.getLocalName() + ": " + l2);
    }
    
    public AID getHuman() {
        return human;
    }
    public HashMap<Date, SignsObserved> getSignsObserved() {
        return signsObserved;
    }
    public void addSignsObserved(SignsObserved signsObserved, Date date){
        this.signsObserved.put(date, signsObserved);
    }
    public void addSignsEntered(String sign, Date date){
        this.signsEntered.put(date, sign);
    }
    private void addPrescription(Prescription prescription){
        assignedPrescriptions.add(prescription);
    }
    private void deletePrescription(Prescription prescription){
        assignedPrescriptions.remove(prescription);
    }
    public void setUsed(boolean used) {
        this.used = used;
    }
    public boolean getUsed() {
        return used;
    }
    public ArrayList<String> getCurrentDiseases() {
        return currentDiseases;
    }
    public ArrayList<String> getPastDiseases() {
        return pastDiseases;
    }
    public boolean isAlive() {
        return alive;
    }
    public void setAlive(boolean alive) {
        this.alive = alive;
    }
}
