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
package dk.dtu.compute.se.pisd.roborally.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

import dk.dtu.compute.se.pisd.roborally.RoboRally;


import dk.dtu.compute.se.pisd.roborally.fileaccess.Adapter;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.model.*;

import dk.dtu.compute.se.pisd.roborally.view.RoboRallywaitiingForPlayers;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.HashMap;
import java.util.Map;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class AppController implements Observer {

    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "defaultboard";
    private static final String JSON_EXT = ".json";

    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");

    final private RoboRally roboRally;

    private GameController gameController;
    private Board board;
    private RoboRallywaitiingForPlayers idlemenu;
    private static final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public AppController(@NotNull RoboRally roboRally) {
        this.roboRally = roboRally;
    }

    /**
     * Method for creating a new game which creates a default board and the chosen number of players for the given game.
     * If result.isPresent is true the program will create the board, gameController and players.
     *
     */
    public void newGame() throws ExecutionException, InterruptedException, TimeoutException, IOException {
        getAllGames();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/games"))
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String games = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);

        //System.out.println(Integer.parseInt(games.substring(games.lastIndexOf(":") + 1,games.lastIndexOf("}")))+1);

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent()) {
            if (gameController != null) {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame()) {
                    return;
                }
            }

            // XXX the board should eventually be created programmatically or loaded from a file
            //     here we just create an empty board with the required number of players.
            board = LoadBoard.loadBoard("defaultboard1");
            board.setId(Integer.parseInt(games.substring(games.lastIndexOf("\"id\":") + 5, games.indexOf(",",games.lastIndexOf("\"id\":")+5)))+1);
            //Board board = new Board(8,8);
            gameController = new GameController(board);
            int no = result.get();
            for (int i = 0; i < no; i++) {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1));
                board.addPlayer(player);
                player.setSpace(board.getSpace(i % board.width, i));
            }

            idleMenu();
            // XXX: V2
            // board.setCurrentPlayer(board.getPlayer(0));
            gameController.startProgrammingPhase();
            LoadBoard.saveBoard(board,"Game"+board.getId().toString());

            String s = Files.readString(Paths.get("RoboRally/roborally-1.2.0-java17/roborally/target/classes/boards/Game" + board.getId().toString() + ".json"));
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(s))
                    .uri(URI.create("http://localhost:8080/games/" + board.getId()))
                    .build();

            CompletableFuture<HttpResponse<String>> postResponse = httpClient.sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());

            String postResult = postResponse.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);

            System.out.println(postResult);

            roboRally.createBoardView(gameController);

        }
    }

    /**
     * Saving the current game by the name entered by the player.
     */
    public void saveGame() {
        // XXX needs to be implemented eventually
        TextInputDialog input = new TextInputDialog();
        input.setHeaderText("Enter name for the save file");
        input.setTitle("Save Game");
        Optional<String> name = input.showAndWait();

        name.ifPresent(result ->{

            LoadBoard.saveBoard(board,"Game"+board.getId().toString());
            LoadBoard.saveBoard(board,result);
            String s = null;
            try {
                s = Files.readString(Paths.get("RoboRally/roborally-1.2.0-java17/roborally/target/classes/boards/Game" + board.getId().toString() + ".json"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            HttpRequest postRequest = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .PUT(HttpRequest.BodyPublishers.ofString(s))
                    .uri(URI.create("http://localhost:8080/games/" + board.getId()))
                    .build();

            CompletableFuture<HttpResponse<String>> postResponse = httpClient.sendAsync(postRequest, HttpResponse.BodyHandlers.ofString());

            String postResult = null;
            try {
                postResult = postResponse.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                e.printStackTrace();
            }

            System.out.println(postResult);
        });
    }

    /**
     * Loading a game....
     */
    public void loadGame() throws ExecutionException, InterruptedException, TimeoutException {
        // XXX needs to be implememted eventually
        // for now, we just create a new game
        if (gameController == null) {
            String path = "RoboRally/roborally-1.2.0-java17/roborally/target/classes/boards";
            File file = new File(path);
            String absPath = file.getAbsolutePath();
            absPath =absPath.replaceAll("\\\\", "$0$0");
            File filePath = new File(absPath);
            File[] folder = filePath.listFiles();
            String[] filenames = new String[folder.length];
            for (int i = 0; i < filenames.length; i++) {
                filenames[i] = folder[i].getName().substring(0,folder[i].getName().length()-5);
            }

//            newGame();
            /*ChoiceDialog<String> dialog = new ChoiceDialog<>(filenames[0],filenames);
            dialog.setTitle("Load Game");
            dialog.setHeaderText("Select a game to load");
            Optional<String> result = dialog.showAndWait();*/

            TextInputDialog input = new TextInputDialog();
            input.setHeaderText("Enter ID for the saved game");
            input.setTitle("Load Game");
            Optional<String> id = input.showAndWait();
            id.ifPresent(result ->{

            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(URI.create("http://localhost:8080/games/" + String.valueOf(id.get())))
                    .build();

            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

                try {
                    String game = response.thenApply((r) -> r.body()).get(5, TimeUnit.SECONDS);

                    String filepath = BOARDSFOLDER + "Game" + String.valueOf(id.get()) + JSON_EXT;
                    FileOutputStream outputStream = null;

                    outputStream = new FileOutputStream(filepath);

                    byte[] strToBytes = game.getBytes();

                    outputStream.write(strToBytes);

                    outputStream.close();

                    board = LoadBoard.loadBoard("Game" + String.valueOf(id.get()));
                    gameController = new GameController(board);
                    roboRally.createBoardView(gameController);

                }catch (FileNotFoundException e){

                } catch (IOException | ExecutionException | InterruptedException | TimeoutException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * Method to join an online game
     * Needs to be implemented
     */
    public void joinGame() throws ExecutionException, InterruptedException, TimeoutException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/games"))
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);

        System.out.println(result);
    }

    public void hostGame() throws ExecutionException, InterruptedException, TimeoutException, FileNotFoundException {



        if (gameController == null) {
            String path = "RoboRally/roborally-1.2.0-java17/roborally/target/classes/boards";
            File file = new File(path);
            String absPath = file.getAbsolutePath();
            absPath =absPath.replaceAll("\\\\", "$0$0");
            File filePath = new File(absPath);
            File[] folder = filePath.listFiles();
            String[] filenames = new String[folder.length];
            for (int i = 0; i < filenames.length; i++) {
                filenames[i] = folder[i].getName().substring(0,folder[i].getName().length()-5);
            }

            ChoiceDialog<String> dialogmap = new ChoiceDialog<>(filenames[0],filenames);
            dialogmap.setTitle("Load Game");
            dialogmap.setHeaderText("Select a game to load");
            Optional<String> resultmap = dialogmap.showAndWait();

            resultmap.ifPresent(choice->{
                board = LoadBoard.loadBoard(choice);
            });



            HttpRequest request = HttpRequest.newBuilder()
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofFile(Paths.get("RoboRally/roborally-1.2.0-java17/roborally/src/main/resources/boards/test.json")))
                    .uri(URI.create("http://localhost:8080/games"))
                    .build();

            CompletableFuture<HttpResponse<String>> response =
                    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

            String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);

            System.out.println(result);


            // display lobby, and code for host to send to other players.
        }



    }
    public void idleMenu(){

        WaitingRoom idleroom = new WaitingRoom();
        RoboRallywaitiingForPlayers idleview = new RoboRallywaitiingForPlayers(idleroom);
        roboRally.lobbyView(idleview);
        idleroom.setMessage(board.getPlayer(0).getName() +" Host"+"\n");
        for (int i = 1; i < board.getPlayersNumber(); i++) {
            idleroom.setMessage(board.getPlayer(i).getName() + "\n");
        }
        StopWatch timer = new StopWatch(5);
        timer.startTimer();


    }


    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     */
    public boolean stopGame() {
        if (gameController != null) {

            // here we save the game (without asking the user).
            saveGame();

            gameController = null;
            roboRally.createBoardView(null);
            return true;
        }
        return false;
    }

    /**
     * Method for when exiting roborally get a Confirmation Window to check if the player is sure about closing the game.
     * Exits the game if gameController is null or stopGame() method is used.
     */

    public void exit() {
        if (gameController != null) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK) {
                return; // return without exiting the application
            }
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame()) {
            Platform.exit();
        }
    }

    public void getAllGames() throws FileNotFoundException, ExecutionException, InterruptedException, TimeoutException {
        ArrayList<String> games = new ArrayList<>();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create("http://localhost:8080/games"))
                .build();

        CompletableFuture<HttpResponse<String>> response =
                httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply((r)->r.body()).get(5, TimeUnit.SECONDS);



        System.out.println(result);
    }

    /**
     * Boolean method to check if the game is running. If the gameController is not null the game is running(true).
     * @return a true or false depending on whether gameController is null or not null.
     */
    public boolean isGameRunning() {
        return gameController != null;
    }


    @Override
    public void update(Subject subject) {
        // XXX do nothing for now
    }



}
