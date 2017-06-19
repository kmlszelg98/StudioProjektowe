package Agents.Band;

import Agents.Time.Date;
import Agents.Time.TimeAgent;
import Behaviours.Band.*;
import Agents.Human.HumanAgent;
import Schemes.Human.HumanStates;
import VitalSigns.SignsObserved;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.util.ArrayList;
/**
 * Created by Kamil on 23.04.2017.
 */
public class BandAgent extends Agent {
    // Signs
    private ArrayList<SignsObserved> signsObserved;
    private int size;
    // States
    private boolean humanInDanger;
    private boolean calling;
    private boolean ambulanceCalled;
    private int waiting;

    private HumanAgent humanAgent;
    private TimeAgent time;
    private AID healthAssistant;

    // INITIALIZING
    public void setup(){
        // Jade description
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("band");
        sd.setName(this.getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        // Signs
        signsObserved = new ArrayList<>();                         //for 4h
        size = 12;
        // States
        humanInDanger = false;
        calling = false;
        ambulanceCalled = false;
        waiting = 0;
    }

    public void startBehaviours() {
        addBehaviour(new BandBehaviourSendTick(this, 9600));        // 12 times a day  10 = 11520
        addBehaviour(new BandBehaviourCheck(this, 1600));           // 20min
        addBehaviour(new BandBehaviourLocation(this));
        addBehaviour(new BandBehaviourRescue(this));
    }

    // FUNCTIONS
    public void updateSigns(SignsObserved signsObserved){
        if(this.signsObserved.size()>=size) this.signsObserved.remove(0);
        this.signsObserved.add(signsObserved);
    }

    public void setAlive() {
        this.humanAgent.setHumanState(HumanStates.WORKING);
        humanInDanger = false;
        calling = false;
        ambulanceCalled = false;
        waiting = 0;
    }

    public void setDead() {
        this.humanAgent.setHumanState(HumanStates.DEAD);
    }

    // SETTERS & GETTERS
    public void setInDanger() {
        this.humanAgent.setHumanState(HumanStates.IN_DANGER);
        this.humanInDanger = true;
    }
    public boolean isInDanger() {
        return humanInDanger;
    }
    public void setCalling(boolean calling){
        this.calling = calling;
    }
    public boolean isCalling(){
        return calling;
    }
    public void setAmbulanceCalled(boolean ambulanceCalled) {
        this.ambulanceCalled = ambulanceCalled;
    }
    public boolean isAmbulanceCalled() {
        return ambulanceCalled;
    }
    public void setHumanAgent(HumanAgent humanAgent) {
        this.humanAgent = humanAgent;
    }
    public HumanAgent getHuman() {
        return humanAgent;
    }
    public int getWaiting() {
        return waiting;
    }
    public void setWaiting(int waiting) {
        this.waiting = waiting;
    }
    public TimeAgent getTime() {
        return time;
    }
    public void setTime(TimeAgent time) {
        this.time = time;
    }
}
