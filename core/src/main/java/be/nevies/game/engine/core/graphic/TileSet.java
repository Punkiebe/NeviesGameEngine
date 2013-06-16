package be.nevies.game.engine.core.graphic;

import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

/**
 * Base class for your own tile sets.
 *
 * @author drs
 */
public class TileSet {

    private String tileSetName;
    private Image baseImage;
    private Map<String, Rectangle2D> tileSetMap = new HashMap<>();

    /**
     * Default constructor for the sprite mapper.
     *
     * @param name The name of this tile set.
     * @param baseImage The base image to use to define the tiles.
     */
    public TileSet(String name, Image baseImage) {
        this.tileSetName = name;
        this.baseImage = baseImage;
    }

    /**
     * Define a tile to add to the map.
     *
     * @param tileName The name of the sprite.
     * @param viewport The viewport of this new sprite.
     */
    public void defineOneTile(String tileName, Rectangle2D viewport) {
        if (tileName == null || viewport == null) {
            throw new IllegalArgumentException("You can't give null objects to this method!!");
        }
        if (tileSetMap.containsKey(tileName)) {
            throw new IllegalArgumentException("This sprite name is already used in this sprite map!!");
        }
        tileSetMap.put(tileName, viewport);
    }

    /**
     * @param tileName The name of the sprite you want to get.
     * @return Returns the sprite or null if there was no sprite found for the sprite name.
     */
    public Sprite getSprite(String tileName) {
        if (tileName == null) {
            throw new IllegalArgumentException("You can't give null sprite name to this method!!");
        }
        if (tileSetMap.containsKey(tileName)) {
            return new Sprite(baseImage, tileSetMap.get(tileName));
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Tile set map : ").append(tileSetName).append(" for image : ").append(baseImage).append("\n");
        builder.append("The following tiles are present in this tile set :  \n");
        int i = 1;
        for (String key : tileSetMap.keySet()) {
            builder.append(key).append(", ");
            if (i % 10 == 0) {
                i = 1;
                builder.append("\n");
            }
            i++;
        }
        return builder.toString();
    }
}
