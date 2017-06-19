package Behaviours.Human;

import Agents.Human.HumanAgent;
import Schemes.Human.HumanStates;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;

/**
 * Created by anka on 06.06.17.
 */
public class HumanBehaviourTakePills extends TickerBehaviour {
    private HumanAgent human;

    public HumanBehaviourTakePills(HumanAgent human, long period) {
        super(human, period);
        this.human = human;
    }

    @Override
    protected void onTick() {
        if (human.getHumanState() != HumanStates.DEAD)
            myAgent.addBehaviour(new TakePills());
        else
            stop();
    }

    private class TakePills extends OneShotBehaviour {
        @Override
        public void action() {
            human.takePills();
        }
    }
}
