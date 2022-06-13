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
package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.CommandCardFieldTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.CommandCardField;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.io.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 * @author Dennis Lolk Løvgreen, Noah Grænge Surel & Martin Wenzel
 */
public class LoadBoard {

    private static final String BOARDSFOLDER = "boards/";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = "json";

    /**
     * Used to get board with a given name to the player(s)
     * @param boardname the name of the board that the player wants to get
     * @return returns the board with the name the player specified
     */
    public static Board loadBoard(String boardname) {
        if (boardname == null) {
            boardname = DEFAULTBOARD;
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(BOARDSFOLDER + boardname + "." + JSON_EXT);
        if (inputStream == null) {
            // TODO these constants should be defined somewhere
            return new Board(8,8);
        }

		// In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>());
        Gson gson = simpleBuilder.create();

		Board result;
		// FileReader fileReader = null;
        JsonReader reader = null;
		try {
			// fileReader = new FileReader(filename);
			reader = gson.newJsonReader(new InputStreamReader(inputStream));
			BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);

			result = new Board(template.width, template.height);

            result.setId(template.id);
            result.setPhase(template.phase);
            result.setStep(template.step);;
			for (SpaceTemplate spaceTemplate: template.spaces) {
			    Space space = result.getSpace(spaceTemplate.x, spaceTemplate.y);
			    if (space != null) {
                    space.addAllFieldActions(spaceTemplate.actions);
                    space.getWalls().addAll(spaceTemplate.walls);
                    if (spaceTemplate.player != null) {
                        Player player = new Player(result, spaceTemplate.player.color, spaceTemplate.player.name);
                        player.setSpace(space);
                        player.setHeading(spaceTemplate.player.heading);
                        player.setReachedCheckpoint(spaceTemplate.player.reachedCheckpoint);
                        CommandCardField[] commandCardFields = new CommandCardField[spaceTemplate.player.cards.length];
                        for (int i = 0; i <= spaceTemplate.player.cards.length - 1; i++) {
                            CommandCardField commandCardField = new CommandCardField(player);
                            commandCardField.setCard(spaceTemplate.player.cards[i].card);
                            commandCardField.setVisible(spaceTemplate.player.cards[i].visibility);
                            commandCardFields[i] = commandCardField;
                        }
                        player.setCards(commandCardFields);
                        CommandCardField[] commandProgramFields = new CommandCardField[spaceTemplate.player.program.length];
                        for (int i = 0; i <= spaceTemplate.player.program.length - 1; i++) {
                            CommandCardField commandProgramField = new CommandCardField(player);
                            commandProgramField.setCard(spaceTemplate.player.program[i].card);
                            commandProgramField.setVisible(spaceTemplate.player.program[i].visibility);
                            commandProgramFields[i] = commandProgramField;
                        }
                        player.setProgram(commandProgramFields);
                        result.setCurrentPlayer(result.getPlayer(template.currentPlayer));
                        result.addPlayer(player);
                    }
                }
            }
			reader.close();
			return result;
		} catch (IOException e1) {
            if (reader != null) {
                try {
                    reader.close();
                    inputStream = null;
                } catch (IOException e2) {}
            }
            if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e2) {}
			}
		}
		return null;
    }

    /**
     * Method used to save the current board to a json file with the name given by the player
     * @param board the board the player is playing on
     * @param name the name that the player wants to save the board as
     */
    public static void saveBoard(Board board, String name) {
        BoardTemplate template = new BoardTemplate();
        template.id = board.getId();
        template.width = board.width;
        template.height = board.height;
        template.currentPlayer = board.getPlayerNumber(board.getCurrentPlayer());
        template.phase = board.getPhase();
        template.step = board.getStep();

        for (int i=0; i<board.width; i++) {
            for (int j=0; j<board.height; j++) {
                Space space = board.getSpace(i,j);
                if (!space.getWalls().isEmpty() || !space.getActions().isEmpty() || space.getPlayer() != null) {
                    SpaceTemplate spaceTemplate = new SpaceTemplate();
                    spaceTemplate.x = space.x;
                    spaceTemplate.y = space.y;
                    spaceTemplate.actions.addAll(space.getActions());
                    spaceTemplate.walls.addAll(space.getWalls());
                    if (space.getPlayer() != null){
                        PlayerTemplate playerTemplate = new PlayerTemplate();
                        playerTemplate.name = space.getPlayer().getName();
                        playerTemplate.color = space.getPlayer().getColor();
                        playerTemplate.heading = space.getPlayer().getHeading();
                        playerTemplate.reachedCheckpoint = space.getPlayer().getReachedCheckpoint();
                        playerTemplate.damage = space.getPlayer().getDamage();
                        playerTemplate.cards = new CommandCardFieldTemplate[space.getPlayer().getCardFieldCards().length];
                        playerTemplate.program = new CommandCardFieldTemplate[space.getPlayer().getProgramFieldCards().length];
                        for (int cn = 0; cn <= space.getPlayer().getCardFieldCards().length - 1; cn++) {
                            CommandCardFieldTemplate commandCardFieldTemplate = new CommandCardFieldTemplate();
                            commandCardFieldTemplate.card = space.getPlayer().getCardField(cn).getCard();
                            commandCardFieldTemplate.visibility = space.getPlayer().getCardField(cn).isVisible();
                            playerTemplate.cards[cn] = commandCardFieldTemplate;
                        }
                        for (int pn = 0; pn <= space.getPlayer().getProgramFieldCards().length - 1; pn++) {
                            CommandCardFieldTemplate commandProgramFieldTemplate = new CommandCardFieldTemplate();
                            commandProgramFieldTemplate.card = space.getPlayer().getProgramField(pn).getCard();
                            commandProgramFieldTemplate.visibility = space.getPlayer().getProgramField(pn).isVisible();
                            playerTemplate.program[pn] = commandProgramFieldTemplate;
                        }
                        spaceTemplate.player = playerTemplate;
                    }
                    template.spaces.add(spaceTemplate);
                }
            }
        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        // TODO: this is not very defensive, and will result in a NullPointerException
        //       when the folder "resources" does not exist! But, it does not need
        //       the file "simpleCards.json" to exist!
        String filename =
                classLoader.getResource(BOARDSFOLDER).getPath() + "/" + name + "." + JSON_EXT;

        // In simple cases, we can create a Gson object with new:
        //
        //   Gson gson = new Gson();
        //
        // But, if you need to configure it, it is better to create it from
        // a builder (here, we want to configure the JSON serialisation with
        // a pretty printer):
        GsonBuilder simpleBuilder = new GsonBuilder().
                registerTypeAdapter(FieldAction.class, new Adapter<FieldAction>()).
                setPrettyPrinting();
        Gson gson = simpleBuilder.create();

        FileWriter fileWriter = null;
        JsonWriter writer = null;
        try {
            fileWriter = new FileWriter(filename);
            writer = gson.newJsonWriter(fileWriter);
            gson.toJson(template, template.getClass(), writer);
            writer.close();
        } catch (IOException e1) {
            if (writer != null) {
                try {
                    writer.close();
                    fileWriter = null;
                } catch (IOException e2) {}
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e2) {}
            }
        }
    }

}
