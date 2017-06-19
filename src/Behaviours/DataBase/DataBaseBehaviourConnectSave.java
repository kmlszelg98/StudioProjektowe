package Behaviours.DataBase;

import Agents.DataBase.DataBaseAgent;
import Agents.Time.Date;
import VitalSigns.SignsObserved;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * Created by anka on 24.05.17.
 */
public class DataBaseBehaviourConnectSave extends CyclicBehaviour {
    private DataBaseAgent dataBaseAgent;
    private int step;
    private MessageTemplate mt;
    private AID patient;

    public DataBaseBehaviourConnectSave(DataBaseAgent dataBaseAgent){
        this.dataBaseAgent = dataBaseAgent;
    }

    @Override
    public void action() {
        mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
                MessageTemplate.MatchConversationId("connecting_db_save"));
        ACLMessage call_request = myAgent.receive(mt);
        step = 1;
        if(call_request!=null){
            if (step == 1) {
                ACLMessage connection_reply = call_request.createReply();
                Object human = null;
                try {
                    human = call_request.getContentObject();
                } catch (UnreadableException e) {
                    e.printStackTrace();
                }
                patient = (AID) human;
                boolean check = dataBaseAgent.findPatient(patient);
                if (check) {
                    connection_reply.setPerformative(ACLMessage.INFORM);
//                    System.out.println(dataBaseAgent.getLocalName() + " Connected to account of " + patient.getLocalName() + " waiting for data");
                } else {
                    connection_reply.setPerformative(ACLMessage.FAILURE);
//                    System.out.println(dataBaseAgent.getLocalName() + ":" + call_request.getSender().getLocalName() + " Connection failed");
                    connection_reply.setContent("not-available");
                }
                connection_reply.setConversationId("connecting_db_save");
                connection_reply.setReplyWith("connecting_db_save" + human);
                myAgent.send(connection_reply);
                mt = MessageTemplate.and(
                        MessageTemplate.MatchConversationId("connecting_db_save"),
                        MessageTemplate.MatchInReplyTo(connection_reply.getReplyWith()));
                step = 2;
            }
            if (step == 2){
                while (true) {
                    ACLMessage save_signs = myAgent.receive(mt);
                    if (save_signs != null) {
                        String[] content = save_signs.getContent().split(";");
                        String[] signs = {content[0], content[1], content[2]};
                        SignsObserved signsObserved = new SignsObserved(signs);
                        Date date = new Date(Integer.parseInt(content[3]), Integer.parseInt(content[4]));
                        dataBaseAgent.saveSigns(patient, signsObserved, date);
                        break;
                    }
                }
            }
        } else {
            block();
        }
    }
}