/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.three;

import be.nevies.game.engine.core.general.GameController;
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

    @Override
    public void start(Stage stage) throws Exception {
        GameController controller = new PlayGroundThree(stage, 60, "PlayGroundThree - test SoundManager", 600, 600);
        controller.initialise();
        //controller.startGameUpdateTimeline();
        stage.show();
        //ScenicView.show(stage.getScene());
    }
    
    public static void main(String[] args) {
        // Set so we see debug info also
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
        launch(args);
    }
}
