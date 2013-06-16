package be.nevies.game.engine.core.collision;

/**
 * The position of a point to worths a line.
 *
 * @author drs
 */
public enum PointPosition {

    LEFT((byte) 1),
    RIGHT((byte) 2),
    ON((byte) 4);
    byte code;

    /**
     * Default constructor.
     *
     * @param code The byte code.
     */
    private PointPosition(byte code) {
        this.code = code;
    }

    /**
     * @return Returns the byte code presentation of the position.
     */
    public byte getCode() {
        return code;
    }
}
