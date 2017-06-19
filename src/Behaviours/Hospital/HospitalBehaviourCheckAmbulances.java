package Behaviours.Hospital;

import Agents.Hospital.HospitalAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by Kamil on 13.05.2017.
 */

public class HospitalBehaviourCheckAmbulances extends CyclicBehaviour {
    private HospitalAgent hospitalAgent;

    public HospitalBehaviourCheckAmbulances(HospitalAgent hospitalAgent){
        this.hospitalAgent = hospitalAgent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchConversationId("life_in_danger"),
                MessageTemplate.MatchPerformative(ACLMessage.CFP));

        ACLMessage check_request = myAgent.receive(mt);

        if(check_request != null){
            ACLMessage check_reply = check_request.createReply();
            boolean check = hospitalAgent.checkAmbulance();
            if(check){
                check_reply.setPerformative(ACLMessage.PROPOSE);
                check_reply.setContent(hospitalAgent.getLocation().getX() + ";" + hospitalAgent.getLocation().getY());
            } else {
                check_reply.setPerformative(ACLMessage.REFUSE);
                check_reply.setContent("not-available");
            }
            myAgent.send(check_reply);
        } else {
            block();
        }
    }
}