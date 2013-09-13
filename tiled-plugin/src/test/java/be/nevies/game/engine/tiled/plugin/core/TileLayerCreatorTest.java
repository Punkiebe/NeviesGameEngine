/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.tmx.ReadTmxFile;
import javafx.scene.Group;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author drs
 */
public class TileLayerCreatorTest {
    
    @Test
    public void testCreateLayer() {
        ReadTmxFile read = new ReadTmxFile();
        Map mapFromTmxFile = read.getMapFromTmxFile();
        assertNotNull(mapFromTmxFile);
        Group groupMap = TileLayerCreator.createLayersForMap(mapFromTmxFile);
        assertNotNull(groupMap);
    }
}
