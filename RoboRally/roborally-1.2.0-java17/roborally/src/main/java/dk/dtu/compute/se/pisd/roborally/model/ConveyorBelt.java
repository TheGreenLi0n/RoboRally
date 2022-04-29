package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;

/**
 * This class is used for holding information about the conveyor belts on the board.
 */

public class ConveyorBelt extends FieldAction {

    Heading heading;
    int speed;

    public ConveyorBelt(Heading heading, int speed){
        this.heading = heading;
        this.speed = speed;
    }

    public Heading getHeading() {
        return heading;
    }

    public int getSpeed() {
        return speed;
    }

    /**
     * The method for the green and blue conveyor belt fields that move the robot 1 and 2 spaces respectively.
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return whether the action was successfully executed
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null){
            Space neighborSpace = space.board.getNeighbour(space, heading);
            if(neighborSpace.getPlayer() == null){
                player.setSpace(neighborSpace);
                if (speed == 2) {
                    for (FieldAction action : neighborSpace.actions) {
                        Space newSpace = space.board.getNeighbour(neighborSpace,((ConveyorBelt) action).getHeading());
                        if(newSpace.getPlayer() == null) {
                            player.setSpace(newSpace);
                        }
                    }
                }
                return true;
            }
        }
        return false;
    }
}
