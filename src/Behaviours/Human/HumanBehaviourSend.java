package Behaviours.Human;

import Agents.Human.HumanAgent;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Created by anka on 25.05.17.
 */
public class HumanBehaviourSend extends OneShotBehaviour {
    private HumanAgent humanAgent;
    private boolean done;
    private String information;

    public HumanBehaviourSend(HumanAgent humanAgent, String information) {
        this.humanAgent = humanAgent;
        this.done = done;
        this.information = information;
    }

    @Override
    public void action() {
        // TODO: 25.05.17 take information and based on variable done send message to db
    }
}
