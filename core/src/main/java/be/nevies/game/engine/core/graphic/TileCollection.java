package be.nevies.game.engine.core.graphic;

import java.util.HashMap;
import java.util.Map;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;

/**
 * Base class for your own tile collection.
 *
 * @author drs
 */
public class TileCollection {

    private String tileCollectionName;
    private Image baseImage;
    private Map<String, Rectangle2D> tileCollectionMap = new HashMap<>();

    /**
     * Default constructor for the tile collection.
     *
     * @param name The name of this tile collection.
     * @param baseImage The base image to use to define the tiles.
     */
    public TileCollection(String name, Image baseImage) {
        this.tileCollectionName = name;
        this.baseImage = baseImage;
    }

    /**
     * Define a tile to add to the collection.
     *
     * @param tileName The name of the tile.
     * @param viewport The viewport of this new tile.
     */
    public void defineOneTile(String tileName, Rectangle2D viewport) {
        if (tileName == null || viewport == null) {
            throw new IllegalArgumentException("You can't give null objects to this method!!");
        }
        if (tileCollectionMap.containsKey(tileName)) {
            throw new IllegalArgumentException("This tile name is already used in this collection!!");
        }
        tileCollectionMap.put(tileName, viewport);
    }

    /**
     * @param tileName The name of the tile you want to get.
     * @return Returns a new sprite or null if there was no tile found with this name.
     */
    public Sprite getSprite(String tileName) {
        if (tileName == null) {
            throw new IllegalArgumentException("You can't give 'null' as tile name to this method!!");
        }
        if (tileCollectionMap.containsKey(tileName)) {
            return new Sprite(baseImage, tileCollectionMap.get(tileName));
        }
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Tile collection : ").append(tileCollectionName).append(" for image : ").append(baseImage).append("\n");
        builder.append("The following tiles are present in this tile collection :  \n");
        int i = 1;
        for (String key : tileCollectionMap.keySet()) {
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
