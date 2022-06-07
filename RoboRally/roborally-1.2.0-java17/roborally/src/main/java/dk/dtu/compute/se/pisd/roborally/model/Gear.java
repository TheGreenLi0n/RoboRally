package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import javafx.scene.transform.Rotate;

/**
 * This class is used for holding information about the Gears on the board.
 */

public class Gear extends FieldAction {

    String color;

    public Gear(String color ){
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    /**
     * The method for the green and red gear fields that turn the robot to the right or left respectively.
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return whether the action was successfully executed
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null){
            if (color.equals("Red")){
                player.setHeading(player.getHeading().prev());
                return true;
            }
            else if (color.equals("Green")){
                player.setHeading(player.getHeading().next());
                return true;
            }
            else
                return false;
        }
        return false;
    }
}
