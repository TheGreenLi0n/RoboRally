package com.example.demoRest;

import java.io.IOException;
import java.util.List;
import com.example.demoRest.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @author Dennis Lolk Løvgreen
 */

@RestController
public class ServerGamesController
{
    @Autowired
    private IServerGamesService GameService;

    /**
     *
     * @return
     */
    @GetMapping(value = "/games")
    public ResponseEntity<List<Game>> getGame()
    {
        List<Game> Games = GameService.findAll();
        return ResponseEntity.ok().body(Games);
    }


   @PostMapping("/games")
    public ResponseEntity<String > addGame(@RequestBody String s) throws IOException {
        boolean added = GameService.createGame(s);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

    /**
     *
     * @param s
     * @param id id of the game.
     * @return a status of how the operation went.
     * @throws IOException
     */
    @PutMapping(value = "/games/{id}")
    public ResponseEntity<String > addGame(@RequestBody String s, @PathVariable int id) throws IOException {
        boolean added = GameService.createGame(s);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

    /**
     * Get a game from an ID.
     * @param id the id of the game.
     * @return a sting in .json format of a game.
     */
    @GetMapping("/games/{id}")
    public String getGameById(@PathVariable int id)  {
        String game = null;
        try {
            game = GameService.getGameById(id);
            return game;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "no Game with provided ID";
    }

    /*@PutMapping("/games/{id}")
    public ResponseEntity<String> updateGame(@PathVariable int id, @RequestBody Game p) {
        boolean added = GameService.updateGame(id, p);
        return ResponseEntity.ok().body("updated");
    }*/

    @DeleteMapping("/games/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable int id) {
        boolean deleted = GameService.deleteGameById(id);
        return ResponseEntity.ok().body("deleted");
    }

}