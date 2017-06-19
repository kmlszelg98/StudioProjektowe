package Simulation;

import Agents.Ambulance.AmbulanceAgent;
import Agents.Band.BandAgent;
import Agents.DataBase.DataBaseAgent;
import Agents.Doctor.DoctorAgent;
import Agents.HealthAssistant.HealthAssistantAgent;
import Agents.Hospital.HospitalAgent;
import Agents.Human.HumanAgent;
import Agents.Time.TimeAgent;
import Map.City;
import Map.Terrain;
import Schemes.Doctor.Specializations;
import Schemes.Map.Hospitals;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by anka on 10.01.17.
 */
public class Generator {
    private int population = 20;
    private int hospitals_nr = 2;
    private Terrain map;
    private City city;  // TODO: 24.05.17 buildings

    private ArrayList<HealthAssistantAgent> healthAssistantAgents = new ArrayList<>();
    private ArrayList<HumanAgent> humanAgents = new ArrayList<>();
    private ArrayList<BandAgent> bandAgents = new ArrayList<>();
    private ArrayList<HospitalAgent> hospitalAgents = new ArrayList<>();
    private ArrayList<DoctorAgent> doctorAgents = new ArrayList<>();
    private ArrayList<AmbulanceAgent> ambulanceAgents = new ArrayList<>();
    private TimeAgent timeAgent;
    private DataBaseAgent dataBaseAgent;

    private AgentController healthAssistantControllers[] = new AgentController[population];
    private AgentController humanControllers[] = new AgentController[population];
    private AgentController bandControllers[] = new AgentController[population];
    private AgentController hospitalControllers[] = new AgentController[hospitals_nr];
    private AgentController doctorControllers[] = new AgentController[hospitals_nr*2];
    private AgentController ambulanceControllers[] = new AgentController[hospitals_nr*2];
    private AgentController timeController;
    private AgentController dataBaseController;

