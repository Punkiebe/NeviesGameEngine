/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.tmx;

import be.nevies.game.engine.tiled.plugin.map.Layer;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.map.Objectgroup;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Base64Data;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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
        assertEquals(3, mapFromTmxFile.getLayerOrObjectgroup().size());
        for (Object obj : mapFromTmxFile.getLayerOrObjectgroup()) {
            if (obj instanceof Layer) {
                Layer layer = (Layer) obj;
                if (layer.getData() != null) {
                    layer.getData().getvalue();
                    System.out.println(">> data : " + layer.getData().getvalue());
                    
                    BASE64Decoder base64 = new BASE64Decoder();
                    byte[] decodeBuffer = base64.decodeBuffer(layer.getData().getvalue());
                    ByteArrayInputStream inputStream = new ByteArrayInputStream(decodeBuffer);
                    StringWriter writer = new StringWriter();
                    
                }
                
            } else if (obj instanceof Objectgroup) {
                System.out.println(">> object of type " + obj.getClass());
            } else {
                System.out.println("ERROR!! Should not happen!");
            }
        }
    }
}
