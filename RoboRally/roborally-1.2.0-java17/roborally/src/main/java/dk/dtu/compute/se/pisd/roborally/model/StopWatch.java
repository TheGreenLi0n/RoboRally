package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * ...
 *
 * This class, primary use is for keeping track of player time and how often the lobby should refresh to check if all
 * players have joined .
 * it's not quite implmented yet but works as proof of concept for the player timing.
 */
public class StopWatch extends Subject {
    private int timetotake;
    private Timer clock;

    public StopWatch(int timetotakeinseconds) {
        this.clock = new Timer();
        this.timetotake = timetotakeinseconds * 1000;
    }

    /**
     * lets the timer cycle 60 times with timetotake seconds in between
     */
    public void startTimer(){
        clock.schedule(new RemindTask(60),0,getTimetotake());

    }


    private class RemindTask extends TimerTask{
        private int times;

        private RemindTask(int times){
        this.times = times;
        }

        /**
         * Counts down the timer. If the timer equals 0 the clock will be stopped.
         */
        @Override
        public void run(){
            if (times == 0) clock.cancel();
            times--;

        }
    }

    /**
     *
     * @return the time left on the clock.
     */
    public int getTimetotake() {
        return timetotake;
    }
}
