/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.scene.control.Alert;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Dennis Lolk Løvgreen, Martin Wenzel, Joakim Anker Kruse, Simon Simonsen & Noah Grænge Surel
 */
public class GameController {

    final public Board board;
    public boolean winner = false;
    private LinkedList<Player> playerOrder = new LinkedList<>();
    private int playerNum;

    public GameController(@NotNull Board board) {
        this.board = board;
    }

    /**
     * This is just some dummy controller operation to make a simple move to see something
     * happening on the board. This method should eventually be deleted!
     *
     * @param space the space to which the current player should move
     */
    public void moveCurrentPlayerToSpace(@NotNull Space space) {
        // TODO Assignment V1: method should be implemented by the students:
        //   - the current player should be moved to the given space
        //     (if it is free()
        //   - and the current player should be set to the player
        //     following the current player
        //   - the counter of moves in the game should be increased by one
        //     if the player is moved

        if (space != null && space.board == board) {
            Player currentPlayer = board.getCurrentPlayer();
            if (currentPlayer != null && space.getPlayer() == null) {
                currentPlayer.setSpace(space);
                int playerNumber = (board.getPlayerNumber(currentPlayer) + 1) % board.getPlayersNumber();
                board.setCurrentPlayer(board.getPlayer(playerNumber));
            }
        }

    }

    // XXX: V2

