package Behaviours.Ambulance;

import Agents.Ambulance.AmbulanceAgent;
import Schemes.Ambulance.AmbulanceStates;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by anka on 23.05.17.
 */

public class AmbulanceBehaviourFindPatient extends OneShotBehaviour {
    private AID patient;
    private AmbulanceAgent ambulanceAgent;
    private MessageTemplate mt;

    public AmbulanceBehaviourFindPatient(AmbulanceAgent ambulanceAgent, AID patient){
        this.patient = patient;
        this.ambulanceAgent = ambulanceAgent;
    }

    @Override
    public void action() {
        System.out.println(ambulanceAgent.getLocalName() + ": I search for " + patient.getLocalName());
        ACLMessage asking_for_location = new ACLMessage(ACLMessage.CFP);
        asking_for_location.addReceiver(patient);
        asking_for_location.setConversationId("asking_for_location");
        asking_for_location.setReplyWith("asking_for_location" + System.currentTimeMillis());
        asking_for_location.setContent("asking_for_location");
        myAgent.send(asking_for_location);

        mt = MessageTemplate.and(
                MessageTemplate.MatchConversationId("asking_for_location"),
                MessageTemplate.MatchInReplyTo(asking_for_location.getReplyWith()));

        while(true) {
            ACLMessage location = myAgent.receive(mt);
            if (location != null) {
                if (location.getPerformative() == ACLMessage.PROPOSE) {
                    String content = location.getContent();
                    System.out.println(ambulanceAgent.getLocalName() + ": I found human at " + content);
                    ambulanceAgent.target(content);
                    break;
                }
                else{
                    System.out.println(ambulanceAgent.getLocalName() + ": Patient already dead. Returning to hospital");
                    ambulanceAgent.returning();
                }
            }
        }
    }
}
