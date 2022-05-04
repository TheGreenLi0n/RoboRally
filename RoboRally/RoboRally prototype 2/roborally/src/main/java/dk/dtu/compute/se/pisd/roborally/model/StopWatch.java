package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ...
 *
 * This class, is fortaking time during a players turn.
 * it's not quite implmented yet but works as proof of concept. 
 */
public class StopWatch extends Subject {
    private int timetotake;
    private Timer clock;

    public StopWatch(int timetotakeinseconds) {
        this.clock = new Timer();
        this.timetotake = timetotakeinseconds * 1000;
    }
    public void startTimer(){
        clock.schedule(new RemindTask(60),0,getTimetotake());

    }


    private class RemindTask extends TimerTask{
        private int times;
        private RemindTask(int times){
        this.times = times;
        }
        @Override
        public void run(){
            if (times == 0) clock.cancel();
            times--;
        }
    }
    public int getTimetotake() {
        return timetotake;
    }
}
