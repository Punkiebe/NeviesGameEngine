/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.core;

import be.nevies.game.engine.core.graphic.TileCollection;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.tmx.ReadTmxFile;
import java.util.Collection;
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
        ReadTmxFile read = new ReadTmxFile();
        Map mapFromTmxFile = read.getMapFromTmxFile();
        assertNotNull(mapFromTmxFile);
        Collection<TileCollection> tileCols = TileCollectionCreator.createTileCollectionsFromMap(mapFromTmxFile);
        assertNotNull(tileCols);
        assertEquals(1, tileCols.size());
    }
}
