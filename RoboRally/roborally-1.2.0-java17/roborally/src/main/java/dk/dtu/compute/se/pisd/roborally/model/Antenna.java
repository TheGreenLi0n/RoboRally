package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class Antenna extends FieldAction {

    public int x;
    public int y;


    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }
}
