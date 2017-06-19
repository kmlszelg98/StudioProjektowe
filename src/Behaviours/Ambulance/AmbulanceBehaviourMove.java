package Behaviours.Ambulance;

import Agents.Ambulance.AmbulanceAgent;
import Schemes.Ambulance.AmbulanceStates;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;

/**
 * Created by anka on 23.05.17.
 */
public class AmbulanceBehaviourMove extends TickerBehaviour {
    private AmbulanceAgent ambulance;

    public AmbulanceBehaviourMove(AmbulanceAgent ambulance, long period) {
        super(ambulance, period);
        this.ambulance = ambulance;
    }

    @Override
    protected void onTick() {
        if (ambulance.getAmbulanceState() != AmbulanceStates.WAITING)
            myAgent.addBehaviour(new Moving());
    }

    private class Moving extends OneShotBehaviour {
        @Override
        public void action() {
            if (ambulance.getTarget() != null) {
                ambulance.move();
            }
        }
    }
}