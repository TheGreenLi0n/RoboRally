package com.example.demoRest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import com.example.demoRest.model.*;
public interface IServerGamesService
{
    List<Game> findAll();
    public String getGameById(int id) throws IOException;
    boolean createGame(String p) throws IOException;
    public boolean updateGame(int id, Game p);
    public boolean deleteGameById(int id);
}