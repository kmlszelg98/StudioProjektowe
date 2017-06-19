package Behaviours.Human;

import Agents.Human.HumanAgent;
import Schemes.Human.HumanStates;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;

/**
 * Created by anka on 23.05.17.
 */
public class HumanBehaviourMove extends TickerBehaviour {
    private HumanAgent human;

    public HumanBehaviourMove(HumanAgent human, long period) {
        super(human, period);
        this.human = human;
    }

    @Override
    protected void onTick() {
        if (human.getHumanState() != HumanStates.DEAD)
            myAgent.addBehaviour(new Moving());
        else
            stop();
    }
    
    private class Moving extends OneShotBehaviour {

        @Override
        public void action() {
            if (human.getTime() != null)
                human.move(human.getTime().getMinute());
        }
    }
}