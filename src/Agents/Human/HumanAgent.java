package Agents.Human;

import Agents.DataBase.HumanAccount;
import Agents.Human.Activity.ActivityGenerator;
import Agents.Human.Activity.HumanWeek;
import Behaviours.Human.HumanBehaviourMove;
import Behaviours.Human.HumanBehaviourTakePills;
import Agents.Time.TimeAgent;
import Behaviours.Human.HumanBehaviourGenerateSigns;
import Agents.Doctor.Prescription;
import Schemes.Human.Actions;
import Schemes.Human.HumanStates;
import Schemes.Map.Colors;
import Schemes.Human.Names;
import Schemes.Medication.Pill;
import Schemes.Medication.SymptomsOfDiseases;
import VitalSigns.SignsGenerator;
import VitalSigns.SignsObserved;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Created by anka on 09.04.2017.
 */
public class HumanAgent extends Agent {
    // Location
    private Point location;
    private Point home;
    private Point target;
    // States
    private String name;
    private int humanState;
    private HumanWeek week;
    private int action;                                                  // TODO: 24.05.17 impact on generatedSings
    private int age;                                                     // TODO: 10.06.17 impact on generatedSigns
    // Medication
    private ArrayList<String> diseases;                                  // TODO: 24.05.17 impact on generatedSings
    private HashMap<Pill, Integer> pillsLeft;
    private ArrayList<Prescription> prescriptions;
    // Signs
    private SignsGenerator signsGenerator;
    private SignsObserved signsObserved;
    // Agents
    private TimeAgent time;
    private HumanAccount account;

    public void setup(){
        // Jade description
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("human");
        sd.setName(this.getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }

        Random r = new Random();
        // Location
        this.location = new Point(r.nextInt(1190) + 10, r.nextInt(890) + 10);
        this.home = new Point(location);
        this.target = home;
        // States
        this.action = Actions.SITTING;
        this.humanState = HumanStates.WORKING;
        this.name = Names.Names[r.nextInt(Names.Names.length)];
        this.age = r.nextInt(90) + 10;
        this.week = new HumanWeek(home);
        generateWeek();
        // Medication
        diseases = new ArrayList<>();
        pillsLeft = new HashMap<>();
        prescriptions = new ArrayList<>();
        // Signs
        signsGenerator = new SignsGenerator();
        signsObserved = new SignsObserved(signsGenerator.generateSigns());
    }

    public void startBehaviours(){
        addBehaviour(new HumanBehaviourMove(this, 10));
        addBehaviour(new HumanBehaviourGenerateSigns(this, 1600));      //20min
        addBehaviour(new HumanBehaviourTakePills(this, 57600));
    }

    public void generateWeek() {
        ActivityGenerator ac = new ActivityGenerator();
        int hx = this.getHome().x;
        int hy = this.getHome().y;
        do{
            ac.createActivity(this.week);
        } while(this.week.getLast().getTarget().x!=hx && this.week.getLast().getTarget().y!=hy);
    }

    public void move(int time){
        if (week.getDay().get(time) != null)
            target = week.getDay().get(time);
        if (humanState != HumanStates.IN_DANGER && humanState != HumanStates.DEAD)
            go();
    }

    public void generateVitalSigns(){
        signsObserved = new SignsObserved(signsGenerator.generateSigns());
    }

    public void takePills() {
        ArrayList<Prescription> toRemove = new ArrayList<>();
        ArrayList<String> toStabilize = new ArrayList<>();

        for (Prescription prescription : prescriptions){
            int time = prescription.getTime();
            time--;
            prescription.setTime(time);
            if (prescription.getTime() <= 0)
                toRemove.add(prescription);
            else
                for (Map.Entry<Pill, Integer> entry : pillsLeft.entrySet()){
                    if (entry.getKey().getName().equals(prescription.getPill().getName()))
                        if (entry.getValue() > 0)
                            toStabilize.add(prescription.getPill().getStabilizes());
                }
        }
        if (!toRemove.isEmpty())
            for(Prescription prescription : toRemove)
                prescriptions.remove(prescription);
        if (!toStabilize.isEmpty())
            signsGenerator.stabilizeSigns(toStabilize);
    }

    public void writeComments() {
        Random r = new Random();
        boolean mayBeSick;
        int goodWill = r.nextInt(100);
        if (goodWill < 5) {
            for (Map.Entry<String, ArrayList<String>> entry : SymptomsOfDiseases.observedByBand.entrySet()) {
                mayBeSick = false;
                String disease = entry.getKey();
                ArrayList<String> symptoms = entry.getValue();
                for (String symptom : symptoms)
                    if (signsObserved.getPulse().equals(symptom) || signsObserved.getTemperature().equals(symptom) || signsObserved.getPressure().equals(symptom)){
                        mayBeSick = true;
                        break;
                    }
                if (mayBeSick && r.nextInt(100) < 50){
                    ArrayList<String> symptomsOfDisease = SymptomsOfDiseases.observedByHuman.get(disease);
                    account.addSignsEntered(symptomsOfDisease.get(r.nextInt(symptomsOfDisease.size()-1)), time.getDate());
                }
            }
        }
    }

    public void go(){
        if(location.x!=target.x){
            if(location.y!=target.y){
                location.x= (location.x>target.x) ? location.x-1 : location.x+1;
                location.y= (location.y>target.y) ? location.y-1 : location.y+1;
            } else {
                location.x= (location.x>target.x) ? location.x-1 : location.x+1;
            }
        } else {
            if(location.y!=target.y){
                location.y= (location.y>target.y) ? location.y-1 : location.y+1;
            } else {
                location.x = target.x;
                location.y = target.y;
            }
        }
    }

    public void draw(Graphics g){
        Graphics2D g2 = (Graphics2D)g;
        if (humanState == HumanStates.DEAD) g2.setColor(Colors.HUMAN_DEAD);
        else if (humanState == HumanStates.IN_DANGER) g2.setColor(Colors.HUMAN_IN_DANGER);
        else g2.setColor(Colors.HUMAN);
        Font font = new Font("Serif", Font.PLAIN, 9);
        g2.setFont(font);
        g2.drawString(this.getLocalName(), location.x, location.y);
        g2.fillRect(location.x - 2,location.y - 2, 4, 4);
    }
    
    public Point getLocation() {
        return location;
    }
    public int getHumanState() {
        return humanState;
    }
    public Point getHome() {
        return home;
    }
    public TimeAgent getTime() {
        return time;
    }

    public void setHumanState(int humanState) {
        this.humanState = humanState;
        if (this.humanState == HumanStates.DEAD)
            account.setAlive(false);
    }
    public void setTime(TimeAgent time) {
        this.time = time;
    }
    public void setAccount(HumanAccount account) {
        this.account = account;
    }
    public SignsObserved getSignsObserved() {
        return signsObserved;
    }

    public void stabilize() {
        signsGenerator.stabilizeAll();
        signsObserved.stabilize();
    }
}