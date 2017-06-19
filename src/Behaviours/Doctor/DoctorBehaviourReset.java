package Behaviours.Doctor;

import Agents.Doctor.DoctorAgent;
import Schemes.Updates;
import jade.core.behaviours.TickerBehaviour;

/**
 * Created by anka on 12.06.17.
 */
public class DoctorBehaviourReset extends TickerBehaviour {
    private DoctorAgent doctorAgent;

    public DoctorBehaviourReset(DoctorAgent doctorAgent, long period) {
        super(doctorAgent, period);
        this.doctorAgent = doctorAgent;
    }

    @Override
    protected void onTick() {
        doctorAgent.updateMonthlyPatients(Updates.RESET);
        doctorAgent.updateRemainingPrescriptions(Updates.RESET);
    }
}
