package Agents.Time;

import java.io.Serializable;

/**
 * Created by anka on 24.05.17.
 */
public class Date implements Serializable{
    private int day;
    private int hour;

    public Date(int day, int hour) {
        this.day = day;
        this.hour = hour;
    }

    public Date() {
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    @Override
    public String toString() {
        return day + ";" + hour;
    }

    public boolean isBefore(Date date){
        // before
        if (date == null) return true;
        if (day < date.day) return true;
        if (day == date.day && hour < date.getHour()) return true;
        // after
        if (day > date.day) return false;
        if (day == date.day && hour > date.getHour()) return false;
        // the same
        return false;
    }
}
