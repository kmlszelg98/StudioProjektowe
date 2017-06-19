package Behaviours.Band;

import Agents.Band.BandAgent;
import Agents.Human.HumanAgent;
import Schemes.Human.HumanStates;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Created by anka on 26.05.17.
 */

public class BandBehaviourLocation extends CyclicBehaviour{
    private BandAgent bandAgent;
    private HumanAgent humanAgent;
    private MessageTemplate mt;

    public BandBehaviourLocation(BandAgent bandAgent){
        this.bandAgent = bandAgent;
        this.humanAgent = bandAgent.getHuman();
    }

    @Override
    public void action() {
            // sending info about human to ambulance
            mt = MessageTemplate.and(
                    MessageTemplate.MatchPerformative(ACLMessage.CFP),
                    MessageTemplate.MatchConversationId("asking_for_location"));

            ACLMessage location_request = myAgent.receive(mt);
            if (location_request != null) {
                ACLMessage location_reply = location_request.createReply();
                if (humanAgent.getHumanState() != HumanStates.DEAD) {
                    System.out.println(bandAgent.getLocalName() + ": I send human location to " + location_request.getSender().getLocalName());
                    location_reply.setPerformative(ACLMessage.PROPOSE);
                    location_reply.setContent(humanAgent.getLocation().getX() + ";" + humanAgent.getLocation().getY());
                }
                else {
                    System.out.println(bandAgent.getLocalName() + ": Human is already dead. Turn back " + location_request.getSender().getLocalName());
                    location_reply.setPerformative(ACLMessage.REFUSE);
                    location_reply.setContent("human_dead");
                }
                myAgent.send(location_reply);
            }
            else block();
    }
}
