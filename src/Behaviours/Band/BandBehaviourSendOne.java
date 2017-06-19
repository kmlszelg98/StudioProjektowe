package Behaviours.Band;

import Agents.Band.BandAgent;
import Agents.Time.Date;
import VitalSigns.SignsObserved;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;

/**
 * Created by anka on 25.05.17.
 */


public class BandBehaviourSendOne extends OneShotBehaviour {
    private BandAgent bandAgent;
    private SignsObserved signsObserved;
    private Date date;
    private AID database;
    private MessageTemplate mt;
    private int step = 0;

    public BandBehaviourSendOne(BandAgent bandAgent, SignsObserved signsObserved, Date date){
        this.bandAgent = bandAgent;
        this.signsObserved = signsObserved;
        this.date = date;
    }

    @Override
    public void action() {
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
//            System.out.println(bandAgent.getLocalName() + ": Connecting with database ");
            ACLMessage connecting_db = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
            connecting_db.addReceiver(database);
            try {
                connecting_db.setContentObject(bandAgent.getHuman().getAID());
            } catch (IOException e) {
                e.printStackTrace();
            }
            connecting_db.setConversationId("connecting_db_save");
            connecting_db.setReplyWith("connecting_db_save" + System.currentTimeMillis());
            myAgent.send(connecting_db);
            mt = MessageTemplate.and(
                    MessageTemplate.MatchConversationId("connecting_db_save"),
                    MessageTemplate.MatchInReplyTo(connecting_db.getReplyWith()));
            step = 1;
        }
        if (step == 1){
            while (true) {
                ACLMessage db_response = myAgent.receive(mt);
                if (db_response != null) {
                    if (db_response.getPerformative() == ACLMessage.INFORM) {
//                        System.out.println(bandAgent.getLocalName()+ ": Sending information to database");
                        ACLMessage sendingSings = db_response.createReply();
                        sendingSings.setContent(signsObserved.toString() + ";" + date.toString());
                        myAgent.send(sendingSings);
                    } else {
//                        System.out.println(bandAgent.getLocalName() + ": Information can't be send to database");
                    }
                    break;
                }
            }
        }

    }
}
