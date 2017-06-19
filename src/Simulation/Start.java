package Simulation;

/**
 * Created by anka on 08.04.17.
 */
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.wrapper.ContainerController;

public class Start extends Agent {
    protected void setup() {
        jade.core.Runtime runtime = jade.core.Runtime.instance();

        Profile profile = new ProfileImpl();
        profile.setParameter(Profile.CONTAINER_NAME, "Humans");
        profile.setParameter(Profile.MAIN_HOST, "localhost");

        Profile profile_2 = new ProfileImpl();
        profile_2.setParameter(Profile.CONTAINER_NAME, "Bands");
        profile_2.setParameter(Profile.MAIN_HOST, "localhost");

        Profile profile_3 = new ProfileImpl();
        profile_3.setParameter(Profile.CONTAINER_NAME, "Hospitals");
        profile_3.setParameter(Profile.MAIN_HOST, "localhost");

        Profile profile_4 = new ProfileImpl();
        profile_4.setParameter(Profile.CONTAINER_NAME, "Doctors");
        profile_4.setParameter(Profile.MAIN_HOST, "localhost");

        Profile profile_5 = new ProfileImpl();
        profile_5.setParameter(Profile.CONTAINER_NAME, "Ambulances");
        profile_5.setParameter(Profile.MAIN_HOST, "localhost");

        Profile profile_6 = new ProfileImpl();
        profile_6.setParameter(Profile.CONTAINER_NAME, "Time");
        profile_6.setParameter(Profile.MAIN_HOST, "localhost");

        Profile profile_7 = new ProfileImpl();
        profile_7.setParameter(Profile.CONTAINER_NAME, "DataBaseAgent");
        profile_7.setParameter(Profile.MAIN_HOST, "localhost");

        Profile profile_8 = new ProfileImpl();
        profile_8.setParameter(Profile.CONTAINER_NAME, "HealthAssistants");
        profile_8.setParameter(Profile.MAIN_HOST, "localhost");

        ContainerController humans = runtime.createAgentContainer(profile);
        ContainerController bands = runtime.createAgentContainer(profile_2);
        ContainerController hospitals = runtime.createAgentContainer(profile_3);
        ContainerController doctors = runtime.createAgentContainer(profile_4);
        ContainerController ambulances = runtime.createAgentContainer(profile_5);
        ContainerController time = runtime.createAgentContainer(profile_6);
        ContainerController database = runtime.createAgentContainer(profile_7);
        ContainerController healthAssistants = runtime.createAgentContainer(profile_8);

        //Mapka
        GUI gui = new GUI(time, database, healthAssistants, humans, bands, hospitals, doctors, ambulances);
    }
}