    /**
     * Method for starting the programming phase. Primarily uses the board and player class.
     */
    public void startProgrammingPhase() {
        setPlayerPrio();
        board.setPhase(Phase.PROGRAMMING);
        board.setStep(0);

        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            if (player != null) {
                for (int j = 0; j < Player.NO_REGISTERS; j++) {
                    CommandCardField field = player.getProgramField(j);
                    field.setCard(null);
                    field.setVisible(true);
                }
                for (int j = 0; j < Player.NO_CARDS; j++) {
                    CommandCardField field = player.getCardField(j);
                    field.setCard(generateRandomCommandCard());
                    field.setVisible(true);
                }
            }
        }
    }

    // XXX: V2

    /**
     * Method for generating random command cards.
     * @return CommandCard
     */
    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    // XXX: V2

    /**
     * Method for finishing the programming phase.
     */
    public void finishProgrammingPhase() {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        setPlayerPrio();
        board.setStep(0);
    }

    // XXX: V2

    /**
     * Method for making program fields visible.
     * @param register
     */
    private void makeProgramFieldsVisible(int register) {
        if (register >= 0 && register < Player.NO_REGISTERS) {
            for (int i = 0; i < board.getPlayersNumber(); i++) {
                Player player = board.getPlayer(i);
                CommandCardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    // XXX: V2

    /**
     * Method for making programming fields invisible.
     */
    private void makeProgramFieldsInvisible() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++) {
                CommandCardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    // XXX: V2

    /**
     * executes all the programs in the register of the players in the correct order, while not in stepmode.
     */
    public void executePrograms() {
        board.setStepMode(false);
        continuePrograms();
    }

    // XXX: V2

    /**
     * Executes the register step by step in correct order while in stepmode.
     */
    public void executeStep() {
        board.setStepMode(true);
        continuePrograms();
    }

    // XXX: V2

    /**
     * While in activation phase this method executes the next step of current player's register.
     */
    private void continuePrograms() {
        do {
            executeNextStep();
        } while (board.getPhase() == Phase.ACTIVATION && !board.isStepMode());
    }

    // XXX: V2

    /**
     * executes the next step of the current players register in the activation phase.
     */
    private void executeNextStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                CommandCard card = currentPlayer.getProgramField(step).getCard();
                if (card != null) {
                    Command command = card.command;
                    if (command.isInteractive()) {
                        board.setPhase(Phase.PLAYER_INTERACTION);
                        return;
                    }
                    executeCommand(currentPlayer, command);
                }
                if (playerNum < playerOrder.size() - 1) {
                    playerNum++;
                    board.setCurrentPlayer(playerOrder.get(playerNum));
                } else {
                    for (int i = 0; i < board.getPlayersNumber(); i++) {
                        for (FieldAction action : board.getPlayer(i).getSpace().getActions()) {
                            action.doAction(this, board.getPlayer(i).getSpace());
                        }
                    }
                    playerNum = 0;
                    robotLaser();
                    setPlayerPrio();
                    step++;
                    if (step < Player.NO_REGISTERS) {
                        makeProgramFieldsVisible(step);
                        board.setStep(step);
                    } else {
                        startProgrammingPhase();
                    }
                }
            } else {
                // this should not happen
                assert false;
            }
        } else {
            // this should not happen
            assert false;
        }
    }

    /**
     * Executes the command card that a player has chosen in their programming register.
     *
     * @param player  is the player that is to be executing the command.
     * @param command is the command card that the player is executing.
     */
    private void executeCommand(@NotNull Player player, Command command) {
        if (player != null && player.board == board && command != null) {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command) {
                case FORWARD_1:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FORWARD_2:
                    this.moveForward2(player);
                    break;
                case U_TURN:
                    this.uTurn(player);
                    break;
                case MOVE_BACK:
                    this.moveBack(player);
                    break;
                case FORWARD_3:
                    this.moveForward3(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

    /**
     * A command that makes the player move 1 space forward on the board.
     *
     * @param player - the player that is executing the command.
     */
    public void moveForward(@NotNull Player player) {
        Space space = player.getSpace();

        if (player != null && player.board == board && space != null) {
            Heading heading = player.getHeading();
            Space target = board.getNeighbour(space, heading);
            if (target != null) {
                if (target.getPlayer() != null) {
                    Player targetPlayer = target.getPlayer();
                    Space pushTarget = board.getNeighbour(targetPlayer.getSpace(), heading);
                    if (pushTarget.getPlayer() != null) {
                        Player secondtarget = pushTarget.getPlayer();
                        Space secondPushtarget = board.getNeighbour(secondtarget.getSpace(), heading);
                        if (secondPushtarget.getPlayer() != null) {
                            Player thirdTarget = secondPushtarget.getPlayer();
                            Space thirdPushTarget = board.getNeighbour(thirdTarget.getSpace(), heading);
                            if (thirdPushTarget.getPlayer() != null) {
                                Player fourthtarget = thirdPushTarget.getPlayer();
                                Space fourthpushtarget = board.getNeighbour(fourthtarget.getSpace(), heading);
                                if (fourthpushtarget.getPlayer() != null) {
                                    Player fifthtarget = fourthpushtarget.getPlayer();
                                    Space fifthpushtarget = board.getNeighbour(fifthtarget.getSpace(), heading);
                                    if (fifthpushtarget.getWalls().contains(player.getHeading().prev().prev()) || fifthtarget.getSpace().getWalls().contains(player.getHeading())) {
                                        return;
                                    }
                                    if (fifthpushtarget != null) {
                                        fifthtarget.setSpace(fifthpushtarget);
                                    }
                                }
                                if (fourthpushtarget.getWalls().contains(player.getHeading().prev().prev()) || fourthtarget.getSpace().getWalls().contains(player.getHeading())) {
                                    return;
                                }
                                if (fourthpushtarget != null) {
                                    fourthtarget.setSpace(fourthpushtarget);
                                }

                            }
                            if (thirdPushTarget.getWalls().contains(player.getHeading().prev().prev()) || thirdTarget.getSpace().getWalls().contains(player.getHeading())) {
                                return;
                            }
                            if (thirdPushTarget != null) {
                                thirdTarget.setSpace(thirdPushTarget);
                            }
                        }
                        if (secondPushtarget.getWalls().contains(player.getHeading().prev().prev()) || secondtarget.getSpace().getWalls().contains(player.getHeading())) {
                            return;
                        }
                        if (secondPushtarget != null) {
                            secondtarget.setSpace(secondPushtarget);
                        }
                    }
                    if (pushTarget.getWalls().contains(player.getHeading()) || pushTarget.getWalls().contains(player.getHeading().prev().prev())) {
                        return;
                    }
                    if (pushTarget != null) {
                        targetPlayer.setSpace(pushTarget);
                    }

                }
                if (target.getWalls().contains(player.getHeading()) || player.getSpace().getWalls().contains(player.getHeading().prev().prev())) {
                    return;
                }
                // XXX note that this removes an other player from the space, when there
                //     is another player on the target. Eventually, this needs to be
                //     implemented in a way so that other players are pushed away!
                target.setPlayer(player);
            }
        }
    }

    /**
     * A command that makes the player move 2 spaces forward on the board.
     *
     * @param player - the player that is executing the command.
     */
    public void moveForward2(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
    }

    /**
     * A command that makes the player move 3 spaces forward on the board.
     *
     * @param player - the player that is executing the command.
     */
    public void moveForward3(@NotNull Player player) {
        moveForward(player);
        moveForward(player);
        moveForward(player);
    }

    /**
     * A command that makes the player turn right (clockwise) on the board.
     *
     * @param player - the player that is executing the command.
     */
    public void turnRight(@NotNull Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().next());
        }
    }

    /**
     * A command that makes the player turn left (counterclockwise) on the board.
     *
     * @param player - the player that is executing the command.
     */
    public void turnLeft(@NotNull Player player) {
        if (player != null && player.board == board) {
            player.setHeading(player.getHeading().prev());
        }
    }

    /**
     * A command that makes the player turn 180 degrees on the board..
     *
     * @param player - the player that is executing the command.
     */
    public void uTurn(@NotNull Player player) {
        if (player != null && player.board == board) {
            turnRight(player);
            turnRight(player);
        }
    }

    /**
     * A command that makes the player move one space backwards without changing the direction the player is facing on the board.
     *
     * @param player - the player that is executing the command.
     */
    public void moveBack(@NotNull Player player) {
        Space space = player.getSpace();
        if (player != null && player.board == board && space != null) {
            Heading heading = player.getHeading().prev().prev();
            Space target = board.getNeighbour(space, heading);
            if (target != null) {

                if (target.getPlayer() != null) {
                    Player targetPlayer = target.getPlayer();
                    Space pushTarget = board.getNeighbour(targetPlayer.getSpace(), heading);
                    if (pushTarget.getPlayer() != null) {
                        Player secondtarget = pushTarget.getPlayer();
                        Space secondPushtarget = board.getNeighbour(secondtarget.getSpace(), heading);
                        if (secondPushtarget.getPlayer() != null) {
                            Player thirdTarget = secondPushtarget.getPlayer();
                            Space thirdPushTarget = board.getNeighbour(thirdTarget.getSpace(), heading);
                            if (thirdPushTarget.getPlayer() != null) {
                                Player fourthtarget = thirdPushTarget.getPlayer();
                                Space fourthpushtarget = board.getNeighbour(fourthtarget.getSpace(), heading);
                                if (fourthpushtarget.getPlayer() != null) {
                                    Player fifthtarget = fourthpushtarget.getPlayer();
                                    Space fifthpushtarget = board.getNeighbour(fifthtarget.getSpace(), heading);
                                    if (fifthpushtarget.getWalls().contains(player.getHeading()) || fifthtarget.getSpace().getWalls().contains(player.getHeading().prev().prev())) {
                                        return;
                                    }
                                    if (fifthpushtarget != null) {
                                        fifthtarget.setSpace(fifthpushtarget);
                                    }
                                }
                                if (fourthpushtarget.getWalls().contains(player.getHeading()) || fourthtarget.getSpace().getWalls().contains(player.getHeading().prev().prev())) {
                                    return;
                                }
                                if (fourthpushtarget != null) {
                                    fourthtarget.setSpace(fourthpushtarget);
                                }

                            }
                            if (thirdPushTarget.getWalls().contains(player.getHeading()) || thirdTarget.getSpace().getWalls().contains(player.getHeading().prev().prev())) {
                                return;
                            }
                            if (thirdPushTarget != null) {
                                thirdTarget.setSpace(thirdPushTarget);
                            }
                        }
                        if (secondPushtarget.getWalls().contains(player.getHeading()) || secondtarget.getSpace().getWalls().contains(player.getHeading().prev().prev())) {
                            return;
                        }
                        if (secondPushtarget != null) {
                            secondtarget.setSpace(secondPushtarget);
                        }
                    }
                    if (pushTarget.getWalls().contains(player.getHeading()) || pushTarget.getWalls().contains(player.getHeading().prev().prev())) {
                        return;
                    }
                    if (pushTarget != null) {
                        targetPlayer.setSpace(pushTarget);
                    }

                }
                if (target.getWalls().contains(player.getHeading()) || player.getSpace().getWalls().contains(player.getHeading().prev().prev())) {
                    return;
                }
                // XXX note that this removes an other player from the space, when there
                //     is another player on the target. Eventually, this needs to be
                //     implemented in a way so that other players are pushed away!
                target.setPlayer(player);
            }
        }
    }

    /**
     * The method for drag and dropping cards into the register and reverse.
     * @param source
     * @param target
     * @return returns a boolean depending on the action happened or not
     */
    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null && targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }
    }

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented() {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

    /**
     * executes A Command And Then Continues the programming phase.
     *
     * @param command A command in the current players register that should be executed.
     */
    public void executeCommandAndContinue(Command command) {
        Player currentPlayer = board.getCurrentPlayer();
        int step = board.getStep();
        executeCommand(currentPlayer, command);
        board.setPhase(Phase.ACTIVATION);
        if (playerNum < playerOrder.size() - 1) {
            playerNum++;
            board.setCurrentPlayer(playerOrder.get(playerNum));
        } else {
            playerNum = 0;
            robotLaser();
            setPlayerPrio();
            step++;
            if (step < Player.NO_REGISTERS) {
                makeProgramFieldsVisible(step);
                board.setStep(step);
            } else {
                startProgrammingPhase();
            }
        }
        if (!board.isStepMode()) {
            continuePrograms();
        }
    }

    /**
     * Fires a laser from everyone in their headed direction, if it hits a player then the laser stops and the target that got hit
     * takes 1 damage.
     * If the laser hits a wall before it hits a player, then the laser stops.
     */
    public void robotLaser() {
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Heading direction = board.getPlayer(i).getHeading();
            Space position = board.getPlayer(i).getSpace();

            switch (direction) {
                case NORTH -> {
                    for (int j = position.y; j >= 0; j--) {
                        if (board.getSpace(position.x, j).getWalls() != null && board.getSpace(position.x, j).getWalls().contains(Heading.NORTH) && board.getSpace(position.x, j) == position) {
                            break;
                        }
                        if (board.getSpace(position.x, j).getWalls() != null && board.getSpace(position.x, j).getWalls().contains(Heading.SOUTH) && board.getSpace(position.x, j) != position) {
                            break;
                        } else if (board.getSpace(position.x, j).getPlayer() != null && board.getSpace(position.x, j).getPlayer() != board.getPlayer(i)) {
                            board.getSpace(position.x, j).getPlayer().dealDamage();
                            break;
                        } else if (board.getSpace(position.x, j).getWalls() != null && board.getSpace(position.x, j).getWalls().contains(Heading.NORTH)) {
                            break;
                        }
                    }
                    break;
                }
                case EAST -> {
                    for (int j = position.x; j <= board.width - 1; j++) {
                        if (board.getSpace(j, position.y).getWalls() != null && board.getSpace(j, position.y).getWalls().contains(Heading.EAST) && board.getSpace(j, position.y) == position) {
                            break;
                        }
                        if (board.getSpace(j, position.y).getWalls() != null && board.getSpace(j, position.y).getWalls().contains(Heading.WEST) && board.getSpace(j, position.y) != position) {
                            break;
                        } else if (board.getSpace(j, position.y).getPlayer() != null && board.getSpace(j, position.y).getPlayer() != board.getPlayer(i)) {
                            board.getSpace(j, position.y).getPlayer().dealDamage();
                            break;
                        } else if (board.getSpace(j, position.y).getWalls() != null && board.getSpace(j, position.y).getWalls().contains(Heading.EAST)) {
                            break;
                        }
                    }
                    break;
                }
                case SOUTH -> {
                    for (int j = position.y; j <= board.height - 1; j++) {
                        if (board.getSpace(position.x, j).getWalls() != null && board.getSpace(position.x, j).getWalls().contains(Heading.SOUTH) && board.getSpace(position.x, j) == position) {
                            break;
                        }
                        if (board.getSpace(position.x, j).getWalls() != null && board.getSpace(position.x, j).getWalls().contains(Heading.NORTH) && board.getSpace(position.x, j) != position) {
                            break;
                        } else if (board.getSpace(position.x, j).getPlayer() != null && board.getSpace(position.x, j).getPlayer() != board.getPlayer(i)) {
                            board.getSpace(position.x, j).getPlayer().dealDamage();
                            break;
                        } else if (board.getSpace(position.x, j).getWalls() != null && board.getSpace(position.x, j).getWalls().contains(Heading.SOUTH)) {
                            break;
                        }
                    }
                    break;
                }
                case WEST -> {
                    for (int j = position.x; j >= 0; j--) {
                        if (board.getSpace(j, position.y).getWalls() != null && board.getSpace(j, position.y).getWalls().contains(Heading.WEST) && board.getSpace(j, position.y) == position) {
                            break;
                        }
                        if (board.getSpace(j, position.y).getWalls() != null && board.getSpace(j, position.y).getWalls().contains(Heading.EAST) && board.getSpace(j, position.y) != position) {
                            break;
                        } else if (board.getSpace(j, position.y).getPlayer() != null && board.getSpace(j, position.y).getPlayer() != board.getPlayer(i)) {
                            board.getSpace(j, position.y).getPlayer().dealDamage();
                            break;
                        } else if (board.getSpace(j, position.y).getWalls() != null && board.getSpace(j, position.y).getWalls().contains(Heading.WEST)) {
                            break;
                        }
                    }
                    break;
                }
            }
        }
    }


    /**
     * WIP, very early code. not functional yet.
     * Supposed to set the priority of all the players, depending on their distance to the antenna.
     * If 2 or more players are the same distance from the antenna, it should then draw a theoretical
     * line in the middle of the players and turn the line clockwise, and the first player it hit would
     * then have priority over the others.
     */
    public void setPlayerPrio() {
        Antenna antenna = board.getAntenna();
        LinkedList<Player> tmpList = new LinkedList<>();
        for (int i = 0; i < board.getPlayersNumber(); i++) {
            Player player = board.getPlayer(i);
            int dist = Math.abs(antenna.x - player.getSpace().x) + Math.abs(antenna.y - player.getSpace().y);
            player.setAntennaDist(dist);
            tmpList.add(player);
        }

        playerOrder = new LinkedList<>(tmpList);
        Collections.sort(playerOrder, (Comparator<Player>) (p1, p2) -> {
            if(p1.getAntennaDist() == p2.getAntennaDist()) {
                // Both players above antenna
                if (p2.getSpace().y <= antenna.y && p1.getSpace().y <= antenna.y) {
                    // Both players right of antenna
                    if (p2.getSpace().x >= antenna.x && p1.getSpace().x >= antenna.x) {
                        return p1.getSpace().x - p2.getSpace().x;
                    } else {
                        return p2.getSpace().x - p1.getSpace().x;
                    }
                }
                //Both player under antenna
                else if (p2.getSpace().y > antenna.y && p1.getSpace().y > antenna.y) {
                    return p1.getSpace().x - p2.getSpace().x;
                }
                //XOR one player above antenna and the other under.
                else /*((p2.getSpace().y > antenna.y && p1.getSpace().y > antenna.y) || (p1.getSpace().y > antenna.y && p2.getSpace().y > antenna.y))*/ {
                    //Both to the right of antenna
                    if (p2.getSpace().x >= antenna.x && p1.getSpace().x >= antenna.x) {
                        return p1.getSpace().y - p2.getSpace().y;
                    } else if (p2.getSpace().x < antenna.x && p1.getSpace().x < antenna.x){
                        return p2.getSpace().y - p1.getSpace().y;
                    } else {
                        if (p1.getSpace().x >= antenna.x){
                            return p1.getSpace().y - p2.getSpace().y;
                        } else {
                            return p2.getSpace().y - p1.getSpace().y;
                        }
                    }
                }
            }
            else {
                return p1.getAntennaDist() - p2.getAntennaDist();
            }
            //return 0;
        });
        for (int i = 0; i < playerOrder.size(); i++) {
            playerOrder.get(i).setPrioNo(i);
        }
        playerNum = 0;
        this.board.setCurrentPlayer(playerOrder.get(0));
    }

    /**
     * This method is used to declare a player the winner
     *
     * @param player - the player that is to be declared winner
     */
    public void makeWinner(Player player) {
        this.winner = true;
        if (player.getName() != "testPlayer") {
            Alert winnerMsg = new Alert(Alert.AlertType.INFORMATION, "Player \"" + player.getName() + "\" is the winner!");
            winnerMsg.showAndWait();
        }
    }

}
