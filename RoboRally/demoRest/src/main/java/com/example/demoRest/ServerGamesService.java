package com.example.demoRest;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import com.example.demoRest.model.*;
import org.springframework.stereotype.Service;


@Service
public class ServerGamesService implements IServerGamesService
{

    private static final String BOARDSFOLDER = "RoboRally/demoRest/src/main/resources/boards";
    private static final String GAMESFOLDER = "RoboRally/demoRest/src/main/resources/Games/";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = ".json";

    //creating an object of ArrayList
    ArrayList<Game> games = new ArrayList<Game>();

    public ServerGamesService() {

        games.add(new Game(101));

    }

    @Override
    public List<Game> findAll()
    {
    //returns a list of product
        return games;
    }

    @Override
    public boolean createGame(String s) throws IOException {
        int id = Integer.parseInt(s.substring(s.indexOf("\"id\": ") + 6, s.indexOf(",",s.indexOf("\"id\": "))));
        games.add(new Game(id));
        String filepath = GAMESFOLDER + "Game" + id + JSON_EXT;
            FileOutputStream outputStream = new FileOutputStream(filepath);
            byte[] strToBytes = s.getBytes();
            outputStream.write(strToBytes);

            outputStream.close();

        return true;
    }

    @Override
    public String getGameById(int id) throws IOException {
        String filepath = GAMESFOLDER + "Game" + id + JSON_EXT;
        return Files.readString(Paths.get(filepath));
    }

    @Override
    public boolean updateGame(int id, Game p) {
        for(Game currProd : games) {
            if(currProd.getId() == id) {
                currProd.setId(p.getId());
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteGameById(int id) {
        ArrayList<Game> newGames = new ArrayList<Game>();
        int oldSize = games.size();
        games.forEach((Game -> {
            if(Game.getId() != id)
                    newGames.add(Game);
        }));
        games = newGames;
        return oldSize < games.size() ? true : false;
    }
}