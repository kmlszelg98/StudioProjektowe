package Behaviours.Band;

import Agents.Band.BandAgent;
import Agents.Human.HumanAgent;
import Schemes.Human.HumanStates;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;

/**
 * Created by Kamil on 13.05.2017.
 */
public class BandBehaviourCheck extends TickerBehaviour {
    private HumanAgent humanAgent;
    private BandAgent bandAgent;

    public BandBehaviourCheck(BandAgent band, long period) {
        super(band, period);
        bandAgent = band;
        this.humanAgent = band.getHuman();
    }

    @Override
    protected void onTick() {
        if (humanAgent.getHumanState() != HumanStates.DEAD)
            myAgent.addBehaviour(new SymptomsPerformer());
        else
            stop();
    }

    private class SymptomsPerformer extends OneShotBehaviour {

        @Override
        public void action() {
            if (humanAgent.getHumanState() != HumanStates.DEAD) {
                bandAgent.updateSigns(humanAgent.getSignsObserved());
                // TIME IN_DANGER
                if (bandAgent.isInDanger() == true && bandAgent.isAmbulanceCalled() == false){
                    int waiting = bandAgent.getWaiting();
                    if (waiting > 2) {
                        System.out.println(bandAgent.getLocalName() + ": " + humanAgent.getLocalName() + " waited to long for help and died");
                        bandAgent.setDead();
                    }
                    else {
                        waiting++;
                        bandAgent.setWaiting(waiting);
                    }
                }

                // IF HUMAN IS OK AND WORKING CHECK
                if (bandAgent.isInDanger() == false) {
                    if (humanAgent.getSignsObserved().checkIfInDanger()) {
                        System.out.println(bandAgent.getLocalName() + ": Human is dying");
                        bandAgent.setInDanger();
                        bandAgent.addBehaviour(new BandBehaviourCallAmbulance(bandAgent, 80));
                        bandAgent.addBehaviour(new BandBehaviourSendOne(bandAgent, humanAgent.getSignsObserved(), humanAgent.getTime().getDate()));
                    }
                }
            }
        }
    }
}