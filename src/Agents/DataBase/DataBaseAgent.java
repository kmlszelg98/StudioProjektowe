package Agents.DataBase;

import Agents.Time.Date;
import Behaviours.DataBase.DataBaseBehaviourConnectSave;
import Behaviours.DataBase.DataBaseBehaviourConnectSend;
import Behaviours.DataBase.DataBaseBehaviourSave;
import Behaviours.DataBase.DataBaseBehaviourSend;
import Agents.Time.TimeAgent;
import VitalSigns.SignsObserved;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;

/**
 * Created by anka on 24.05.17.
 */
public class DataBaseAgent extends Agent {
    private ArrayList<HumanAccount> humanAccounts;
    private TimeAgent time;

    public void setup(){
        // Jade description
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("database");
        sd.setName(this.getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        humanAccounts = new ArrayList<>();
    }

    public void startBehaviours() {
        addBehaviour(new DataBaseBehaviourConnectSave(this));
        addBehaviour(new DataBaseBehaviourConnectSend(this));
    }

    public boolean findPatient(AID patient){
        for (HumanAccount human : humanAccounts)
            if (human.getHuman().getLocalName().equals(patient.getLocalName())) return true;
        return false;
    }


    public void saveSigns(AID patient, SignsObserved signsObserved, Date date){
        addBehaviour(new DataBaseBehaviourSave(this, patient, signsObserved, date));

    }

    public void sendSigns(AID patient, ACLMessage reply) {
        addBehaviour(new DataBaseBehaviourSend(this, patient, reply));
    }

    // Setters & Getters
    public HumanAccount getPatient(AID patient){
        for (HumanAccount human : humanAccounts)
            if (human.getHuman().getLocalName().equals(patient.getLocalName())) return human;
        return null;
    }
    public void setHumanAccounts(AID human){
        humanAccounts.add(new HumanAccount(human));
    }
    public void setTime(TimeAgent time) {
        this.time = time;
    }
    public TimeAgent getTime() {
        return time;
    }


}
