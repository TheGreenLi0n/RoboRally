package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

public class StartSpace extends FieldAction
{
    int prio;

    public StartSpace(int prio){
        this.prio = prio;
    }

    public int getPrio() {
        return prio;
    }

    @Override
    public boolean doAction(GameController gameController, Space space) {
        return false;
    }
}
