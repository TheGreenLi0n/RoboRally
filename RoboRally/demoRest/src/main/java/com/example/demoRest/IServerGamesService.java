package com.example.demoRest;

import java.util.List;
import com.example.demoRest.model.*;
public interface IServerGamesService
{
    List<Game> findAll();
    public Game getGameById(int id);
    boolean createGame(Game p);
    public boolean updateGame(int id, Game p);
    public boolean deleteGameById(int id);
}