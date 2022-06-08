package com.example.demoRest;

import java.util.List;
import com.example.demoRest.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ServerGamesController
{
    @Autowired
    private IServerGamesService GameService;

    @GetMapping(value = "/games")
    public ResponseEntity<List<Game>> getGame()
    {
        List<Game> Games = GameService.findAll();
        return ResponseEntity.ok().body(Games);
    }

    @PostMapping("/games")
    public ResponseEntity<String > addGame(@RequestBody Game p) {
        boolean added = GameService.createGame(p);
        if(added)
            return ResponseEntity.ok().body("added");
        else
            return ResponseEntity.internalServerError().body("not added");

    }

    @GetMapping("/games/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable int id) {
        Game p = GameService.getGameById(id);
        return ResponseEntity.ok().body(p);
    }

    @PutMapping("/games/{id}")
    public ResponseEntity<String> updateGame(@PathVariable int id, @RequestBody Game p) {
        boolean added = GameService.updateGame(id, p);
        return ResponseEntity.ok().body("updated");
    }

    @DeleteMapping("/games/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable int id) {
        boolean deleted = GameService.deleteGameById(id);
        return ResponseEntity.ok().body("deleted");
    }

}