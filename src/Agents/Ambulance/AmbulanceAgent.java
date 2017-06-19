package Agents.Ambulance;

import Behaviours.Ambulance.AmbulanceBehaviourFindPatient;
import Behaviours.Ambulance.AmbulanceBehaviourLocation;
import Behaviours.Ambulance.AmbulanceBehaviourRescue;
import Behaviours.Ambulance.AmbulanceBehaviourMove;
import Schemes.Ambulance.AmbulanceStates;
import Schemes.Map.Colors;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.awt.*;
import java.util.Random;

import static Functions.FromString.fromStringToInt;

/**
 * Created by anka on 25.04.17.
 */
public class AmbulanceAgent extends Agent {
    // Location
    private Point location;
    private Point target;
    private Point hospital;
    // States
    private int ambulanceState;
    private boolean rescuing;
    private AID patient;
    private AID nextPatient;

    public void setup(){
        // Jade description
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("ambulance");
        sd.setName(this.getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

        ambulanceState = AmbulanceStates.WAITING;
        rescuing = false;
        this.getLocalName();
    }

    public void startBehaviours() {
        addBehaviour(new AmbulanceBehaviourMove(this, 10));
        addBehaviour(new AmbulanceBehaviourLocation(this));
    }

    public void startAction(AID patient){
        this.ambulanceState = AmbulanceStates.RESCUING;
        this.patient = patient;
        addBehaviour(new AmbulanceBehaviourFindPatient(this, patient));
    }

    public void move() {
        if (ambulanceState == AmbulanceStates.RESCUING) {
            if (location.x == target.x && location.y == target.y) {
                if (rescuing == false) {
                    rescuing = true;
                    addBehaviour(new AmbulanceBehaviourRescue(this, patient));
                }
            }
            else
                go();
        }
        if (ambulanceState == AmbulanceStates.RETURNING) {
            if (location.x == target.x && location.y == target.y)
                ambulanceState = AmbulanceStates.WAITING;
            else
                go();
        }
    }

    public void go(){
        for(int i = 0; i < 12; i++){
            if(location.x != target.x){
                if(location.y != target.y){
                    location.x = (location.x > target.x) ? location.x-1 : location.x+1;
                    location.y = (location.y > target.y) ? location.y-1 : location.y+1;
                } else {
                    location.x = (location.x > target.x) ? location.x-1 : location.x+1;
                }
            } else {
                if(location.y != target.y){
                    location.y = (location.y > target.y) ? location.y-1 : location.y+1;
                } else {
                    location.x = target.x;
                    location.y = target.y;
                }
            }
        }
    }

    public void target(String location) {
        int tab[] = fromStringToInt(location);
        target = new Point(tab[0], tab[1]);
    }

    public void returning() {
        this.ambulanceState = AmbulanceStates.RETURNING;
        this.target = hospital;
        this.rescuing = false;
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        Font font = new Font("Serif", Font.PLAIN, 9);
        g2.setFont(font);
        g2.setColor(Colors.AMBULANCE);
        g2.drawString(this.getLocalName(), location.x, location.y);
        g2.fillRect(location.x - 4,location.y + 2, 8, 4);
    }

    // SETTERS & GETTERS
    public void setLocation(Point location) {
        this.location = location;
        Random r = new Random();
        this.location.x += r.nextInt(20) + 5;
        this.location.y += r.nextInt(20) + 5;
        this.hospital = new Point(this.location);
    }

    public int getAmbulanceState(){
        return ambulanceState;
    }
    public void setAmbulanceState(int ambulanceState) {
        this.ambulanceState = ambulanceState;
    }

    public Point getLocation() {
        return location;
    }

    public AID getNextPatient() {
        return nextPatient;
    }

    public Point getTarget() {
        return target;
    }
}
