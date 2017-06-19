package Behaviours.DataBase;

import Agents.DataBase.DataBaseAgent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * Created by anka on 05.06.17.
 */
public class DataBaseBehaviourConnectSend extends CyclicBehaviour{
    private DataBaseAgent dataBaseAgent;
    private MessageTemplate mt;
    private AID patient;

    public DataBaseBehaviourConnectSend(DataBaseAgent dataBaseAgent){
        this.dataBaseAgent = dataBaseAgent;
    }

    @Override
    public void action() {
        mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.CFP),
                MessageTemplate.MatchConversationId("connecting_db_send"));

        ACLMessage call_request = myAgent.receive(mt);
        if(call_request != null){
            Object human = null;
            try {
                human = call_request.getContentObject();
            } catch (UnreadableException e) {
                e.printStackTrace();
            }
            patient = (AID) human;
            dataBaseAgent.sendSigns(patient, call_request.createReply());
        } else {
            block();
        }
    }
}