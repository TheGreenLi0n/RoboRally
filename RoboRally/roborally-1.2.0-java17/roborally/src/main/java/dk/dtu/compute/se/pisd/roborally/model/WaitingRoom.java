package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

/**
 *
 * @author Martin Wenzel
 */

public class WaitingRoom extends Subject {
private String message;
    public WaitingRoom(){
    this.message = "Lobby: \n" + "Players:" ;
    }

    public String getMessage() {
        return this.message;

    }

    public void setMessage(String message) {
        this.message = message;
        notifyChange();
    }
}
