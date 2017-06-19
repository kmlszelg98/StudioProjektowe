package Behaviours.Hospital;

import Agents.Hospital.HospitalAgent;
import Agents.Hospital.SuggestedDoctor;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by anka on 08.06.17.
 */
public class HospitalBehaviourCheckDoctors extends CyclicBehaviour {
    private HospitalAgent hospitalAgent;

    public HospitalBehaviourCheckDoctors(HospitalAgent hospitalAgent){
        this.hospitalAgent = hospitalAgent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchConversationId("need_visit"),
                MessageTemplate.MatchPerformative(ACLMessage.CFP));

        ACLMessage check_request = myAgent.receive(mt);
        if(check_request != null){
            ACLMessage check_reply = check_request.createReply();
            ArrayList<SuggestedDoctor> suggestedDoctors = hospitalAgent.checkDoctors();
            if(!suggestedDoctors.isEmpty()){
                check_reply.setPerformative(ACLMessage.PROPOSE);
                try {
                    check_reply.setContentObject(suggestedDoctors);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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