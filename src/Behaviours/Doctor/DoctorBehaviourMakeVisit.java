package Behaviours.Doctor;

import Agents.Ambulance.AmbulanceAgent;
import Agents.Doctor.DoctorAgent;
import Agents.Doctor.Visit;
import Agents.Time.Date;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * Created by anka on 12.06.17.
 */
public class DoctorBehaviourMakeVisit extends CyclicBehaviour {
    private DoctorAgent doctorAgent;

    public DoctorBehaviourMakeVisit(DoctorAgent doctorAgent){
        this.doctorAgent = doctorAgent;
    }

    @Override
    public void action() {
        MessageTemplate mt = MessageTemplate.and(
                MessageTemplate.MatchConversationId("making_visit"),
                MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));

        ACLMessage call_request = myAgent.receive(mt);
        if(call_request != null){
            ACLMessage call_reply = call_request.createReply();
            Object content = null;
            try {
                content = call_request.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            Visit visit = (Visit) content;
            boolean check = doctorAgent.addVisit(visit);
            if(check){
                call_reply.setPerformative(ACLMessage.INFORM);
                System.out.println(doctorAgent.getLocalName() + ": Visit made for " + visit.getPatient().getLocalName() + " for " + visit.getDate());
            } else {
                call_reply.setPerformative(ACLMessage.FAILURE);
                System.out.println(doctorAgent.getLocalName() + ": " + visit.getPatient().getLocalName() + " I'm sorry this date is taken");
                call_reply.setContent("not-available");
            }
            myAgent.send(call_reply);
        } else {
            block();
        }
    }
}
