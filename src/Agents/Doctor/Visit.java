package Agents.Doctor;

import Agents.Time.Date;
import jade.core.AID;

import java.io.Serializable;

/**
 * Created by anka on 26.04.17.
 */
public class Visit implements Serializable {
    private AID patient;
    private Date date;

    public Visit(AID patient, Date date) {
        this.patient = patient;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public AID getPatient() {
        return patient;
    }
}
