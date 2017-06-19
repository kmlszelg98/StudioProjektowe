package Simulation;

import jade.wrapper.ContainerController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.TimerTask;

/**
 * Created by anka on 15.01.17.
 */
public class GUI extends JFrame {
    private Simulator simulator;
    private java.util.Timer timer;
    private TimerTask task;
    private int height;
    private int width;

    public GUI(ContainerController time, ContainerController database, ContainerController healthAssistants, ContainerController humans, ContainerController bands, ContainerController hospitals, ContainerController doctors, ContainerController ambulances){
        super("City");
        super.frameInit();
        this.getContentPane().setLayout(new CardLayout(0,0));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Setting bounds
        this.setBounds(0, 0, 1202, 930);

        //Simulator
        simulator = new Simulator(time, database, healthAssistants, humans, bands, hospitals, doctors, ambulances);
        getContentPane().add(simulator, "Simulator");

        task = new TimerTask() {
            public void run() {}
        };

        timer = new java.util.Timer();
        timer.schedule(task,0,1000/100);
        simulator.setVisible(true);
        this.setVisible(true);
    }


    // Exit
    public void processWindowEvent(WindowEvent e) {
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            this.dispose();
            System.out.print("\n\nQuitting the application!\n");
            System.exit(0);
        }
    }

    public int resize(double size){
        return (int) (height*size/1020);
    }
}



