package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

public class Antenna extends Subject {

    private int x;
    private int y;
    private int[] cords;

    public Antenna(int x, int y)
    {
        cords = new int[]{x,y};
    }

    public int[] getCords() {
        return cords;
    }
}
