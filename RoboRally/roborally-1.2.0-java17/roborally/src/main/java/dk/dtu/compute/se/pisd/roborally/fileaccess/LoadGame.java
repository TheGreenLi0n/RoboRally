package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.CommandCardFieldTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Game;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Dennis Lolk Løvgreen
 */

public class LoadGame {

    private static final String GAMESFOLDER = "games";
    private static final String JSON_EXT = "json";

    public static Game loadGame(Integer gameID) {
        if (gameID == null) {


        }

        ClassLoader classLoader = LoadBoard.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(GAMESFOLDER + "/Game" + gameID.toString() + "." + JSON_EXT);
        if (inputStream == null) {
            // TODO these constants should be defined somewhere
            return new Game();
        }
        return new Game();
    }


    /*public static void saveGame(Game game, String name) {
        BoardTemplate template = new BoardTemplate();
        template.id = game.getId();
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
    }*/

}
