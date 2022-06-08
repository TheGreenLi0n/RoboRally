package com.example.demoRest;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class GameService implements IGameService
{
    //creating an object of ArrayList
    ArrayList<Game> games = new ArrayList<Game>();

    public GameService() {

        games.add(new Game(101));

    }

    @Override
    public List<Game> findAll()
    {
    //returns a list of product
        return games;
    }

    @Override
    public boolean createGame(Game p) {
        games.add(p);
        return true;
    }

    @Override
    public Game getGameById(int id) {
        for(Game p : games) {
            if(p.getId() == id) {
                return p;
            }
        }
        return null;
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