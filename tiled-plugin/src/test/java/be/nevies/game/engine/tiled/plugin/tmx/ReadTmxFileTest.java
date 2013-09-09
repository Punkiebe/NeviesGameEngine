/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.tmx;

import be.nevies.game.engine.tiled.plugin.map.LayerType;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.map.ObjectgroupType;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import static org.junit.Assert.*;
import org.junit.Test;
import sun.misc.BASE64Decoder;

/**
 * Test class for ReadTmxFile class.
 *
 * @author drs
 */
public class ReadTmxFileTest {

    @Test
    public void getMapFromTmxFile() throws IOException {
        ReadTmxFile read = new ReadTmxFile();
        Map mapFromTmxFile = read.getMapFromTmxFile();
        assertNotNull(mapFromTmxFile);
        assertEquals(3, mapFromTmxFile.getLayerOrObjectgroupOrImagelayer().size());
        for (Serializable obj : mapFromTmxFile.getLayerOrObjectgroupOrImagelayer()) {
            if (obj instanceof LayerType) {
                LayerType layer = (LayerType) obj;
                if (layer.getData() != null) {
                    layer.getData().getContent();
                    System.out.println(">> content list : " + layer.getData().getContent().size());
                    System.out.println(">> content : " + layer.getData().getContent());
                    
//                    BASE64Decoder base64 = new BASE64Decoder();
//                    byte[] decodeBuffer = base64.decodeBuffer(layer.getData().getvalue());
//                    ByteArrayInputStream inputStream = new ByteArrayInputStream(decodeBuffer);
//                    StringWriter writer = new StringWriter();
                    
                }
                
            } else if (obj instanceof ObjectgroupType) {
                System.out.println(">> object of type " + obj.getClass());
            } else {
                System.out.println("ERROR!! Should not happen!");
            }
        }
    }
}
