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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Space extends Subject {

    public final Board board;

    public final int x;
    public final int y;
    public List<Heading> walls;
    private Player player;
    private Checkpoint checkpoint;
    private ConveyorBelt conveyorBelt;
    private Antenna antenna;



    public List<FieldAction> actions = new ArrayList<>();

    public Space(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
        this.walls = new ArrayList<>();
    }

    /**
     *
     * @return player of the space.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Moves the target player to the space.
     * @param player
     */
    public void setPlayer(Player player) {
        Player oldPlayer = this.player;
        if (player != oldPlayer &&
                (player == null || board == player.board)) {
            this.player = player;
            if (oldPlayer != null) {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null) {
                player.setSpace(this);
            }
            notifyChange();
        }
    }

    void playerChanged() {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }

    /**
     *
     * @return the checkpoint of the space.
     */
    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    /**
     * sets the checkpoint at the targeted space on the board.
     * @param checkpoint
     */
    public void setCheckpoint(Checkpoint checkpoint) {
        this.checkpoint = checkpoint;
        this.actions.add(checkpoint);
    }

    /**
     *
     * @return the conveyor belt of the targeted space.
     */
    public ConveyorBelt getConveyorBelt() {
        return conveyorBelt;
    }

    /**
     * sets the conveyor belt to the targeted space.
     * @param conveyorBelt
     */
    public void setConveyorBelt(ConveyorBelt conveyorBelt) {
        this.conveyorBelt = conveyorBelt;
        this.actions.add(conveyorBelt);
    }

    /**
     *
     * @return walls
     */
    public List<Heading> getWalls() {
        return walls;
    }

    /**
     * moves walls with a specific heading to the targeted space.
     * @param walls
     */
    public void setWalls(List<Heading> walls) {
        this.walls = walls;
    }

    /**
     *
     * @return the current actions of the current space.
     */
    public List<FieldAction> getActions() {
        return actions;
    }


    /**
     *
     * @return Antenna
     */
    public Antenna getAntenna() {
        return antenna;
    }

    /**
     *
     * @param antenna
     */
    public void setAntenna(Antenna antenna) {
        this.antenna = antenna;
        this.actions.add(antenna);
    }
    public void addAllFieldActions(List<FieldAction> list){
        actions.addAll(list);
        for (FieldAction fa : list) {
            if (fa.getClass() == Checkpoint.class){
                board.addCheckpoint((Checkpoint) fa);
            }

        }
    }
}
