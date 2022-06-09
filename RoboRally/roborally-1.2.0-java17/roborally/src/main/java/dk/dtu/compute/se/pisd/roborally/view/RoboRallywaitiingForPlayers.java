package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.WaitingRoom;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;

public class RoboRallywaitiingForPlayers extends VBox implements ViewObserver {
    private Pane canvas;
    private Label label1;
    private Label label2;
    private Button button;
    private Stage stage;
    private WaitingRoom idleroom;
    public RoboRallywaitiingForPlayers(WaitingRoom idleroom ){


        this.idleroom = idleroom;
        /*this.label1 = new Label("Hello World");
        this.label2 = new Label("GoodbyeWorld");
*/
       /* this.canvas.getChildren().add(label1);
        this.canvas.getChildren().add(label2);*/

        idleroom.attach(this);
        updateView(idleroom);

    }

    ;

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
