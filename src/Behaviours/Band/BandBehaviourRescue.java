package Behaviours.Band;

import Agents.Band.BandAgent;
import Agents.Human.HumanAgent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.Random;

/**
 * Created by anka on 30.05.17.
 */
public class BandBehaviourRescue extends CyclicBehaviour {
    private BandAgent bandAgent;
    private HumanAgent humanAgent;
    private MessageTemplate mt;

    public BandBehaviourRescue(BandAgent bandAgent){
        this.bandAgent = bandAgent;
        this.humanAgent = bandAgent.getHuman();
    }

    @Override
    public void action() {
        // rescuing human simulates chances to survive
        mt = MessageTemplate.and(
                MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL),
                MessageTemplate.MatchConversationId("rescue"));

        ACLMessage rescue = myAgent.receive(mt);
        try {
            Thread.sleep(800);                  //they rescue for 10min
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (rescue != null) {
            ACLMessage call_reply = rescue.createReply();
            Random r = new Random();
            int success = r.nextInt(100);
            if (success > 20) {
                call_reply.setPerformative(ACLMessage.INFORM);
                bandAgent.setAlive();
                humanAgent.stabilize();
            } else {
                call_reply.setPerformative(ACLMessage.FAILURE);
                call_reply.setContent("died");
                bandAgent.setDead();
            }
            myAgent.send(call_reply);
        }
        else block();
    }
}
