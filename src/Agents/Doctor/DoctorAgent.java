package Agents.Doctor;

import Agents.DataBase.DataBaseAgent;
import Agents.Time.Date;
import Behaviours.Doctor.DoctorBehaviourAdvice;
import Behaviours.Doctor.DoctorBehaviourMakeVisit;
import Behaviours.Doctor.DoctorBehaviourMove;
import Agents.Time.TimeAgent;
import Behaviours.Doctor.DoctorBehaviourReset;
import Schemes.Doctor.Specializations;
import Schemes.Map.Colors;
import Schemes.Updates;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by anka on 24.04.17.
 */
public class DoctorAgent extends Agent {
    // location
    private Point location;
    private Point home;
    private Point target;
    private Point hospital;
    // states
    private int startMinute = 480, endMinute = 960, visitsPerDay = 4;
    private ArrayList<String> specializations;
    private ArrayList<Visit> visits;
    private int monthlyPatients;            //number of clients in 7 days
    private int remainingPrescriptions;     //starts with 10?

    private TimeAgent time;
    private DataBaseAgent dataBase;

    public void setup(){
        // Jade description
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("doctor");
        sd.setName(this.getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        this.location = new Point();
        this.home = new Point();
        this.hospital = new Point();

        visits = new ArrayList<>();
        monthlyPatients = 0;
        remainingPrescriptions = 10;
        Random r = new Random();
        specializations = new ArrayList<>();
    }

    public void startBehaviours() {
        addBehaviour(new DoctorBehaviourMove(this, 10));
        addBehaviour(new DoctorBehaviourReset(this, 806400));
        addBehaviour(new DoctorBehaviourAdvice(this));
        addBehaviour(new DoctorBehaviourMakeVisit(this));
    }

    public void updateMonthlyPatients(int update) {
        if (update == Updates.RESET) monthlyPatients = 0;
        else monthlyPatients = monthlyPatients + update;
    }

    public void updateRemainingPrescriptions(int update){
        if (update == Updates.RESET) remainingPrescriptions = 10;
        else remainingPrescriptions = remainingPrescriptions + update;
    }

    public Date suggestVisit(){
        if (monthlyPatients < 15 && remainingPrescriptions > 0){
            Date suggestion = new Date();
            if (visits.isEmpty()){
                suggestion.setDay(time.getDay() + 1);
                suggestion.setHour(startMinute);
            }
            else {
                int lastDay = visits.get(visits.size() - 1).getDate().getDay();             // TODO: 12.06.17 there is possible exception - nullPointer kills hospital
                if (lastDay <= time.getDay()){
                    suggestion.setDay(time.getDay() + 1);
                    suggestion.setHour(startMinute);
                }
                else {
                    int visitsInDay = 0;
                    for (Visit visit : visits)
                        if (visit.getDate().getDay() == lastDay) visitsInDay++;
                    if (visitsInDay < visitsPerDay) {
                        suggestion.setDay(lastDay);
                        suggestion.setHour(visits.get(visits.size() - 1).getDate().getHour() + ((endMinute - startMinute) / visitsPerDay));
                    } else {
                        suggestion.setDay(lastDay + 1);
                        suggestion.setHour(startMinute);
                    }
                }
            }
            return suggestion;
        }
        else {
            System.out.println(this.getLocalName() + ": No more patients");
            return null;
        }
    }

    public boolean addVisit(Visit visite) {
        for (Visit visit : visits)
            if (visit.getDate().getDay() == visite.getDate().getDay() && visit.getDate().getHour() == visite.getDate().getHour()) return false;
        visits.add(visite);
        updateMonthlyPatients(Updates.ADD);
        return true;
    }

    public void move(int minute) {
        if (minute > 420 && minute < 980)
            target = hospital;
        if (minute < 420 || minute > 980)
            target = home;
        if (location.x != target.x || location.y != target.y )
            go();
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
        Font font = new Font("Serif", Font.PLAIN, 9);
        g2.setFont(font);
        g2.setColor(Colors.DOCTOR);
        g2.drawString(this.getLocalName(), location.x, location.y);
        g2.fillRect(location.x - 2,location.y - 2, 4, 4);
    }

    public void setLocation(Point location) {
        Random r = new Random();
        this.hospital.x = location.x + r.nextInt(41) - 20;
        this.hospital.y = location.y + r.nextInt(41) - 20;
        this.home.x = hospital.x + r.nextInt(301) - 150;
        this.home.y = hospital.y + r.nextInt(301) - 150;
        this.location.x = home.x;
        this.location.y = home.y;
        this.target = home;
    }
    public TimeAgent getTime() {
        return time;
    }
    public void setTime(TimeAgent time) {
        this.time = time;
    }
    public DataBaseAgent getDataBase() {
        return dataBase;
    }
    public void setDataBase(DataBaseAgent dataBase) {
        this.dataBase = dataBase;
    }
    public ArrayList<String> getSpecializations() {
        return specializations;
    }
    public void setSpecializations(String specialization) {
        this.specializations.add(specialization);
    }
}