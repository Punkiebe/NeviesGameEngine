package be.nevies.game.playground.three;

import be.nevies.game.engine.core.general.GameController;
import be.nevies.game.engine.core.sound.SoundManager;
import com.javafx.experiments.scenicview.ScenicView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * This play ground test the SoundManager.
 *
 * @author drs
 */
public class PlayGroundThreeLauncher extends Application {

    private GameController controller;

    @Override
    public void init() throws Exception {
        super.init();
        controller = new PlayGroundThree(60, 1, "PlayGroundThree - test SoundManager");
        controller.initialise();
    }

    @Override
    public void start(Stage stage) throws Exception {
        controller.createGameScene(600, 600, stage);
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
        controller.stopGameUpdateTimeline();
        controller.stopSoundUpdateTimeline();
        SoundManager.getInstance().stopCollisionCheck();
    }
}
