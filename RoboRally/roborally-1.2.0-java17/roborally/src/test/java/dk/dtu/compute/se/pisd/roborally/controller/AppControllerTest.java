package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppControllerTest {


    private AppController appController;


    @BeforeEach
    void setUp() {
        RoboRally roboRally = new RoboRally();
        appController = new AppController(roboRally);
    }


    @AfterEach
    void tearDown() {
        appController = null;
    }

    @Test
    void newGame() {
        appController.newGame();
        appController.stopGame();

    }

    @Test
    void saveGame(){

    }

    @Test
    void loadGame(){

    }


}