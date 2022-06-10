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
package com.example.demoRest.model;

import java.util.ArrayList;
import java.util.List;


/**
 * ...
 *
 * @author Dennis Lolk Løvgreen
 *
 */
public class Game {

    public int id;
    public int width;
    public int height;
    public Phase phase;
    public int currentPlayer;
    public int step;

    public List<SpaceTemplate> spaces = new ArrayList<>();
    //default constructor
    public Game()
    {

    }
    //constructor using fields
    public Game(int id)
    {
        super();
        this.id = id;
    }
    //getters and setters
    public int getId()
    {
        return id;
    }
    public void setId(int id)
    {
        this.id = id;
    }


}
