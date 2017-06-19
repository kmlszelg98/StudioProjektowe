package Behaviours.DataBase;

import Agents.DataBase.DataBaseAgent;
import Agents.Time.Date;
import VitalSigns.SignsObserved;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;

/**
 * Created by anka on 25.05.17.
 */
public class DataBaseBehaviourSave extends OneShotBehaviour {
    private DataBaseAgent dataBase;
    private AID patient;
    private SignsObserved signsObserved;
    private Date date;

    public DataBaseBehaviourSave(DataBaseAgent database, AID patient, SignsObserved signsObserved, Date date) {
        this.dataBase = database;
        this.patient = patient;
        this.signsObserved = signsObserved;
        this.date = date;
    }

    @Override
    public void action() {
        while(dataBase.getPatient(patient).getUsed()){}
        dataBase.getPatient(patient).setUsed(true);
        dataBase.getPatient(patient).addSignsObserved(signsObserved, date);
//        dataBase.getPatient(patient).printSigns();
        dataBase.getPatient(patient).setUsed(false);
    }
}
