/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.tmx;

import be.nevies.game.engine.tiled.plugin.core.TileCollectionCreatorTest;
import be.nevies.game.engine.tiled.plugin.map.LayerType;
import be.nevies.game.engine.tiled.plugin.map.Map;
import be.nevies.game.engine.tiled.plugin.map.ObjectgroupType;
import be.nevies.game.engine.tiled.plugin.map.TileLayerType;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBElement;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for ReadTmxFile class.
 *
 * @author drs
 */
public class ReadTmxFileTest {

    @Test
    public void getMapFromTmxFile() throws IOException {
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
        assertEquals(4, mapFromTmxFile.getLayerOrObjectgroupOrImagelayer().size());
        for (Serializable obj : mapFromTmxFile.getLayerOrObjectgroupOrImagelayer()) {
            if (obj instanceof LayerType) {
                LayerType layer = (LayerType) obj;
                if (layer.getData() != null) {
                    layer.getData().getContent();
                    List<Serializable> content = layer.getData().getContent();
                    
                    for (Serializable con : content) {
                        if (con instanceof JAXBElement) {
                            JAXBElement jaxbEl = (JAXBElement) con;
                            
                            if (jaxbEl.getValue() instanceof TileLayerType) {
                                TileLayerType tileLayer = (TileLayerType) jaxbEl.getValue();
                            }
                            
                        }
                    }
                    
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
