/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.tmx.ReadTmxFile;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        URL resource = ReadTmxFile.class.getResource("/be/nevies/game/engine/tiled/plugin/example/firstHouseXML.tmx");
        File file= null;
        try {
            file = new File(resource.toURI());
        } catch (URISyntaxException ex) {
            Logger.getLogger(TileCollectionCreatorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        ReadTmxFile read = new ReadTmxFile(file);
        Map mapFromTmxFile = read.getMapFromTmxFile();
        assertNotNull(mapFromTmxFile);
        Group groupMap = TileLayerCreator.createLayersForMap(mapFromTmxFile, file);
        assertNotNull(groupMap);
    }
}
