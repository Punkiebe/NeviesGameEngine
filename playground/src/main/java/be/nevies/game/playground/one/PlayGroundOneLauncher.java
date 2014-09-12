/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.one;

import be.nevies.game.engine.core.general.GameController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author drs
 */
public class PlayGroundOneLauncher extends Application {
    
    private GameController playGroundOne;
    
    @Override
    public void init() throws Exception {
        super.init();
        playGroundOne = new PlayGroundOne(60, 30, "Play ground one.");
        playGroundOne.initialise();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        playGroundOne.createGameScene(600, 600, stage);
        playGroundOne.startGameUpdateTimeline();
       // ScenicView.show(stage.getScene());
        stage.show();
    }
    
    public static void main(String[] args) {
        // Set so we see debug info also
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
        launch(args);
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Stop application");
        playGroundOne.stop();
    }
}
