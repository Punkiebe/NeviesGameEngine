/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.playground.four;

import be.nevies.game.engine.core.general.GameController;
import be.nevies.game.engine.tiled.plugin.core.TileLayerCreator;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.tmx.ReadTmxFile;
import javafx.scene.Group;
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

    public PlayGroundFour(Stage stage, int gups, int sups, String title, double widthWindow, double heightWindow) {
        super(stage, gups, sups, title, widthWindow, heightWindow);
    }

    
    @Override
    public void initialise() {
        LOG.debug("Start loading map");
        ReadTmxFile read = new ReadTmxFile();
        Map map = read.getMapFromTmxFile();
        Group layer = TileLayerCreator.createLayersForMap(map);
        
        getGameMainNode().getChildren().add(layer);
        
        LOG.debug("End loading map");
    }
    
}
