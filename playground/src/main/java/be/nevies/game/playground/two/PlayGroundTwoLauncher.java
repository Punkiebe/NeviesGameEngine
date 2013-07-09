/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.two;

import be.nevies.game.engine.core.general.GameController;
import com.javafx.experiments.scenicview.ScenicView;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author drs
 */
public class PlayGroundTwoLauncher extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        GameController controller = new PlayGroundTwo(stage, 30, 30, "Play ground two. Atom all around.", 600, 600);
        controller.initialise();
        controller.startGameUpdateTimeline();
        // ScenicView.show(stage.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
