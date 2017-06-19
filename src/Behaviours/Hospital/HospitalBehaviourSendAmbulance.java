package Behaviours.Hospital;

import Agents.Ambulance.AmbulanceAgent;
import Agents.Hospital.HospitalAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by Kamil on 13.05.2017.
 */
public class HospitalBehaviourSendAmbulance extends CyclicBehaviour{
    private HospitalAgent hospitalAgent;

    public HospitalBehaviourSendAmbulance(HospitalAgent hospitalAgent){
        this.hospitalAgent = hospitalAgent;
    }

    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchConversationId("calling_ambulance"),
                MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));

        ACLMessage call_request = myAgent.receive(mt);

        if(call_request != null){
            ACLMessage call_reply = call_request.createReply();
            AmbulanceAgent ambulance = hospitalAgent.findAmbulance();
            if(ambulance!=null){
                call_reply.setPerformative(ACLMessage.INFORM);
                System.out.println(hospitalAgent.getLocalName() + " We are sending " + ambulance.getLocalName() + " to " + call_request.getSender().getLocalName());
                hospitalAgent.sendAmbulance(ambulance, call_request.getSender());
            } else {
                call_reply.setPerformative(ACLMessage.FAILURE);
                System.out.println(hospitalAgent.getLocalName() + ":" + call_request.getSender().getLocalName() + " I'm sorry we don't have ambulance for you");
                call_reply.setContent("not-available");
            }
            myAgent.send(call_reply);
        } else {
            block();
        }
    }
}
