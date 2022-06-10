package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.WaitingRoom;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

/**
 *
 * @author Martin Wenzel
 */

public class RoboRallywaitiingForPlayers extends VBox implements ViewObserver {
    private Button button;
    private WaitingRoom idleroom;
    public RoboRallywaitiingForPlayers(WaitingRoom idleroom ){
        this.idleroom = idleroom;
        idleroom.attach(this);
        updateView(idleroom);

    }

    public Button getButton() {
        return button;
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == this.idleroom){
            this.getChildren().add(new Pane(new Label(idleroom.getMessage())));
        }




    }

    public WaitingRoom getIdleroom() {
        return idleroom;
    }

    @Override
    public void update(Subject subject) {
        ViewObserver.super.update(subject);
    }
}
