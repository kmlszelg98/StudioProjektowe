package Agents.HealthAssistant;

import Agents.DataBase.DataBaseAgent;
import Behaviours.HealthAssistent.HealthAssistantBehaviourCheck;
import Schemes.Medication.Diseases;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.ArrayList;

/**
 * Created by anka on 26.04.17.
 */
public class HealthAssistantAgent extends Agent{
    private AID patient;
    private AID band;
    private boolean alive;

    private ArrayList<String> currentDiseases;
    private ArrayList<String> pastDiseases;

    private DataBaseAgent dataBase;

    private boolean visitMade;
    private boolean makingVisit;

    public void setup(){
        // Jade description
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("healthAssistant");
        sd.setName(this.getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        alive = true;
        currentDiseases = new ArrayList<>();
        pastDiseases = new ArrayList<>();

        visitMade = false;
        makingVisit = false;
    }

    public void startBehaviours(){
        addBehaviour(new HealthAssistantBehaviourCheck(this, 115200));       //one per day
    }

    public void update() {
        currentDiseases = new ArrayList<>(dataBase.getPatient(patient).getCurrentDiseases());
        pastDiseases = new ArrayList<>(dataBase.getPatient(patient).getPastDiseases());
        alive = dataBase.getPatient(patient).isAlive();
    }

    public void setAIDs(AID patient, AID band){
        this.patient = patient;
        this.band = band;
    }
    public AID getPatient() {
        return patient;
    }
    public AID getBand() {
        return band;
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
    public void setDataBase(DataBaseAgent dataBase) {
        this.dataBase = dataBase;
    }
    public boolean isMakingVisit() {
        return makingVisit;
    }
    public boolean isVisitMade() {
        return visitMade;
    }
    public void setVisitMade(boolean visitMade) {
        this.visitMade = visitMade;
    }
    public void setMakingVisit(boolean makingVisit) {
        this.makingVisit = makingVisit;
    }
}