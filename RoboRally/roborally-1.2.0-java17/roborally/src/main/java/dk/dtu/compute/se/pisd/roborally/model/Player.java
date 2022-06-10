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
import org.jetbrains.annotations.NotNull;

import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Player extends Subject {

    final public static int NO_REGISTERS = 5;
    final public static int NO_CARDS = 8;

    final public Board board;

    private String name;
    private String color;

    private int damage;
    private int reachedCheckpoint = 0;

    private Space space;
    private Heading heading = SOUTH;

    private int prioNo;

    private int antennaDist;

    private CommandCardField[] program;


    private CommandCardField[] cards;

    public Player(@NotNull Board board, String color, @NotNull String name) {
        this.board = board;
        this.name = name;
        this.color = color;

        this.space = null;

        program = new CommandCardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++) {
            program[i] = new CommandCardField(this);
        }

        cards = new CommandCardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++) {
            cards[i] = new CommandCardField(this);
        }
    }

    /**
     *
     * @return name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name of the player
     */
    public void setName(String name) {
        if (name != null && !name.equals(this.name)) {
            this.name = name;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    /**
     *
     * @return color of the player
     */
    public String getColor() {
        return color;
    }

    /**
     * set the color of the player
     * @param color
     */
    public void setColor(String color) {
        this.color = color;
        notifyChange();
        if (space != null) {
            space.playerChanged();
        }
    }

    /**
     *
     * @return space of the player??
     */
    public Space getSpace() {
        return space;
    }

    /**
     * space of the player??
     * @param space
     */
    public void setSpace(Space space) {
        Space oldSpace = this.space;
        if (space != oldSpace &&
                (space == null || space.board == this.board)) {
            this.space = space;
            if (oldSpace != null) {
                oldSpace.setPlayer(null);
            }
            if (space != null) {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    /**
     *
     * @return heading of the player
     */
    public Heading getHeading() {
        return heading;
    }

    /**
     * sets the heading of the player.
     * @param heading
     */
    public void setHeading(@NotNull Heading heading) {
        if (heading != this.heading) {
            this.heading = heading;
            notifyChange();
            if (space != null) {
                space.playerChanged();
            }
        }
    }

    public int getAntennaDist() {
        return antennaDist;
    }

    public void setAntennaDist(int antennaDist) {
        this.antennaDist = antennaDist;
    }

    /**
     *
     * @return damage taken to the player.
     */
    public int getDamage() {
        return damage;
    }

    /**
     *
     */
    public void dealDamage(){damage++;}

    /**
     *
     * @return last reached checkpoint for the player.
     */
    public int getReachedCheckpoint() {
        return reachedCheckpoint;
    }

    /**
     * sets the reached checkpoint for the player.
     * @param reachedCheckpoint
     */
    public void setReachedCheckpoint(int reachedCheckpoint) {
        this.reachedCheckpoint = reachedCheckpoint;
        notifyChange();
    }

    /**
     *
     * @return priority number for the player.
     */
    public int getPrioNo() {
        return prioNo;
    }

    /**
     * sets the priority number for the player.
     * @param prioNo
     */
    public void setPrioNo(int prioNo) {
        this.prioNo = prioNo;
    }

    /**
     *
     * @param i is the current card.
     * @return a card in the player's register.
     */
    public CommandCardField getProgramField(int i) {
        return program[i];
    }

    /**
     *
     * @param i is the current card.
     * @return a card in the player's card deck.
     */
    public CommandCardField getCardField(int i) {
        return cards[i];
    }


    public CommandCard[] getCardFieldCards() {

        CommandCard[] cc = new CommandCard[cards.length];
        for (int i = 0; i <= cards.length - 1; i++ ) {
            cc[i] = cards[i].getCard();
        }
        return cc;
    }
    public void setProgram(CommandCardField[] program) {
        this.program = program;
    }

    public void setCards(CommandCardField[] cards) {
        this.cards = cards;
    }
    public CommandCard[] getProgramFieldCards() {
        CommandCard[] cc = new CommandCard[program.length];
        for (int i = 0; i <= program.length - 1; i++ ) {
            cc[i] = program[i].getCard();
        }
        return cc;
    }

}