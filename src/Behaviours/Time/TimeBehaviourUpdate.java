package Behaviours.Time;

import Agents.Time.TimeAgent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;

/**
 * Created by anka on 24.05.17.
 */
public class TimeBehaviourUpdate extends TickerBehaviour{
    private TimeAgent time;

    public TimeBehaviourUpdate(TimeAgent time, long period) {
        super(time, period);
        this.time = time;
    }

    @Override
    protected void onTick() {
        myAgent.addBehaviour(new Update());
    }

    private class Update extends OneShotBehaviour {
        @Override
        public void action() {
            time.update();
        }
    }
}
