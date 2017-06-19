package Agents.Hospital;

import Agents.Time.Date;
import jade.core.AID;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by anka on 08.06.17.
 */

public class SuggestedDoctor implements Serializable {
    private AID doctor;
    private ArrayList<String> specializations;
    private Date date;

    public SuggestedDoctor(AID aid, ArrayList<String> specializations, Date date) {
        this.doctor = aid;
        this.specializations = specializations;
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public ArrayList<String> getSpecializations() {
        return specializations;
    }

    public AID getDoctor() {
        return doctor;
    }
}
