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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The different commands that a player can use in their registers with the names in " ".
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public enum Command {

    // This is a very simplistic way of realizing different commands.

    FORWARD_1("Move fwd 1"),
    RIGHT("Turn Right"),
    LEFT("Turn Left"),
    FORWARD_2("Move fwd 2"),
    FORWARD_3("Move fwd 3"),
    MOVE_BACK("Move back"),
    U_TURN("U-turn"),

    OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT),
    OPTION_FORWARD_1_2_3("Move fwd 1, 2 or 3",FORWARD_1, FORWARD_2, FORWARD_3),
    OPTION_LEFT_RIGHT_U_TURN("Left, Right or U-turn",LEFT, RIGHT, U_TURN);

    final public String displayName;

    // XXX Assignment P3
    // Command(String displayName) {
    //     this.displayName = displayName;
    // }
    //
    // replaced by the code below:

    final private List<Command> options;

    Command(String displayName, Command... options) {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }

    /**
     * boolean method to check if ... is interactive
     * @return true if options is not empty
     */
    public boolean isInteractive() {
        return !options.isEmpty();
    }

    /**
     * a list method to
     * @return options
     */
    public List<Command> getOptions() {
        return options;
    }

}
