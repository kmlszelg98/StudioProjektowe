package Agents.Human.Activity;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.lang.Math.abs;

/**
 * Created by Kamil on 09.04.2017.
 */
public class HumanWeek {

    private HashMap<Integer,Point> day;
    private List<OneMove> one;
    private Point home;

    public HumanWeek(Point home){
        day = new HashMap<>();
        day.put(0,home);
        one = new ArrayList<>();
        one.add(new OneMove(0,0,home,home));
        this.home=home;
        // = new ArrayList<>();
        //places = new ArrayList<>();
    }

    public void addPoint(int minute,int x, int y){

        //day.put(minute,point);
        Point p = one.get(one.size()-1).getTarget();
        Point p2 = new Point(x,y);
        //if(p==null) System.out.println("Null");
        //System.out.println(p.x);
        //System.out.println(point.x);

        int d = abs(p.x-x)+abs(p.y-y);
        d= d/60;
        one.add(new OneMove(minute,minute+d,p2,p));
        day.put(minute,p2);

    }

    public String toString() {
        String s = "";
        for (OneMove move : one) {
            s = s + "\n" + move.getPlace() + " :" + move.getStart() + move.getTarget() + " :" + move.getEnd();
            s = s + "\n godzina" + move.getStart() / 3600 + ":" + (move.getStart() % 3600) / 60;
        }
        return s;
    }

    public OneMove getLast(){
        return one.get(one.size()-1);
    }

    public void setHome(Point home) {
        this.home = home;
    }

    public Point getHome() {
        return home;
    }

    public HashMap<Integer, Point> getDay() {
        return day;
    }

    public void setDay(HashMap<Integer, Point> day) {
        this.day = day;
    }

    public List<OneMove> getOne() {
        return one;
    }

    public void setOne(List<OneMove> one) {
        this.one = one;
    }
}
