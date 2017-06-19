package Behaviours.Band;

import Agents.Band.BandAgent;
import Agents.Human.HumanAgent;
import Schemes.Human.HumanStates;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import static Functions.Distance.distanceV;
import static Functions.FromString.fromStringToInt;

/**
 * Created by anka on 29.05.17.
 */
public class BandBehaviourCallAmbulance extends TickerBehaviour {
    private BandAgent bandAgent;
    private HumanAgent humanAgent;


    public BandBehaviourCallAmbulance(BandAgent bandAgent, long period) {
        super(bandAgent, period);
        this.bandAgent = bandAgent;
        this.humanAgent = bandAgent.getHuman();
    }

    @Override
    protected void onTick() {
        if (humanAgent.getHumanState() != HumanStates.DEAD && bandAgent.isAmbulanceCalled() == false && bandAgent.isCalling() == false)
            myAgent.addBehaviour(new CallAmbulance());
        if (humanAgent.getHumanState() == HumanStates.DEAD || bandAgent.isAmbulanceCalled() == true)
            stop();
    }

    private class CallAmbulance extends OneShotBehaviour{
        private int step = 1;
        private AID[] hospitals;
        private AID bestHospital;
        private MessageTemplate mt;
        private double best;
        private int count;

        @Override
        public void action() {
            // CALLING HOSPITALS
            if (step == 1) {
                bandAgent.setCalling(true);
                System.out.println(bandAgent.getLocalName() + ": Calling hospitals");
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("hospital");
                template.addServices(sd);
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    hospitals = new AID[result.length];

                    for (int i = 0; i < result.length; i++) {
                        hospitals[i] = result[i].getName();
                    }
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }

                ACLMessage life_in_danger = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < hospitals.length; ++i) {
                    life_in_danger.addReceiver(hospitals[i]);
                }
                life_in_danger.setConversationId("life_in_danger");
                life_in_danger.setReplyWith("life_in_danger" + System.currentTimeMillis());
                myAgent.send(life_in_danger);

                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("life_in_danger"),
                        MessageTemplate.MatchInReplyTo(life_in_danger.getReplyWith()));
                step = 2;
            }

            // GETTING HOSPITALS RESPONSE
            if (step == 2) {
                System.out.println(bandAgent.getLocalName() + ": Finding the best hospital");
                while (true) {
                    ACLMessage hospital_info = myAgent.receive(mt);
                    if (hospital_info != null) {
                        if (hospital_info.getPerformative() == ACLMessage.PROPOSE) {
                            String content = hospital_info.getContent();
                            int tab[] = fromStringToInt(content);
                            double d = distanceV(tab[0], humanAgent.getLocation().getX(), tab[1], humanAgent.getLocation().getY());
                            if (bestHospital == null || d < best) {
                                best = d;
                                bestHospital = hospital_info.getSender();
                            }
                        }
                        count++;
                        if (count >= hospitals.length) {
                            step = 3;
                            break;
                        }
                    }
                }
            }

            // CALLING AMBULANCE
            if (step == 3) {
                if (bestHospital == null) {
                    System.out.println(bandAgent.getLocalName() + ": There is no ambulance available");
                } else {
                    System.out.println(bandAgent.getLocalName() + ": Calling ambulance from the " + bestHospital.getLocalName());
                    ACLMessage calling_ambulance = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    calling_ambulance.addReceiver(bestHospital);
                    calling_ambulance.setContent("calling_ambulance");
                    calling_ambulance.setConversationId("calling_ambulance");
                    calling_ambulance.setReplyWith("calling_ambulance" + System.currentTimeMillis());
                    myAgent.send(calling_ambulance);

                    mt = MessageTemplate.and(
                            MessageTemplate.MatchConversationId("calling_ambulance"),
                            MessageTemplate.MatchInReplyTo(calling_ambulance.getReplyWith()));
                    step = 4;
                }
            }

            // IF AMBULANCE IS COMING
            if (step == 4) {
                while (true) {
                    ACLMessage ambulance_response = myAgent.receive(mt);
                    if (ambulance_response != null) {
                        if (ambulance_response.getPerformative() == ACLMessage.INFORM)
                            bandAgent.setAmbulanceCalled(true);
                        bandAgent.setCalling(false);
                        break;
                    }
                }
            }
        }
    }
}
