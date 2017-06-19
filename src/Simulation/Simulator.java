package Simulation;

import jade.wrapper.ContainerController;

import javax.swing.*;
import java.awt.*;

public class Simulator extends JPanel {
    private Generator generator;

    public Simulator(ContainerController time, ContainerController database, ContainerController healthAssistants, ContainerController humans, ContainerController bands, ContainerController hospitals, ContainerController doctors, ContainerController ambulances){
        this.generator = new Generator(time, database, healthAssistants, humans, bands, hospitals, doctors, ambulances);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        generator.draw(g);
        repaint();
    }
}