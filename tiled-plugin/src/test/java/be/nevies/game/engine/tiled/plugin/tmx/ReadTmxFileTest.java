/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.tmx;

import be.nevies.game.engine.tiled.plugin.map.Map;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for ReadTmxFile class.
 *
 * @author drs
 */
public class ReadTmxFileTest {
    
    @Test
    public void getMapFromTmxFile() {
        ReadTmxFile read = new ReadTmxFile();
        Map mapFromTmxFile = read.getMapFromTmxFile();
        assertNotNull(mapFromTmxFile);
        assertEquals(3, mapFromTmxFile.getLayerOrObjectgroup().size());
    }
}
