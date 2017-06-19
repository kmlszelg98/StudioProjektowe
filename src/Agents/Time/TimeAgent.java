package Agents.Time;

import Behaviours.Time.TimeBehaviourUpdate;
import Schemes.Map.Colors;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;

import java.awt.*;

/**
 * Created by anka on 24.05.17.
 */
public class TimeAgent extends Agent {
    private int day;
    private int minute;

    public void setup(){
        // Jade description
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setType("time");
        sd.setName(this.getLocalName());
        dfd.addServices(sd);
        try {
            DFService.register(this, dfd);
        }
        catch (FIPAException fe) {
            fe.printStackTrace();
        }
        day = 0;
        minute = 0;
    }

    public void startBehaviours(){
        addBehaviour(new TimeBehaviourUpdate(this, 80));
    }

    public void update(){
        minute ++;
        if(minute == 24*60){
            this.day ++;
            this.minute = 0;
        }
    }

    public Date getDate() {
        return new Date(day, minute);
    }
    public int getDay() {
        return day;
    }
    public int getMinute() {
        return minute;
    }

    public void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        Font font = new Font("Serif", Font.BOLD, 20);
        g2.setFont(font);
        g2.setColor(Colors.DOCTOR);
        g2.drawString("Day " + day + " Hour " + minute/60 + ":" + minute%60, 980, 30);
    }
}