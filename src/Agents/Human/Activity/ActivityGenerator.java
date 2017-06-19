package Agents.Human.Activity;

import java.util.Random;

import static java.lang.Math.abs;

/**
 * Created by Kamil on 10.04.2017.
 */
public class ActivityGenerator {

    private int range = 24*60;
    private int back = 20*60;


    public void createActivity(HumanWeek week){

        Random generator = new Random();
        OneMove move = week.getLast();
        int end = move.getEnd();


        int x = generator.nextInt(1200);
        int y = generator.nextInt(900);
        int d = abs(x-move.getTarget().x)+abs(y-move.getTarget().y);
        //System.out.println( " ->"+end+" >>"+(end+d));
        d = d/60;

        if((end+d)>back){
            int time = generator.nextInt(range-end)+end;
            week.addPoint(time,week.getHome().x,week.getHome().y);
        } else {
            int time;
            time = generator.nextInt(range-end-d)+end;

            if (time>(end+4*60)) time = end+4*60;
            if(end==0 && time==4*60) {
                time=generator.nextInt(240)+(4*60);
            }
            week.addPoint(time,x,y);
        }
    }
}