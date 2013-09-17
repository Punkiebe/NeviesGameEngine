/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.four;

import be.nevies.game.engine.core.general.GameController;
import be.nevies.game.engine.tiled.plugin.core.TileLayerCreator;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.tmx.ReadTmxFile;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 */
public class PlayGroundFour extends GameController {

    /* Logger. */
    private static final Logger LOG = LoggerFactory.getLogger(PlayGroundFour.class);
    
    private PlayerFour player = new PlayerFour();

    public PlayGroundFour(Stage stage, int gups, int sups, String title, double widthWindow, double heightWindow) {
        super(stage, gups, sups, title, widthWindow, heightWindow);
    }

    @Override
    public void initialise() {
        LOG.debug("Start loading map");
        URL resource = ReadTmxFile.class.getResource("/be/nevies/game/engine/tiled/plugin/example/firstHouseXML.tmx");
        File file = null;
        try {
            file = new File(resource.toURI());
        } catch (URISyntaxException ex) {
            java.util.logging.Logger.getLogger(PlayGroundFour.class.getName()).log(Level.SEVERE, null, ex);
        }
        ReadTmxFile read = new ReadTmxFile(file);
        Map map = read.getMapFromTmxFile();
        Group layer = TileLayerCreator.createGroupFromMap(map, file);

        getGameMainNode().getChildren().add(layer);
        LOG.debug("End loading map");
        player.setLayoutX(120);
        player.setLayoutY(90);
        getGameMainNode().getChildren().add(player);
        setupInput(getGameScene());
    }
    
    
    private void setupInput(Scene scene) {
        EventHandler movePlayerEvent = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent t) {
                switch (t.getCode()) {
                    case UP:
                        player.moveUp();
                        break;
                    case DOWN:
                        player.moveDown();
                        break;
                    case LEFT:
                        player.moveLeft();
                        break;
                    case RIGHT:
                        player.moveRight();
                        break;
                }
            }
        };
        scene.setOnKeyPressed(movePlayerEvent);
    }
}
