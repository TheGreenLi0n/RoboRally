# RoboRally
This is the current iteration of the RoboRally project from group 16 in DTU's course [02324-F22](https://kurser.dtu.dk/course/02324).

## Running the program
1. Have JavaFX downloaded on your computer.
2. Open RoboRally in IntelliJ and set vm options to “--module-path
   "locationOfJavaFX.lib" --add-modules javafx.controls,javafx.fxml ” in the
   configuration.
3. Add the path of JavaFX/lib as a library in the project structure.
4. Start the program in the class `RoboRally` by running the `main` method will start the game.
5. If you can't use the run the program, remember to trust the maven project by right clicking the `pom` and choose `maven`at the bottom.
6. When you start the game, a window will show asking to start a new game or load an old one.

## Current features
1. Programming cards.
2. Pushing - robots can push each other.
3. Walls - represented by a red line on the board that the player can't walk through.
4. Conveyor - belts, both green and blue which respectively move the robots one and two spaces. These are shown on the board with the original images from the RoboRally ruleset.
5. Checkpoints - the players have to go through them in the correct order, and these are shown on the board with the original images from the RoboRally ruleset.
6. Win - winning the game by going through the correct order of checkpoints.
7. Checkpoint counter - a checkpoint counter for each player that shows the last valid checkpoint they visited.
8. Lasers - robots can shoot a laser in their headed position to damage other robots. The lasers are currently invisible.
9. Loading a board - it is possible to load an already made board.
10. Saving a game - it is possible to save a game.
11. Loading a saved game - it is possible to load a previous saved game, but it does not currently load the players' cards from the saved game.


## How to play RoboRally
RoboRally is played in rounds consisting of three phases: The upgrade phase, the
programming phase and the activation phase.

In the upgrade phase, the players use energy cubes to purchase upgrades for their robot
that enhances their gameplay.

In the programming phase, the players each draw nine cards from their programming deck
and pick five of them to use in their programming registers.

In the activation phase, the players carry out their programming for their robots. The priority
to see who goes first, is determined by the distance to the board element “priority antenna”
(which is yet to be implemented). The players execute the first registers in turn priority, then
the second register and so on until the fifth register has been executed. The board elements
activate and the robot lasers fire after all players have completed the current register that is
to be executed. This continues until the fifth register has been executed for all players. 

The game then loops back to the upgrade phases and continues the gameplay pattern until a player has won by going
through all the checkpoints.