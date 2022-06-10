package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Dennis Lolk Løvgreen, Joakim Anker Kruse & Noah Græne Surel
 */

class GameControllerTest {

    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;

    @BeforeEach
    void setUp() {
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        for (int i = 0; i < 6; i++) {
            Player player = new Player(board, null,"testPlayer");
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown() {
        gameController = null;
    }

    @Test
    void moveCurrentPlayerToSpace() {
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));

        Assertions.assertEquals(player1, board.getSpace(0, 4).getPlayer(), "Player " + player1.getName() + " should beSpace (0,4)!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertEquals(player2, board.getCurrentPlayer(), "Current player should be " + player2.getName() +"!");
    }

    @Test
    void moveForward() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward(current);

        Assertions.assertEquals(current, board.getSpace(0, 1).getPlayer(), "Player " + current.getName() + " should be Space (0,1)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
    }

    @Test
    void moveForward2() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward2(current);

        Assertions.assertEquals(current, board.getSpace(0, 2).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");
    }

    @Test
    void moveForward3() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.moveForward3(current);

        Assertions.assertEquals(current, board.getSpace(0, 3).getPlayer(), "Player " + current.getName() + " should beSpace (0,2)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");
        Assertions.assertNull(board.getSpace(0, 0).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertNull(board.getSpace(0, 1).getPlayer(), "Space (0,1) should be empty!");
        Assertions.assertNull(board.getSpace(0, 2).getPlayer(), "Space (0,2) should be empty!");
    }

    @Test
    void turnRight(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnRight(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.WEST, current.getHeading(), "Player 0 should be heading WEST!");
    }

    @Test
    void turnLeft(){
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.turnLeft(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.EAST, current.getHeading(), "Player 0 should be heading EAST!");
    }

    @Test
    void moveBack() {
        Board board = gameController.board;
        Player current = board.getPlayer(0);

        gameController.moveForward(current);
        gameController.moveBack(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading SOUTH!");

    }

    @Test
    void uTurn() {
        Board board = gameController.board;
        Player current = board.getCurrentPlayer();

        gameController.uTurn(current);

        Assertions.assertEquals(current, board.getSpace(0, 0).getPlayer(), "Player " + current.getName() + " should beSpace (0,0)!");
        Assertions.assertEquals(Heading.NORTH, current.getHeading(), "Player 0 should be heading NORTH!");
    }

    @Test
    void pushPlayer(){
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(2, 4));
        gameController.moveCurrentPlayerToSpace(board.getSpace(2, 5));
        gameController.moveForward(player1);

        Assertions.assertEquals(player1, board.getSpace(2, 5).getPlayer(), "Player " + player1.getName() + " should be Space (2,5)!");
        Assertions.assertNull(board.getSpace(2, 4).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertEquals(player2, board.getSpace(2, 6).getPlayer(), "Player " + player2.getName() + " should be Space (2,6)!");
    }

    @Test
    void checkpointFunctionality() {
        Board board = gameController.board;
        Checkpoint checkpoint1 = new Checkpoint(1, 2, 2);
        Checkpoint checkpoint2 = new Checkpoint(2, 3, 2);
        Antenna antenna = new Antenna(4,4);
        board.getSpace(4,4).setAntenna(antenna);
        board.addCheckpoint(checkpoint1);
        board.addCheckpoint(checkpoint2);

        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        player1.setHeading(Heading.SOUTH);
        player2.setHeading(Heading.SOUTH);
        gameController.moveCurrentPlayerToSpace(board.getSpace(2, 1));
        gameController.moveCurrentPlayerToSpace(board.getSpace(3, 1));
        player1.getProgramField(1).setCard(new CommandCard(Command.FORWARD_1));
        player2.getProgramField(1).setCard(new CommandCard(Command.FORWARD_1));
        gameController.finishProgrammingPhase();
        gameController.executePrograms();

        Assertions.assertEquals(1, player1.getReachedCheckpoint(), "Player 1 should have completed checkpoint 1!");
        Assertions.assertNotEquals(2, player2.getReachedCheckpoint(), "Player 2 should not have completed any checkpoint");
    }

    @Test
    void makeWinner(){
        Board board = gameController.board;
        Checkpoint checkpoint1 = new Checkpoint(1,2,2);
        Checkpoint checkpoint2 = new Checkpoint(2,2,3);
        Antenna antenna = new Antenna(4,4);
        board.getSpace(4,4).setAntenna(antenna);
        board.addCheckpoint(checkpoint1);
        board.addCheckpoint(checkpoint2);

        Player player1 = board.getPlayer(0);

        gameController.moveCurrentPlayerToSpace(board.getSpace(2, 1));
        player1.getProgramField(1).setCard(new CommandCard(Command.FORWARD_1));
        player1.getProgramField(2).setCard(new CommandCard(Command.FORWARD_1));
        gameController.finishProgrammingPhase();
        gameController.executePrograms();


        Assertions.assertEquals(2, player1.getReachedCheckpoint(), "The player should have reached all checkpoints (2)");
        Assertions.assertTrue(gameController.winner, "We should have found a winner!");

    }

    @Test
    void ConveyorBeltMove(){
        Board board = gameController.board;
        ConveyorBelt conveyorBelt1 = new ConveyorBelt(Heading.EAST,2);
        ConveyorBelt conveyorBelt2 = new ConveyorBelt(Heading.NORTH,2);
        Antenna antenna = new Antenna(4,4);
        board.getSpace(4,4).setAntenna(antenna);
        board.getSpace(6,2).setConveyorBelt(conveyorBelt1);
        board.getSpace(6,3).setConveyorBelt(conveyorBelt2);

        Player current = board.getPlayer(0);

        gameController.moveCurrentPlayerToSpace(board.getSpace(6, 4));

        current.getProgramField(1).setCard(new CommandCard(Command.MOVE_BACK));
        gameController.finishProgrammingPhase();
        gameController.executePrograms();

        Assertions.assertEquals(current, board.getSpace(7, 2).getPlayer(), "Player " + current.getName() + " should be on space (7,2)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading South!");
    }


    @Test
    void pushOnConveyorBelt(){
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(2, 4));
        gameController.moveCurrentPlayerToSpace(board.getSpace(2, 5));
        gameController.moveForward(player1);

        Assertions.assertEquals(player1, board.getSpace(2, 5).getPlayer(), "Player " + player1.getName() + " should be Space (2,5)!");
        Assertions.assertNull(board.getSpace(2, 4).getPlayer(), "Space (0,0) should be empty!");
        Assertions.assertEquals(player2, board.getSpace(2, 6).getPlayer(), "Player " + player2.getName() + " should be Space (2,6)!");

    }

    @Test
    void wallStopMove(){
        Board board = gameController.board;

        Antenna antenna = new Antenna(4,4);
        board.getSpace(4,4).setAntenna(antenna);
        board.getSpace(7,2).walls.add(Heading.NORTH);

        Player current = board.getPlayer(0);

        gameController.moveCurrentPlayerToSpace(board.getSpace(7, 2));

        current.getProgramField(1).setCard(new CommandCard(Command.MOVE_BACK));
        gameController.finishProgrammingPhase();
        gameController.executePrograms();

        Assertions.assertEquals(current, board.getSpace(7, 2).getPlayer(), "Player " + current.getName() + " should be on space (7,2)!");
        Assertions.assertEquals(Heading.SOUTH, current.getHeading(), "Player 0 should be heading South!");
    }

    @Test
    void laserStopOnHit(){
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);
        Antenna antenna = new Antenna(4,4);
        board.getSpace(4,4).setAntenna(antenna);

        board.getSpace(4,1).walls.add(Heading.NORTH);

        gameController.moveCurrentPlayerToSpace(board.getSpace(4, 0));
        gameController.moveCurrentPlayerToSpace(board.getSpace(4, 2));

        gameController.finishProgrammingPhase();
        gameController.executePrograms();

        Assertions.assertEquals(0,player2.getDamage(), "player " + player2.getName() + "should not have taken damage! " );
    }

