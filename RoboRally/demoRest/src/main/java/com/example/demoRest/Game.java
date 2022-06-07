package com.example.demoRest;

public class Game
{
    private int id;
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
