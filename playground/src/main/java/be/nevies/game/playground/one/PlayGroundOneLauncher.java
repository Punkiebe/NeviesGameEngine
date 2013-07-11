/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.one;

import be.nevies.game.engine.core.general.GameController;
import be.nevies.game.engine.core.sound.SoundManager;
import com.javafx.experiments.scenicview.ScenicView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author drs
 */
public class PlayGroundOneLauncher extends Application {
    
    GameController playGroundOne;

    @Override
    public void start(Stage stage) throws Exception {
        
        playGroundOne = new PlayGroundOne(stage, 60, 60, "Play ground one.", 800, 640);
        playGroundOne.initialise();
        playGroundOne.startGameUpdateTimeline();
        ScenicView.show(stage.getScene());
        stage.show();
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
        playGroundOne.stopGameUpdateTimeline();
        playGroundOne.stopSoundUpdateTimeline();
    }
}
