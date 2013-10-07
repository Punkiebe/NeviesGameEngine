/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package be.nevies.game.engine.tiled.plugin.tmx;

import be.nevies.game.engine.tiled.plugin.map.Map;
import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.slf4j.LoggerFactory;

/**
 *
 * @author drs
 * 
 * @since 1.0.0
 */
public class ReadTmxFile {

    /* Logger. */
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ReadTmxFile.class);
    private File tmxFile;

    /**
     * Constructor.
     * @param tmxFile The tmx file.
     */
    public ReadTmxFile(File tmxFile) {
        if (tmxFile == null || !tmxFile.exists()) {
            throw new IllegalArgumentException("The tmx file must exist!");
        }
        this.tmxFile = tmxFile;
    }

    public Map getMapFromTmxFile() {
        try {
            JAXBContext context = JAXBContext.newInstance(Map.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Map tmxMap = (Map) unmarshaller.unmarshal(tmxFile);
            return tmxMap;
        } catch (JAXBException ex) {
            LOG.error("There where problem when triing to get the map from the tmx file. {}", ex.getMessage());
        }

        return null;
    }
}
