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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.FieldAction;
import dk.dtu.compute.se.pisd.roborally.model.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import org.jetbrains.annotations.NotNull;

import java.net.URISyntaxException;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class SpaceView extends StackPane implements ViewObserver {

    final public static int SPACE_HEIGHT = 60; // 75;
    final public static int SPACE_WIDTH = 60; // 75;

    public final Space space;

    private ImageView checkpoint;


    public SpaceView(@NotNull Space space) {
        this.space = space;

        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0) {
            this.setStyle("-fx-background-color: white;");
        } else {
            this.setStyle("-fx-background-color: black;");
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    private void updatePlayer() {

        Player player = space.getPlayer();
        if (player != null) {
            Polygon arrow = new Polygon(0.0, 0.0,
                    10.0, 20.0,
                    20.0, 0.0);
            try {
                arrow.setFill(Color.valueOf(player.getColor()));
            } catch (Exception e) {
                arrow.setFill(Color.MEDIUMPURPLE);
            }

            arrow.setRotate((90 * player.getHeading().ordinal()) % 360);
            this.getChildren().add(arrow);
        }
    }

    /**
     * Draws a wall on the gameboard.
     *
     * @return canvas
     */
    public Canvas drawWall(Heading heading) {
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        gc.setLineCap(StrokeLineCap.SQUARE);
        // North wall gc.strokeLine(2,SPACE_HEIGHT-58,SPACE_WIDTH,SPACE_HEIGHT-58);
        // West  wall  gc.strokeLine(2, SPACE_HEIGHT, SPACE_WIDTH - 58, SPACE_HEIGHT - 58);
        // South wall gc.strokeLine(2, SPACE_HEIGHT, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
        // East wall  gc.strokeLine(SPACE_HEIGHT,SPACE_HEIGHT-2,SPACE_WIDTH,SPACE_HEIGHT-58);
        switch (heading) {
            case NORTH -> {
                gc.strokeLine(2, SPACE_HEIGHT - 58, SPACE_WIDTH, SPACE_HEIGHT - 58);
                break;
            }
            case WEST -> {
                gc.strokeLine(2, SPACE_HEIGHT, SPACE_WIDTH - 58, SPACE_HEIGHT - 58);
                break;
            }
            case SOUTH -> {
                gc.strokeLine(2, SPACE_HEIGHT, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
                break;
            }
            case EAST -> {
                gc.strokeLine(SPACE_HEIGHT, SPACE_HEIGHT - 2, SPACE_WIDTH, SPACE_HEIGHT - 58);
                break;
            }

        }
        this.getChildren().add(canvas);
        return canvas;

    }

    public void updateFieldAction()
    {
        List<FieldAction> actions = space.getActions();
        if (actions != null) {
            for (FieldAction action : actions) {
                if (action.getClass() == Checkpoint.class) {
                    Checkpoint checkpoint = space.getCheckpoint();
                    addImage("images/checkpoint" + checkpoint.checkpointNo + ".png", 270);
                }
            }
        }
    }

    private void drawLine() {
        /*Pane pane = new Pane();
        Rectangle rectangle  = new Rectangle(0.0,0.0,SPACE_WIDTH,SPACE_HEIGHT);
        rectangle.setFill(Color.TRANSPARENT);
        pane.getChildren().add(rectangle);
        Line line = new Line(2, SPACE_HEIGHT-2,SPACE_WIDTH-2,SPACE_HEIGHT-2);
        line.setStroke(Color.BLUE);
        line.setStrokeWidth(5);
        pane.getChildren().add(line);
        this.getChildren().add(pane);*/
        // east wall  gc.strokeLine(SPACE_HEIGHT,SPACE_HEIGHT,SPACE_WIDTH,SPACE_HEIGHT-58);
        // south wall gc.strokeLine(2, SPACE_HEIGHT, SPACE_WIDTH - 2, SPACE_HEIGHT - 2);
        // north wall
        // west wall
        Canvas canvas = new Canvas(SPACE_WIDTH, SPACE_HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.RED);
        gc.setLineWidth(5);
        gc.setLineCap(StrokeLineCap.SQUARE);
        gc.strokeLine(SPACE_HEIGHT,SPACE_WIDTH,SPACE_WIDTH-2,SPACE_HEIGHT-2);
        this.getChildren().add(canvas);
    }

    @Override
    public void updateView(Subject subject) {
        this.getChildren().clear();
        if (subject == this.space) {
            updateFieldAction();
            updatePlayer();

        }
        if ((space.x == 2 && space.y == 2)) {
            space.setWallheading(Heading.WEST);
            space.setWall(drawWall(space.getWallheading()));
        }
        if (space.x == 2 && space.y == 2) {
            drawLine();
        }
    }

    private ImageView addImage (String name){
        Image img = null;
        try {
            img = new Image(SpaceView.class.getClassLoader().getResource(name).toURI().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        ImageView imgView = new ImageView(img);
        imgView.setImage(img);
        imgView.setFitHeight(SPACE_HEIGHT);
        imgView.setFitWidth(SPACE_WIDTH);
        imgView.setVisible(true);

        this.getChildren().add(imgView);

        return imgView;
    }

    private ImageView addImage (String name,double rotation){
        ImageView imageView = addImage(name);
        imageView.setRotate(rotation);

        return imageView;
    }

    }
