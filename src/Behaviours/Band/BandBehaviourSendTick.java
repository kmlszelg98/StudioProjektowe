package Behaviours.Band;

import Agents.Band.BandAgent;
import Agents.Human.HumanAgent;
import Schemes.Human.HumanStates;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

/**
 * Created by anka on 29.05.17.
 */
public class BandBehaviourSendTick extends TickerBehaviour {
    private BandAgent bandAgent;
    private HumanAgent humanAgent;

    public BandBehaviourSendTick(BandAgent bandAgent, long period) {
        super(bandAgent, period);
        this.bandAgent = bandAgent;
        this.humanAgent = bandAgent.getHuman();
    }

    @Override
    protected void onTick() {
        if (humanAgent.getHumanState() != HumanStates.DEAD)
            bandAgent.addBehaviour(new BandBehaviourSendOne(bandAgent, humanAgent.getSignsObserved(), humanAgent.getTime().getDate()));
        else {
            stop();
        }
    }
}
