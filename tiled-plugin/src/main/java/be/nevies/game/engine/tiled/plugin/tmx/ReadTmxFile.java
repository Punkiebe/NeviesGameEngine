/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.tmx;

import be.nevies.game.engine.tiled.plugin.map.Map;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
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
            JAXBContext context = JAXBContext.newInstance("be.nevies.game.engine.tiled.plugin.map");
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Map tmxMap = (Map) unmarshaller.unmarshal(ReadTmxFile.class.getResourceAsStream("/be/nevies/game/engine/tiled/plugin/example/firstHouseXML.tmx"));
            return tmxMap;
        } catch (JAXBException ex) {
            LOG.error("There where problem when triing to get the map from the tmx file. {}", ex.getMessage());
            ex.printStackTrace();
        }
        
        return null;
    }
}
