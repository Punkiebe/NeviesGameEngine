package be.nevies.game.playground.four;

import be.nevies.game.engine.core.general.GameController;
import com.javafx.experiments.scenicview.ScenicView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Play ground four is to test the tiled plugin.
 *
 * @author drs
 */
public class PlayGroundFourLauncher extends Application {
    
    private GameController controller;
    
    @Override
    public void init() throws Exception {
        super.init();
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        controller = new PlayGroundFour(60, 60, "PlayGroundFour");
        controller.createGameScene(600, 600, stage);
        controller.initialise();
        controller.startGameUpdateTimeline();
        ScenicView.show(stage.getScene());
        stage.show();
    }
    
    public static void main(String[] args) {
        // Set so we see debug info also
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "trace");
        launch(args);
        
    }
    
    @Override
    public void stop() throws Exception {
        super.stop();
        controller.stopGameUpdateTimeline();
    }
}
