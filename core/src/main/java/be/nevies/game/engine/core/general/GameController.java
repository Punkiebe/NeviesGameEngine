package be.nevies.game.engine.core.general;

import be.nevies.game.engine.core.event.GameEvent;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TimelineBuilder;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @TODO : Still need to implement a sound manager!!
 *
 * The game controller. Extend this class to create your own game controller.  <pre>
 *  <b>initialise()</b> : Overide this method and set up the start of your game. This method you need to call your self after creating your game controller.
 *  <b>handleGameUpdate()</b> : This method is called each frame of the game update time line. Overide it whith what you want to do each game update.
 *  <b>startGameUpdateTimeline()</b> : This starts the game update time line.
 *  <b>pauseGameUpdateTimeline()</b> : Pauses the game update time line, so no game updates are executed.
 * </pre>
 *
 * @author drs
 */
public abstract class GameController {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);
    /* Title of your game window. */
    private final String gameTitle;
    /* The stage of your game. */
    private Stage gameStage;
    /* The scene of your game. */
    private Scene gameScene;
    /* Main group node for all your game nodes. */
    private Group gameMainNode;
    /* The Timeline that handles the game updates. It fires the GameEvent 'GAME_UPDATE_EVENT' and calls the method handleGameUpdate. */
    private static Timeline gameUpdateTimeline;
    /* Number of game updates per second. */
    private final int updatePerSecond;

    /**
     * Constructor that will create a game scene with a Group node attached to it and add this scene to the given stage. It will also create the game update
     * time line, but doesn't start it yet!
     *
     * @param stage The stage of this game.
     * @param ups Update per second.
     * @param title Title of the games window.
     * @param widthWindow Width of the game window.
     * @param heightWindow Height of the game window.
     */
    public GameController(Stage stage, int ups, String title, double widthWindow, double heightWindow) {
        if (stage == null) {
            throw new IllegalArgumentException("The given Stage was null!! Stage can't be null to create a GameController.");
        }
        gameStage = stage;
        updatePerSecond = ups;
        gameTitle = title;

        gameMainNode = new Group();
        gameMainNode.setId("GameMainNode");
        gameScene = new Scene(gameMainNode, widthWindow, heightWindow);

        gameStage.setTitle(title);
        gameStage.setScene(gameScene);

        initialiseGameUpdateTimeline();
    }

    /**
     * Initialise the game update time line.
     */
    private void initialiseGameUpdateTimeline() {
        final Duration oneFrameAmt = Duration.millis(1000 / (float) getUpdatePerSecond());
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        getGameMainNode().fireEvent(new GameEvent(GameEvent.GAME_UPDATE_EVENT));
                        handleGameUpdate();
                    }
                });

        gameUpdateTimeline = TimelineBuilder.create().cycleCount(Animation.INDEFINITE).keyFrames(oneFrame).build();
    }

    /**
     * Initialise the rest of your game. This method you need to call once the GameController is created.
     */
    public abstract void initialise();

    /**
     * This method is called each time a game update is done.
     */
    protected void handleGameUpdate() {
        // Overide if you want to execute game updates in the game time line.
    }

    /**
     * Start the time line that calls the 'handleGameUpdate' and fires a GameEvent 'GAME_UPDATE_EVENT'.
     */
    public void startGameUpdateTimeline() {
        gameUpdateTimeline.play();
    }

    /**
     * Pause the game update time line.
     */
    public void pauseGameUpdateTimeline() {
        gameUpdateTimeline.pause();
    }

    /**
     * Stop the game update time line.
     */
    public void stopGameUpdateTimeline() {
        gameUpdateTimeline.stop();
    }

    /**
     * Returns the updates per second.
     *
     * @return The updates per second.
     */
    protected int getUpdatePerSecond() {
        return updatePerSecond;
    }

    /**
     * Returns the games window title.
     *
     * @return The games window title.
     */
    public String getWindowTitle() {
        return gameTitle;
    }

    /**
     * Gives you the Timeline that calls the game updates.
     *
     * @return The time line used for game updates.
     */
    protected static Timeline getGameUpdatesTimeline() {
        return gameUpdateTimeline;
    }

    /**
     * The sets the current game loop for this game world.
     *
     * @param gameLoop Timeline object of an animation running indefinitely representing the game loop.
     */
    protected static void setGameLoop(Timeline gameLoop) {
        GameController.gameUpdateTimeline = gameLoop;
    }

    /**
     * Get current game scene.
     *
     * @return Scene The JavaFX scene graph.
     */
    public Scene getGameScene() {
        return gameScene;
    }

    /**
     * This will set a new scene for you game. Default there already a scene created for you! This new scene is set to the game stage also.
     *
     * @param scene The game scene.
     */
    protected void setGameScene(Scene scene) {
        if (scene == null) {
            throw new IllegalArgumentException("You can't set the scene to null!!");
        }
        gameScene = scene;
        gameStage.setScene(scene);
    }

    /**
     * The main node of your game. To this node you want to add all your game Elements.
     *
     * @return The main node of type Group of your game.
     */
    public Group getGameMainNode() {
        return gameMainNode;
    }

    /**
     * Adds the given element to the games main node.
     *
     * @param el The element you want to add.
     */
    public void addElementToGameMainNode(Element el) {
        getGameMainNode().getChildren().add(el);
    }
}