    public Generator(ContainerController time, ContainerController database, ContainerController healthAssistants, ContainerController humans, ContainerController bands, ContainerController hospitals, ContainerController doctors, ContainerController ambulances){
        // making map
        map = new Terrain("maps/map3.jpg");

        // making agents
        timeAgent = new TimeAgent();
        dataBaseAgent = new DataBaseAgent();

        for (int i = 0; i < hospitals_nr; i++){
            HospitalAgent hospital = new HospitalAgent();
            hospitalAgents.add(hospital);
        }

        for (int i = 0; i < hospitals_nr*2; i++){
            DoctorAgent doctor = new DoctorAgent();
            AmbulanceAgent ambulance = new AmbulanceAgent();

            doctorAgents.add(doctor);
            ambulanceAgents.add(ambulance);
        }

        for (int i = 0; i < population; i++){
            HealthAssistantAgent healthAssistant = new HealthAssistantAgent();
            HumanAgent human = new HumanAgent();
            BandAgent band = new BandAgent();

            healthAssistantAgents.add(healthAssistant);
            humanAgents.add(human);
            bandAgents.add(band);
        }

        // adding agents to controllers
        try {
            timeController = time.acceptNewAgent("Time", timeAgent);
            timeController.start();

            dataBaseController = database.acceptNewAgent("Data_Base", dataBaseAgent);
            dataBaseController.start();

            for (HospitalAgent hospital : hospitalAgents){
                hospitalControllers[hospitalAgents.indexOf(hospital)] = hospitals.acceptNewAgent("Hospital_" + hospitalAgents.indexOf(hospital), hospital);
                hospitalControllers[hospitalAgents.indexOf(hospital)].start();
            }

            for (DoctorAgent doctor : doctorAgents){
                doctorControllers[doctorAgents.indexOf(doctor)] = doctors.acceptNewAgent("Doctor_" + doctorAgents.indexOf(doctor), doctor);
                doctorControllers[doctorAgents.indexOf(doctor)].start();
            }

            for (AmbulanceAgent ambulance : ambulanceAgents){
                ambulanceControllers[ambulanceAgents.indexOf(ambulance)] = ambulances.acceptNewAgent("Ambulance_" + ambulanceAgents.indexOf(ambulance), ambulance);
                ambulanceControllers[ambulanceAgents.indexOf(ambulance)].start();
            }

            for (HealthAssistantAgent healthAssistant : healthAssistantAgents){
                healthAssistantControllers[healthAssistantAgents.indexOf(healthAssistant)] = healthAssistants.acceptNewAgent("HealthAssistant_" + healthAssistantAgents.indexOf(healthAssistant), healthAssistant);
                healthAssistantControllers[healthAssistantAgents.indexOf(healthAssistant)].start();
            }

            for (HumanAgent human : humanAgents) {
                humanControllers[humanAgents.indexOf(human)] = humans.acceptNewAgent("Human_" + humanAgents.indexOf(human), human);
                humanControllers[humanAgents.indexOf(human)].start();
            }

            for (BandAgent band : bandAgents) {
                bandControllers[bandAgents.indexOf(band)] = bands.acceptNewAgent("Band_" + bandAgents.indexOf(band), band);
                bandControllers[bandAgents.indexOf(band)].start();
            }
        } catch (StaleProxyException e) {
            e.printStackTrace();
        }
        // wait for all to add
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // setting up agents
        for (DoctorAgent doctorAgent : doctorAgents) {
            doctorAgent.setSpecializations(Specializations.Specializations[doctorAgents.indexOf(doctorAgent)]);
            doctorAgent.setDataBase(dataBaseAgent);
            doctorAgent.setTime(timeAgent);
        }

        for(int i = 0; i < bandAgents.size() ; i++){
            healthAssistantAgents.get(i).setAIDs(humanAgents.get(i).getAID(), bandAgents.get(i).getAID());
            healthAssistantAgents.get(i).setDataBase(dataBaseAgent);
            bandAgents.get(i).setHumanAgent(humanAgents.get(i));
            dataBaseAgent.setHumanAccounts(humanAgents.get(i).getAID());
        }

        for(HumanAgent humanAgent : humanAgents) {
            humanAgent.setTime(timeAgent);
            humanAgent.setAccount(dataBaseAgent.getPatient(humanAgent.getAID()));
        }

        dataBaseAgent.setTime(timeAgent);

        // TODO: 06.06.17 this is to stiff, works but to stiff
        for(int i=0; i<hospitalAgents.size(); i++){
            ArrayList<DoctorAgent> doctors2 = new ArrayList<>();
            ArrayList<AmbulanceAgent> ambulances2 = new ArrayList<>();
            ambulances2.add(ambulanceAgents.get(2*i));
            ambulances2.add(ambulanceAgents.get(2*i+1));
            doctors2.add(doctorAgents.get(2*i));
            doctors2.add(doctorAgents.get(2*i+1));
            hospitalAgents.get(i).setAmbulances(ambulances2);
            hospitalAgents.get(i).setDoctors(doctors2);
        }

        hospitalAgents.get(0).setLocation(new Point(Hospitals.HOSPITAL_0));
        hospitalAgents.get(1).setLocation(new Point(Hospitals.HOSPITAL_1));

        for (int i=0; i < ambulanceAgents.size(); i++){
            if (i < 2){
                ambulanceAgents.get(i).setLocation(new Point(Hospitals.HOSPITAL_0));
                doctorAgents.get(i).setLocation(new Point(Hospitals.HOSPITAL_0));
            }
            else{
                ambulanceAgents.get(i).setLocation(new Point(Hospitals.HOSPITAL_1));
                doctorAgents.get(i).setLocation(new Point(Hospitals.HOSPITAL_1));
            }
        }

        // starting behaviours
        timeAgent.startBehaviours();
        dataBaseAgent.startBehaviours();
        for (AmbulanceAgent ambulanceAgent : ambulanceAgents)
            ambulanceAgent.startBehaviours();
        for (HospitalAgent hospitalAgent : hospitalAgents)
            hospitalAgent.startBehaviours();
        for (HumanAgent humanAgent : humanAgents)
            humanAgent.startBehaviours();
        for (DoctorAgent doctorAgent : doctorAgents)
            doctorAgent.startBehaviours();
        for (HealthAssistantAgent healthAssistantAgent : healthAssistantAgents)
            healthAssistantAgent.startBehaviours();
        for (BandAgent bandAgent : bandAgents)
            bandAgent.startBehaviours();
    }

    // Drawing
    public void draw(Graphics g){
        map.draw(g);

        for (HospitalAgent hospital : hospitalAgents)
            hospital.draw(g);

        for (HumanAgent human : humanAgents)
            human.draw(g);

        for (AmbulanceAgent ambulance : ambulanceAgents)
            ambulance.draw(g);

        for (DoctorAgent doctor : doctorAgents)
            doctor.draw(g);

        timeAgent.draw(g);
    }

    // Getters
    public Terrain getMap() {
        return map;
    }
}