package Behaviours.HealthAssistent;

import Agents.Doctor.Visit;
import Agents.HealthAssistant.HealthAssistantAgent;
import Agents.Hospital.SuggestedDoctor;
import Agents.Time.Date;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static Functions.Distance.distanceV;
import static Functions.FromString.fromStringToInt;

/**
 * Created by anka on 06.06.17.
 */
public class HealthAssistantBehaviourMakeVisit extends TickerBehaviour {
    private HealthAssistantAgent healthAssistant;
    private ArrayList<String> detectedProblems;

    public HealthAssistantBehaviourMakeVisit(HealthAssistantAgent healthAssistantAgent, ArrayList<String> detectedProblems, long period) {
        super(healthAssistantAgent, period);
        this.healthAssistant = healthAssistantAgent;
        this.detectedProblems = detectedProblems;
    }

    @Override
    protected void onTick() {
        if (healthAssistant.isMakingVisit() == false && healthAssistant.isVisitMade() == false)
            myAgent.addBehaviour(new MakeVisit());
        if (healthAssistant.isVisitMade() == true) {
            healthAssistant.setMakingVisit(false);
            healthAssistant.setVisitMade(false);
            stop();
        }
    }

    private class MakeVisit extends OneShotBehaviour {
        private ArrayList<SuggestedDoctor> suggestedDoctors = new ArrayList<>();
        private int step = 1, count = 0;
        private AID[] hospitals;
        private SuggestedDoctor bestDoctor;
        private Date bestDate = null;
        private int mostKnowledge;
        private MessageTemplate mt;

        @Override
        public void action() {
            // CALLING HOSPITALS
            if (step == 1) {
                System.out.println(healthAssistant.getLocalName() + ": Calling hospitals");
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("hospital");
                template.addServices(sd);
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    hospitals = new AID[result.length];

                    for (int i = 0; i < result.length; i++) {
                        hospitals[i] = result[i].getName();
                    }
                } catch (FIPAException fe) {
                    fe.printStackTrace();
                }

                ACLMessage life_in_danger = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < hospitals.length; ++i) {
                    life_in_danger.addReceiver(hospitals[i]);
                }
                life_in_danger.setConversationId("need_visit");
                life_in_danger.setReplyWith("need_visit" + System.currentTimeMillis());
                myAgent.send(life_in_danger);

                mt = MessageTemplate.and(MessageTemplate.MatchConversationId("need_visit"),
                        MessageTemplate.MatchInReplyTo(life_in_danger.getReplyWith()));
                step = 2;
            }

            // GETTING HOSPITALS RESPONSE
            if (step == 2) {
                System.out.println(healthAssistant.getLocalName() + ": Finding the best doctor");
                while (true) {
                    ACLMessage doctor_info = myAgent.receive(mt);
                    if (doctor_info != null) {
                        if (doctor_info.getPerformative() == ACLMessage.PROPOSE) {
                            Object content = null;
                            try {
                                content = doctor_info.getContentObject();
                            } catch (UnreadableException e) {
                                e.printStackTrace();
                            }
                            ArrayList<SuggestedDoctor> newSuggestedDoctors = (ArrayList<SuggestedDoctor>) content;
                            suggestedDoctors.addAll(newSuggestedDoctors);
                        }
                        count++;
                        if (count >= hospitals.length) {
                            step = 3;
                            break;
                        }
                    }
                }
                if(!suggestedDoctors.isEmpty()) {
                    if (detectedProblems.size() == 1 && detectedProblems.get(0) == "notClassified") {
                        for (SuggestedDoctor suggestedDoctor : suggestedDoctors) {
                            if (suggestedDoctor.getDate().isBefore(bestDate))
                                bestDoctor = suggestedDoctor;
                        }
                    } else {
                        int[] knowledge = new int[suggestedDoctors.size()];
                        for (SuggestedDoctor suggestedDoctor : suggestedDoctors) {
                            for (String disease : detectedProblems)
                                for (String specialization : suggestedDoctor.getSpecializations())
                                    if (disease.equals(specialization))
                                        knowledge[suggestedDoctors.indexOf(suggestedDoctor)]++;
                        }
                        mostKnowledge = Arrays.stream(knowledge).max().getAsInt();
                        for (SuggestedDoctor suggestedDoctor : suggestedDoctors) {
                            if (knowledge[suggestedDoctors.indexOf(suggestedDoctor)] == mostKnowledge)
                                if (suggestedDoctor.getDate().isBefore(bestDate))
                                    bestDoctor = suggestedDoctor;
                        }
                    }
                }
            }
            // MAKING VISIT
            if (step == 3) {
                if (bestDoctor == null) {
                    System.out.println(healthAssistant.getLocalName() + ": There is no doctor available");
                }
                // TODO: 12.06.17 ask patient if ok
                else {
                    System.out.println(healthAssistant.getLocalName() + ": Making visit with " + bestDoctor.getDoctor().getLocalName() + " of " + bestDoctor.getSpecializations() + " for " + detectedProblems);
                    ACLMessage making_visit = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    making_visit.addReceiver(bestDoctor.getDoctor());
                    Visit visit = new Visit(healthAssistant.getPatient(), bestDoctor.getDate());
                    try {
                        making_visit.setContentObject(visit);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    making_visit.setConversationId("making_visit");
                    making_visit.setReplyWith("making_visit" + System.currentTimeMillis());
                    myAgent.send(making_visit);

                    mt = MessageTemplate.and(
                            MessageTemplate.MatchConversationId("making_visit"),
                            MessageTemplate.MatchInReplyTo(making_visit.getReplyWith()));
                    step = 4;
                }
            }
            // IF VISIT MADE
            if (step == 4) {
                while (true) {
                    ACLMessage doctor_response = myAgent.receive(mt);
                    if (doctor_response != null) {
                        if (doctor_response.getPerformative() == ACLMessage.INFORM)
                            healthAssistant.setVisitMade(true);
                        healthAssistant.setMakingVisit(false);
                        break;
                    }
                }
            }
        }
    }
}