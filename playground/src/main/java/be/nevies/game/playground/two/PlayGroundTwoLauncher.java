package be.nevies.game.playground.two;

import be.nevies.game.engine.core.general.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The launcher for PlayGroundTwo.
 * In this playground we have the atom create and remove when they collide example.
 * 
 * @author drs
 */
public class PlayGroundTwoLauncher extends Application {

    private GameController controller;

    @Override
    public void init() throws Exception {
        super.init();
        controller = new PlayGroundTwo(30, 30, "Play ground two. Atoms all around.");
        controller.initialise();
    }
    
    
    
    @Override
    public void start(Stage stage) throws Exception {
        controller.createGameScene(600, 600, stage);
        controller.startGameUpdateTimeline();
        // ScenicView.show(stage.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
        launch(args);
    }
    
    
    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Stop application");
        controller.stop();
    }
}
