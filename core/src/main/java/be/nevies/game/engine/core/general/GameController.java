package be.nevies.game.engine.core.general;

import be.nevies.game.engine.core.collision.CollisionManager;
import be.nevies.game.engine.core.event.GameEvent;
import be.nevies.game.engine.core.sound.SoundManager;
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
    private final int gameUpdatesPerSecond;
    /* The Timeline that handles the sound updates. It will call the method handleSoundUpdate. */
    private static Timeline soundUpdateTimeline;
    /* Number of sound updates per second. */
    private final int soundUpdatesPerSecond;

    /**
     * Constructor that will create the games main group node. It will also
     * create the game update time line, but doesn't start it yet! It creates
     * the Sound updates time line, but doesn't start it yet!
     *
     * @param gups Game updates per second.
     * @param sups Sound updates per second.
     * @param title Title of the games window.
     */
    public GameController(int gups, int sups, String title) {
        gameUpdatesPerSecond = gups;
        soundUpdatesPerSecond = sups;
        gameTitle = title;

        gameMainNode = new Group();
        gameMainNode.setId("GameMainNode");

        initialiseGameUpdateTimeline();
        initialiseSoundUpdateTimeline();
    }

    /**
     * Initialise the game update time line.
     */
    private void initialiseGameUpdateTimeline() {
        final Duration oneFrameAmt = Duration.millis(1000 / (float) getGameUpdatesPerSecond());
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        //getGameMainNode().fireEvent(new GameEvent(GameEvent.GAME_UPDATE_EVENT));
                        handleGameUpdate();
                    }
                });

        gameUpdateTimeline = TimelineBuilder.create().cycleCount(Animation.INDEFINITE).keyFrames(oneFrame).build();
    }

    /**
     * Initialise the sound update time line.
     */
    private void initialiseSoundUpdateTimeline() {
        final Duration oneFrameAmt = Duration.millis(1000 / (float) getSoundUpdatesPerSecond());
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(javafx.event.ActionEvent event) {
                        handleSoundUpdate();
                    }
                });

        soundUpdateTimeline = TimelineBuilder.create().cycleCount(Animation.INDEFINITE).keyFrames(oneFrame).build();
    }

    /**
     * Initialise the rest of your game. This method you need to call once the
     * GameController is created.<br/> This method shouldn't call any object
     * that only can be modified on the JavaFX Application Thread!
     */
    public abstract void initialise();

    /**
     * Define all the input events that need to be attached to the scene.<br/>
     * This is also called in the method 'createGameScene'. So normally there's
     * no need to call this your self.
     */
    public abstract void defineSceneEvents(Scene scene);

    /**
     * This method is called each time a game update is done.
     */
    protected void handleGameUpdate() {
        // Overide if you want to execute game updates in the game time line.
    }

    /**
     * This method is called each time a sound update is done.
     */
    protected void handleSoundUpdate() {
        // Overide if you wan to execute sound updates in the sound update time line calls.
    }

    /**
     * Start the time line that calls the 'handleGameUpdate' and fires a
     * GameEvent 'GAME_UPDATE_EVENT'.
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
     * Start the time line that calls the 'handleSoundUpdate'.
     */
    public void startSoundUpdateTimeline() {
        soundUpdateTimeline.play();
    }

    /**
     * Pause the sound update time line.
     */
    public void pauseSoundUpdateTimeline() {
        soundUpdateTimeline.pause();
    }

    /**
     * Stop the sound update time line.
     */
    public void stopSoundUpdateTimeline() {
        soundUpdateTimeline.stop();
    }

    /**
     * Returns the game updates per second.
     *
     * @return The game updates per second.
     */
    protected int getGameUpdatesPerSecond() {
        return gameUpdatesPerSecond;
    }

    /**
     * Returns the sound updates per second.
     *
     * @return The sound updates per second.
     */
    protected int getSoundUpdatesPerSecond() {
        return soundUpdatesPerSecond;
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
     * @param gameLoop Timeline object of an animation running indefinitely
     * representing the game loop.
     */
    protected static void setGameLoop(Timeline gameLoop) {
        GameController.gameUpdateTimeline = gameLoop;
    }

    /**
     * This creates the scene for the game, and if a stage is given it will add
     * the scene tot the given stage. After creating the scene it calls also the
     * 'defineScenEvents' method.
     *
     * @param widthWindow Width of the game window.
     * @param heightWindow Height of the game window.
     * @param stage The stage of the game. If you give the stage here, then
     * there's no need to call 'setGameStage' after.
     */
    public void createGameScene(double widthWindow, double heightWindow, Stage stage) {
        gameScene = new Scene(getGameMainNode(), widthWindow, heightWindow);
        defineSceneEvents(gameScene);
        if (stage != null) {
            setGameStage(stage);
        }
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
     * This will set a new scene for you game. This new scene is set to the game
     * stage also.
     *
     * @param scene The game scene.
     */
    protected void setGameScene(Scene scene) {
        if (gameStage == null) {
            throw new IllegalAccessError("Can't set the game scene if the game stage isn't set!!");
        }
        if (scene == null) {
            throw new IllegalArgumentException("You can't set the scene to null!!");
        }
        gameScene = scene;
        gameStage.setScene(scene);
    }

    /**
     * The main node of your game. To this node you want to add all your game
     * Elements.
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
        try {
            el.addElementToGroup(getGameMainNode());
        } catch (IllegalAccessException ex) {
            LOG.error("The following error was thrown when adding an element to the main game node. {}", ex.getMessage());
        }
    }

    /**
     * Setting the game stage we'll add the game scene to the game stage.
     *
     * @param stage The stage for this GameController.
     */
    public void setGameStage(Stage stage) {
        if (getGameScene() == null) {
            throw new IllegalAccessError("Can't set the game stage if the game scene isn't set!!");
        }
        gameStage = stage;
        gameStage.setScene(getGameScene());
        gameStage.setTitle(gameTitle);
    }

    /**
     * Stops all the game and sound updates. This stops also the service of the
     * collision and sound manager.
     */
    public void stop() {
        this.stopGameUpdateTimeline();
        this.stopSoundUpdateTimeline();
        if (SoundManager.isInitialised()) {
            SoundManager.getInstance().stopCollisionCheck();
        }
        CollisionManager.getInstance().stopCollisionCheck();
    }
}
