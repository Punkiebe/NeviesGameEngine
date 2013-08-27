/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.tmx;

import be.nevies.game.engine.tiled.plugin.map.Map;
import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 */
public class ReadTmxFile {
    
    /* Logger. */
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ReadTmxFile.class);
    
    public Map getMapFromTmxFile() {
        try {
            JAXBContext context = JAXBContextImpl.newInstance("be.nevies.game.engine.tiled.plugin.map");
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Map tmxMap = (Map) unmarshaller.unmarshal(ReadTmxFile.class.getResourceAsStream("/be/nevies/game/engine/tiled/plugin/example/firstHouse.tmx"));
            return tmxMap;
        } catch (JAXBException ex) {
            LOG.error("There where problem when triing to get the map from the tmx file. {}", ex.getMessage());
            ex.printStackTrace();
        }
        
        return null;
    }
}