    @Test
    void laserDmgPlayer(){
        Board board = gameController.board;
        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);
        Antenna antenna = new Antenna(4,4);
        board.getSpace(4,4).setAntenna(antenna);


        gameController.moveCurrentPlayerToSpace(board.getSpace(4, 0));
        gameController.moveCurrentPlayerToSpace(board.getSpace(4, 2));

        gameController.finishProgrammingPhase();
        gameController.executePrograms();

        player1.getDamage();
        Assertions.assertEquals(5,player2.getDamage(), "player " + player2.getName() + "should have taken 5 damage! " );
    }

    @Test
    void gearsRotation(){
        Board board = gameController.board;
        Gear gearRed = new Gear("Red");
        Gear gearGreen = new Gear("Green");
        board.getSpace(0,4).setGear(gearRed);
        board.getSpace(3,5).setGear(gearGreen);
        Antenna antenna = new Antenna(4,4);
        board.getSpace(4,4).setAntenna(antenna);

        Player player1 = board.getPlayer(0);
        Player player2 = board.getPlayer(1);

        gameController.moveCurrentPlayerToSpace(board.getSpace(0, 4));
        gameController.moveCurrentPlayerToSpace(board.getSpace(3, 5));
        player1.getProgramField(1).setCard(new CommandCard(Command.FORWARD_1));
        player2.getProgramField(1).setCard(new CommandCard(Command.FORWARD_1));
        gameController.finishProgrammingPhase();
        gameController.executePrograms();

        Assertions.assertEquals(Heading.EAST, player1.getHeading());
        Assertions.assertEquals(Heading.NORTH, player2.getHeading());
    }
}