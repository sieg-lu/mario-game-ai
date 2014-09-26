package dk.itu.mario.yufanlu_level;

import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelGenerator;
import dk.itu.mario.MarioInterface.LevelInterface;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-26
 * Time: 下午8:37
 * To change this template use File | Settings | File Templates.
 */
public class MyLevelGenerator implements LevelGenerator {
    private int mWidth;
    private int mHeight;
//    private String mPattern = "abc(bd|ca|dd)+(bcc|adada)*cdba";
    private String mPattern = "acc(db|dabc|dba|cbcac|dbcda)+(ddbac|ccba|acbac|b)*da";
//    private String mPattern = "ad(ab|ba|cdc|da)+dad(c|a)+";
//    private String mPattern = "a";

    private RegularSequence mRegSeq;
    /*
    * Terrain type:
    *   -- a: A normal flat ground with length of rand() % sLongLengthBase;
    *   -- b: A pitfall with length to be rand() % sShortLengthBase;
    *   -- c: A higher ground with the length to be rand() % sLongLengthBase,
     *        height of rand() % sShortLengthBase;
    *   -- d:
    *           -----
    *   ----- 1 | 3 | 2 ---
    *       |   |   |   |
    *       -------------
    *       with:
    *           1 -- depth: rand() % sShortLengthBase, length: rand() % sCloseLengthBase
    *           2 -- depth: equals to 1's depth, length: rand() % sMediumLengthBase
    *           3 -- height (relative to initial ground): rand() % sCloseLengthBase
    *                depth: equals to 1's depth, length: rand() % sLongLengthBase
    * Each terrain is separated by ***ONE*** unit of base ground
    * Any ***OTHER*** cases will be ignored
    * */

    public MyLevelGenerator(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
        mRegSeq = new RegularSequence(mPattern);
        mRegSeq.ParseExpression();
//        mRegSeq.DebugOutput();
    }

    @Override
    public LevelInterface generateLevel(GamePlay playerMetrics) {
        LevelInterface tl = new MyLevel(mWidth, mHeight, mRegSeq.getTerrain());
        return tl;
    }
}
