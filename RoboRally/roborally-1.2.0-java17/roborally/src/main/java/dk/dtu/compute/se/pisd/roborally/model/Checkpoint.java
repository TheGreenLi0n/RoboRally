package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;

/**
 *
 * @author Joakim Anker Kruse & Dennis Lolk Løvgreen
 *
 * This class is used for holding information about the checkpoints on the board
 */

public class Checkpoint extends FieldAction {

    public int x;
    public int y;

    public int checkpointNo;


    public Checkpoint(int checkpointNo, int x, int y){
        this.checkpointNo = checkpointNo;
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @param gameController the gameController of the respective game
     * @param space the space this action should be executed for
     * @return whether the action was successfully executed
     */
    @Override
    public boolean doAction(GameController gameController, Space space) {
        Player player = space.getPlayer();
        if (player != null){
            if (player.getReachedCheckpoint() + 1 == this.checkpointNo){
                player.setReachedCheckpoint(this.checkpointNo);
                if (player.getReachedCheckpoint() >= gameController.board.getCheckpoints().size()){
                    gameController.makeWinner(player);
                }
                return true;
            }
        }
        return false;
    }
}
