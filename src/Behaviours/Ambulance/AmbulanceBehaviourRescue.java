package Behaviours.Ambulance;

import Agents.Ambulance.AmbulanceAgent;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by anka on 23.05.17.
 */
public class AmbulanceBehaviourRescue extends OneShotBehaviour {
    private AID patient;
    private AmbulanceAgent ambulanceAgent;
    private MessageTemplate mt;

    public AmbulanceBehaviourRescue(AmbulanceAgent ambulanceAgent, AID patient){
        this.patient = patient;
        this.ambulanceAgent = ambulanceAgent;
    }

    @Override
    public void action() {
        System.out.println(ambulanceAgent.getLocalName() + ": I try to rescue patient of " + patient.getLocalName());
        ACLMessage rescue = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
        rescue.addReceiver(patient);
        rescue.setContent("rescue");
        rescue.setConversationId("rescue");
        rescue.setReplyWith("rescue" + System.currentTimeMillis());
        myAgent.send(rescue);

        mt = MessageTemplate.and(
                MessageTemplate.MatchConversationId("rescue"),
                MessageTemplate.MatchInReplyTo(rescue.getReplyWith()));

        while (true) {
            ACLMessage success = myAgent.receive(mt);
            if (success != null) {
                if (success.getPerformative() == ACLMessage.INFORM) {
                    System.out.println(ambulanceAgent.getLocalName() + ": Rescued patient of " + patient.getLocalName() + ". Going back to hospital");
                } else {
                    System.out.println(ambulanceAgent.getLocalName() + ": Patient of " + patient.getLocalName() + " died...sorry. Going back to hospital");
                }
                ambulanceAgent.returning();
                break;
            }
        }
    }
}
