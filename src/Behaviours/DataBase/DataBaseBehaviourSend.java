package Behaviours.DataBase;

import Agents.DataBase.DataBaseAgent;
import VitalSigns.SignsObserved;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by anka on 24.05.17.
 */
public class DataBaseBehaviourSend extends OneShotBehaviour{
    private DataBaseAgent dataBaseAgent;
    private AID patient;
    private ACLMessage send_signs;

    public DataBaseBehaviourSend(DataBaseAgent dataBaseAgent, AID patient, ACLMessage reply){
        this.dataBaseAgent = dataBaseAgent;
        this.patient = patient;
        this.send_signs = reply;
    }

    @Override
    public void action() {
        if(dataBaseAgent.findPatient(patient)){
            send_signs.setPerformative(ACLMessage.PROPOSE);
            ArrayList<SignsObserved> signs = new ArrayList<>(dataBaseAgent.getPatient(patient).getSignsObserved().values());
            if (signs.size() > 20){
                List<SignsObserved> list =  signs.subList(signs.size()-21, signs.size()-1);
                ArrayList<SignsObserved> signsShort = new ArrayList<>(list.size());
                signsShort.addAll(list);
                try {
                    send_signs.setContentObject(signsShort);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    send_signs.setContentObject(signs);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            send_signs.setPerformative(ACLMessage.REFUSE);
        }
        myAgent.send(send_signs);
    }
}