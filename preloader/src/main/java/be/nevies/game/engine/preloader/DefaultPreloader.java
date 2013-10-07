/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.preloader;

import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * A default pre-loader for you game.
 *
 * @author drs
 */
public class DefaultPreloader extends Preloader {
    
    private Stage stage;
    private ProgressBar bar;
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        bar = new ProgressBar();
        BorderPane p = new BorderPane();
        p.setCenter(bar);
        stage.setScene(new Scene(p, 600, 400));
        stage.show();
    }
    
    @Override
    public void handleProgressNotification(ProgressNotification pn) {
        bar.setProgress(pn.getProgress());
    }
}
