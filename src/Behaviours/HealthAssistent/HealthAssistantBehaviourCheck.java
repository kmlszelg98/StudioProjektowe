package Behaviours.HealthAssistent;

import Agents.HealthAssistant.HealthAssistantAgent;
import Schemes.Medication.SymptomsOfDiseases;
import VitalSigns.SignsObserved;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.*;

/**
 * Created by anka on 25.05.17.
 */

public class HealthAssistantBehaviourCheck extends TickerBehaviour {
    private HealthAssistantAgent healthAssistantAgent;
    private AID human, band, database;
    private MessageTemplate mt;

    public HealthAssistantBehaviourCheck(HealthAssistantAgent healthAssistantAgent, long period) {
        super(healthAssistantAgent, period);
        this.healthAssistantAgent = healthAssistantAgent;
        this.human = healthAssistantAgent.getPatient();
        this.band = healthAssistantAgent.getBand();
    }

    @Override
    protected void onTick() {
        healthAssistantAgent.update();
        if (healthAssistantAgent.isAlive())
            myAgent.addBehaviour(new SymptomsCheck());
        else
            stop();
    }

    private class SymptomsCheck extends OneShotBehaviour{
        private int step = 0;
        private HashMap<String, Integer> symptomsDetected = new HashMap<>();
        ArrayList<String> detectedProblems = new ArrayList<>();
        private ArrayList<SignsObserved> signs = new ArrayList();

        @Override
        public void action() {
            // getting new signs
            if (step == 0) {
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("database");
                template.addServices(sd);
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    database = result[0].getName();
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }
//                System.out.println(healthAssistantAgent.getLocalName() + ": Connecting with database ");

                ACLMessage connecting_db = new ACLMessage(ACLMessage.CFP);
                connecting_db.addReceiver(database);
                try {
                    connecting_db.setContentObject(human);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                connecting_db.setConversationId("connecting_db_send");
                connecting_db.setReplyWith("connecting_db_send" + System.currentTimeMillis());
                myAgent.send(connecting_db);
                mt = MessageTemplate.and(
                        MessageTemplate.MatchConversationId("connecting_db_send"),
                        MessageTemplate.MatchInReplyTo(connecting_db.getReplyWith()));
                step = 1;
            }
            if (step == 1){
                while (true) {
                    ACLMessage db_response = myAgent.receive(mt);
                    if (db_response != null) {
                        if (db_response.getPerformative() == ACLMessage.PROPOSE) {
//                            System.out.println(healthAssistantAgent.getLocalName()+ ": Information about patient acquired");
                            Object object = null;
                            try {
                                object = db_response.getContentObject();
                            } catch (UnreadableException e) {
                                e.printStackTrace();
                            }
                            signs = (ArrayList<SignsObserved>) object;
                        } else {
//                            System.out.println(healthAssistantAgent.getLocalName() + ": Information about patient not available");
                        }
                        break;
                    }
                }
                step = 2;
            }
            // checking for symptoms
            if (step == 2){
                String notClassified = "notClassified";
                int numberOfOtherSymptoms = 0;

                for(Map.Entry<String, ArrayList<String>> entry : SymptomsOfDiseases.observedByBand.entrySet()) {
                    int numberOfSymptoms = 0;
                    String disease = entry.getKey();
                    ArrayList<String> symptoms = entry.getValue();
                    for (SignsObserved signsObserved : signs)
                        for (String symptom : symptoms)
                            if (signsObserved.getPulse().equals(symptom) || signsObserved.getTemperature().equals(symptom) || signsObserved.getPressure().equals(symptom))
                                numberOfSymptoms++;

                    boolean isKnown = false;
                    for (String name : healthAssistantAgent.getCurrentDiseases())
                        if (name.equals(disease)){
                            isKnown = true;
                            break;
                        }
                    if (!isKnown)
                        for (String name : healthAssistantAgent.getPastDiseases())
                            if (name.equals(disease)){
                                isKnown = true;
                                break;
                            }
                    if (isKnown) symptomsDetected.put(disease, numberOfSymptoms);
                    else numberOfOtherSymptoms += numberOfSymptoms;
                }
                symptomsDetected.put(notClassified, numberOfOtherSymptoms);
                step = 3;
            }
            // making visit if needed
            if (step == 3){
//                System.out.println(healthAssistantAgent.getLocalName() + ": " + symptomsDetected);
                for(Map.Entry<String, Integer> entry : symptomsDetected.entrySet()){
                    boolean isCurrentDisease = false, isPastDisease = false;
                    String disease = entry.getKey();
                    int symptoms = entry.getValue();
                    for (String name : healthAssistantAgent.getCurrentDiseases())
                        if (name.equals(disease) && symptoms > 0){
                            isCurrentDisease = true;
                            break;
                        }
                    if (!isCurrentDisease)
                        for (String name : healthAssistantAgent.getPastDiseases())
                            if (name.equals(disease) && symptoms > 0){
                                isPastDisease = true;
                                break;
                            }
                    if (isCurrentDisease && symptoms > 10) {
                        System.out.println(healthAssistantAgent.getLocalName() +": You have meds already");
                    }
                    if (isPastDisease && !isCurrentDisease && symptoms > 5) {
                        System.out.println(healthAssistantAgent.getLocalName() +": You need to go to doctor you had this in the past");
                        detectedProblems.add(disease);
                    }
                    if (!isCurrentDisease && !isPastDisease && symptoms > 10) {
                        System.out.println(healthAssistantAgent.getLocalName() +": You need to go to doctor we don't know what is wrong");
                        detectedProblems.add(disease);
                    }
                }
                step = 4;
            }
            if (step == 4){
                if (!detectedProblems.isEmpty()){
                    // TODO: 12.06.17 ask patient if he wants
                    healthAssistantAgent.addBehaviour(new HealthAssistantBehaviourMakeVisit(healthAssistantAgent, detectedProblems, 4800));
                }
            }
        }
    }
}