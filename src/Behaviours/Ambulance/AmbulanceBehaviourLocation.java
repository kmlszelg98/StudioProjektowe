package Behaviours.Ambulance;

import Agents.Ambulance.AmbulanceAgent;
import Schemes.Ambulance.AmbulanceStates;
import Schemes.Human.HumanStates;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by anka on 26.05.17.
 */
public class AmbulanceBehaviourLocation extends CyclicBehaviour {
    private AmbulanceAgent ambulanceAgent;
    private MessageTemplate mt;

    public AmbulanceBehaviourLocation(AmbulanceAgent ambulanceAgent) {
        this.ambulanceAgent = ambulanceAgent;
    }

    @Override
    public void action() {
        // sending info about location and state to hospital
        mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.CFP),
                MessageTemplate.MatchConversationId("asking_for_location"));

        ACLMessage location_request = myAgent.receive(mt);
        if (location_request != null) {
            ACLMessage location_reply = location_request.createReply();
            if (ambulanceAgent.getAmbulanceState() == AmbulanceStates.WAITING || ambulanceAgent.getAmbulanceState() == AmbulanceStates.RETURNING) {
                location_reply.setPerformative(ACLMessage.PROPOSE);
                location_reply.setContent("now;" + ambulanceAgent.getLocation().getX() + ";" + ambulanceAgent.getLocation().getY());
            }
            else if (ambulanceAgent.getAmbulanceState() == AmbulanceStates.RESCUING && ambulanceAgent.getNextPatient() == null){
                location_reply.setPerformative(ACLMessage.PROPOSE);
                location_reply.setContent("later;" + ambulanceAgent.getLocation().getX() + ";" + ambulanceAgent.getLocation().getY() );
            }
            else {
                location_reply.setPerformative(ACLMessage.REFUSE);
                location_reply.setContent("not available");
            }
            myAgent.send(location_reply);
        }
        else block();
    }
}
