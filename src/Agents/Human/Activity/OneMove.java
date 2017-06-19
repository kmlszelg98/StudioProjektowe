package Agents.Human.Activity;

import java.awt.*;

/**
 * Created by Kamil on 10.04.2017.
 */
public class OneMove {

    private int start;
    private int end;
    private Point target;
    private Point place;

    public OneMove(int start, int end, Point target, Point place) {
        this.start = start;
        this.end = end;
        this.target = target;
        this.place = place;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public Point getTarget() {
        return target;
    }

    public void setTarget(Point target) {
        this.target = target;
    }

    public Point getPlace() {
        return place;
    }

    public void setPlace(Point place) {
        this.place = place;
    }

    public String toString(){
        return place+" "+start+" "+target+" "+end;
    }
}
