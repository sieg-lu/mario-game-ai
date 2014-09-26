package dk.itu.mario.yufanlu_level;

import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelGenerator;
import dk.itu.mario.MarioInterface.LevelInterface;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-30
 * Time: 下午5:18
 * To change this template use File | Settings | File Templates.
 */
public class AltLevelGenerator implements LevelGenerator {
    private int mWidth;
    private int mHeight;

    public AltLevelGenerator(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    @Override
    public LevelInterface generateLevel(GamePlay playerMetrics) {
        LevelInterface tl = new AltLevel(mWidth, mHeight, 4);
        return tl;
    }
}
