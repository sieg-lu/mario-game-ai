package dk.itu.mario.yufanlu_level;

/**
 * Created by IntelliJ IDEA.
 * User: Yufan
 * Date: 13-10-24
 * Time: 下午4:59
 * To change this template use File | Settings | File Templates.
 */
public class MyConstants {
    // sprite(x, y)
    // => (byte) (x + (y << 16))

    // Observed from mapsheet.png and mapsheet_legend.png
    public static final byte TURRET_UPPER                    = ConvertCoordToIndex(14, 0);
    public static final byte TURRET_MIDDLE                   = ConvertCoordToIndex(14, 1);
    public static final byte TURRET_LOWER                    = ConvertCoordToIndex(14, 2);

    public static final byte DUNGEON_WALL_LEFT_UPPER         = ConvertCoordToIndex(8, 8);
    public static final byte DUNGEON_WALL_LEFT_MIDDLE        = ConvertCoordToIndex(8, 9);
    public static final byte DUNGEON_WALL_LEFT_LOWER         = ConvertCoordToIndex(8, 10);
    public static final byte DUNGEON_WALL_MIDDLE_UPPER       = ConvertCoordToIndex(9, 8);
    public static final byte DUNGEON_WALL_MIDDLE_MIDDLE      = ConvertCoordToIndex(9, 9);
    public static final byte DUNGEON_WALL_MIDDLE_LOWER       = ConvertCoordToIndex(9, 10);
    public static final byte DUNGEON_WALL_RIGHT_UPPER        = ConvertCoordToIndex(10, 8);
    public static final byte DUNGEON_WALL_RIGHT_MIDDLE       = ConvertCoordToIndex(10, 9);
    public static final byte DUNGEON_WALL_RIGHT_LOWER        = ConvertCoordToIndex(10, 10);

    public static final byte UNDERGROUND_WALL_LEFT_UPPER     = ConvertCoordToIndex(12, 8);
    public static final byte UNDERGROUND_WALL_LEFT_MIDDLE    = ConvertCoordToIndex(12, 9);
    public static final byte UNDERGROUND_WALL_LEFT_LOWER     = ConvertCoordToIndex(12, 10);
    public static final byte UNDERGROUND_WALL_MIDDLE_UPPER   = ConvertCoordToIndex(13, 8);
    public static final byte UNDERGROUND_WALL_MIDDLE_MIDDLE  = ConvertCoordToIndex(13, 9);
    public static final byte UNDERGROUND_WALL_MIDDLE_LOWER   = ConvertCoordToIndex(13, 10);
    public static final byte UNDERGROUND_WALL_RIGHT_UPPER    = ConvertCoordToIndex(14, 8);
    public static final byte UNDERGROUND_WALL_RIGHT_MIDDLE   = ConvertCoordToIndex(14, 9);
    public static final byte UNDERGROUND_WALL_RIGHT_LOWER    = ConvertCoordToIndex(14, 10);

    public static final byte GRASSLAND_WALL_LEFT_UPPER       = ConvertCoordToIndex(0, 8);
    public static final byte GRASSLAND_WALL_LEFT_MIDDLE      = ConvertCoordToIndex(0, 9);
    public static final byte GRASSLAND_WALL_LEFT_LOWER       = ConvertCoordToIndex(0, 10);
    public static final byte GRASSLAND_WALL_MIDDLE_UPPER     = ConvertCoordToIndex(1, 8);
    public static final byte GRASSLAND_WALL_MIDDLE_MIDDLE    = ConvertCoordToIndex(1, 9);
    public static final byte GRASSLAND_WALL_MIDDLE_LOWER     = ConvertCoordToIndex(1, 10);
    public static final byte GRASSLAND_WALL_RIGHT_UPPER      = ConvertCoordToIndex(2, 8);
    public static final byte GRASSLAND_WALL_RIGHT_MIDDLE     = ConvertCoordToIndex(2, 9);
    public static final byte GRASSLAND_WALL_RIGHT_LOWER      = ConvertCoordToIndex(2, 10);

    public static byte ConvertCoordToIndex(int x, int y) {
        assert(x >= 0 && x < 16 && y >= 0 && y < 16);
        return (byte) (x + (y << 4));
    }
}
