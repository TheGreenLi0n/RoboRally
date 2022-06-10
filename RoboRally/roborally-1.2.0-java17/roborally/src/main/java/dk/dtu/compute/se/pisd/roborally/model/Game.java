package dk.dtu.compute.se.pisd.roborally.model;

/**
 *
 * @author Dennis Lolk Løvgreen
 */

public class Game {

    public int id;
    public Board board;
    public GameState gameState;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


}
