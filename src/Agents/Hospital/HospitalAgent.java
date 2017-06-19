package Agents.Hospital;

import Agents.Time.Date;
import Behaviours.Hospital.HospitalBehaviourCheckDoctors;
import Behaviours.Hospital.HospitalBehaviourSendAmbulance;
import Behaviours.Hospital.HospitalBehaviourCheckAmbulances;
import Agents.Doctor.DoctorAgent;
import Agents.Ambulance.AmbulanceAgent;
import Schemes.Ambulance.AmbulanceStates;
import Schemes.Map.Colors;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by anka on 24.04.17.
 */
public class HospitalAgent extends Agent {
    private Point location;
    private ArrayList<DoctorAgent> doctors;
    private ArrayList<AmbulanceAgent> ambulances;

    public void setup(){
        // Jade description
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("hospital");
        sd.setName(this.getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        doctors = new ArrayList<>();
        ambulances = new ArrayList<>();
    }

    public void startBehaviours(){
        addBehaviour(new HospitalBehaviourCheckAmbulances(this));
        addBehaviour(new HospitalBehaviourSendAmbulance(this));
        addBehaviour(new HospitalBehaviourCheckDoctors(this));
    }

    public boolean checkAmbulance(){
        for (AmbulanceAgent ambulance:ambulances)
            if(ambulance.getAmbulanceState() == AmbulanceStates.WAITING || ambulance.getAmbulanceState() == AmbulanceStates.RETURNING) return true;
        return false;
    }

    public ArrayList<SuggestedDoctor> checkDoctors() {
        ArrayList<SuggestedDoctor> suggestedDoctors = new ArrayList<>();
        for (DoctorAgent doctor : doctors){
            Date suggestedVisit = doctor.suggestVisit();
            if (suggestedVisit != null)
                suggestedDoctors.add(new SuggestedDoctor(doctor.getAID(), doctor.getSpecializations(), suggestedVisit));
        }
        return suggestedDoctors;
    }

    public AmbulanceAgent findAmbulance(){
        for (AmbulanceAgent ambulance:ambulances)
            if(ambulance.getAmbulanceState() == AmbulanceStates.WAITING || ambulance.getAmbulanceState() == AmbulanceStates.RETURNING) return ambulance;
        return null;
    }

    public void sendAmbulance(AmbulanceAgent agent,AID patient){
        agent.startAction(patient);
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        Font font = new Font("Serif", Font.PLAIN, 15);
        g2.setFont(font);
        g2.setColor(Colors.HOSPITAL);
        g2.drawString(this.getLocalName(), location.x, location.y);
    }

    // GETTERS & SETTERS
    public ArrayList<DoctorAgent> getDoctors() {
        return doctors;
    }
    public ArrayList<AmbulanceAgent> getAmbulances() {
        return ambulances;
    }
    public Point getLocation() {
        return location;
    }

    public void setLocation(Point location) {
        this.location = location;
    }
    public void setDoctors(ArrayList<DoctorAgent> doctors) {
        this.doctors = doctors;
    }
    public void setAmbulances(ArrayList<AmbulanceAgent> ambulances) {
        this.ambulances = ambulances;
    }
}