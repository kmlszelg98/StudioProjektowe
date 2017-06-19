package Behaviours.Doctor;

import Agents.Doctor.DoctorAgent;
import Behaviours.Human.HumanBehaviourMove;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;

/**
 * Created by anka on 25.05.17.
 */
public class DoctorBehaviourMove extends TickerBehaviour {
    private DoctorAgent doctorAgent;

    public DoctorBehaviourMove(DoctorAgent doctorAgent, long period) {
        super(doctorAgent, period);
        this.doctorAgent = doctorAgent;
    }

    @Override
    protected void onTick() {
        myAgent.addBehaviour(new Moving());
    }

    private class Moving extends OneShotBehaviour {

        @Override
        public void action() {
            if (doctorAgent.getTime() != null)
                doctorAgent.move(doctorAgent.getTime().getMinute());
        }
    }
}
