package Behaviours.Human;

import Agents.Human.HumanAgent;
import Schemes.Human.HumanStates;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;

/**
 * Created by anka on 29.05.17.
 */

public class HumanBehaviourGenerateSigns extends TickerBehaviour {
    private HumanAgent human;

    public HumanBehaviourGenerateSigns(HumanAgent human, long period) {
        super(human, period);
        this.human = human;
    }

    @Override
    protected void onTick() {
        if (human.getHumanState() != HumanStates.DEAD)
            myAgent.addBehaviour(new GenerateSigns());
        else
            stop();
    }

    private class GenerateSigns extends OneShotBehaviour {

        @Override
        public void action() {
            human.generateVitalSigns();
            human.writeComments();
        }
    }
}
