package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ...
 *
 * @author Martin Wenzel
 */
public class StopWatch extends Subject {
    private int timetotake;
    private Timer clock;

    public StopWatch(int timetotakeinseconds) {
        this.clock = new Timer();
        this.timetotake = timetotakeinseconds * 1000;
    }
    public void startTimer(){
        clock.schedule(new RemindTask(),getTimetotake());
    }

    public int getTimetotake() {
        return timetotake;
    }
    private class RemindTask extends TimerTask{
        @Override
        public void run(){
            System.out.println("test test test");
            clock.cancel();
        }
    }
}
