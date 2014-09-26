package dk.itu.mario.Test;

import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelGenerator;
import dk.itu.mario.MarioInterface.LevelInterface;

/**
 * Created by IntelliJ IDEA.
 * User: Yufan
 * Date: 13-10-24
 * Time: 下午2:01
 * To change this template use File | Settings | File Templates.
 */
public class TestLevelGenerator implements LevelGenerator {
    @Override
    public LevelInterface generateLevel(GamePlay playerMetrics) {
        LevelInterface tl = new TestLevel(16, 16);
        return tl;
    }
}
