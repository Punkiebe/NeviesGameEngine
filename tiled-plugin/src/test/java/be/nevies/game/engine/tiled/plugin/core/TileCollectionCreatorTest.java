/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.core.graphic.TileCollection;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.tmx.ReadTmxFile;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for TileCollectionCreator.
 * 
 * @author drs
 */
public class TileCollectionCreatorTest {
    
    @Test 
    public void testCreateTileCollectionsFromMap() {
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
        Collection<TileCollection> tileCols = TileCollectionCreator.createTileCollectionsFromMap(mapFromTmxFile, file);
        assertNotNull(tileCols);
        assertEquals(1, tileCols.size());
    }
}
