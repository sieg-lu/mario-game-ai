package dk.itu.mario.Test;

import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.level.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Yufan
 * Date: 13-10-24
 * Time: 下午1:29
 * To change this template use File | Settings | File Templates.
 */
public class TestLevel extends Level implements LevelInterface {
    int floor;
    
    public TestLevel(int width, int height) {
        super(width, height);
        floor = height - 4;
        Initialize();
    }

    public void Initialize() {
        byte cnt = 0;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < getWidth(); x++) {
//                if (y >= floor) {
                    setBlock(x, y, COIN);
//                }
            }
        }
    }
}
