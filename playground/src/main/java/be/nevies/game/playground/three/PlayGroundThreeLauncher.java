/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.three;

import be.nevies.game.engine.core.general.GameController;
import be.nevies.game.engine.core.sound.SoundManager;
import com.javafx.experiments.scenicview.ScenicView;
import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

/**
 * This play ground test the SoundManager.
 * 
 * @author drs
 */
public class PlayGroundThreeLauncher extends Application {

    private GameController controller;
    
    @Override
    public void start(Stage stage) throws Exception {
        controller = new PlayGroundThree(stage, 60, 1, "PlayGroundThree - test SoundManager", 600, 600);
        controller.initialise();
        controller.startGameUpdateTimeline();
        controller.startSoundUpdateTimeline();
        stage.show();
        //ScenicView.show(stage.getScene());
    }
    
    public static void main(String[] args) {
        // Set so we see debug info also
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("Stop application");
        controller.stopGameUpdateTimeline();
        controller.stopSoundUpdateTimeline();
        SoundManager.getInstance().stopCollisionCheck();
    }
    
    
}
