package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

import java.security.PublicKey;

public class Antenna extends FieldAction {

    public int x;
    public int y;

    public Antenna(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }
}
