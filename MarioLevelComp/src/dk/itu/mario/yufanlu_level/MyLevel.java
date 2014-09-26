package dk.itu.mario.yufanlu_level;

import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.level.Level;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-26
 * Time: 下午8:37
 * To change this template use File | Settings | File Templates.
 */
public class MyLevel extends Level implements LevelInterface {
    public int mFloorHeight = 4;
    private ArrayList<RegularSequence.RegularNode> mTerrain;
    private Random mRnd = new Random();
    private int mEnemiesMaxCount = 0;

    public static final int sCloseLengthBase = 2;
    public static final int sShortLengthBase = 3;
    public static final int sMediumLengthBase = 7;
    public static final int sLongLengthBase = 10;

    /*
    * Terrain type:
    *   -- a: A normal flat ground with length of rand() % sMediumLengthBase;
    *   -- b: A pit/lower ground with length to be rand() % sMediumLengthBase;
    *   -- c: A higher ground with the length to be rand() % sLongLengthBase,
    *         height of rand() % sShortLengthBase;
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

    public MyLevel(int width, int height, ArrayList<RegularSequence.RegularNode> terrain) {
        super(width, height);
        mTerrain = terrain;
//        mEnemiesMaxCount = terrain.size() >> 1;
        System.out.println(mEnemiesMaxCount);
        Initialize();
    }

    public void Initialize() {
        GenerateTerrain();
    }

    // to load the regular expression and generate the terrain according to "abcd"
    private void GenerateTerrain() {
        int baseIndex = 0;
        while (baseIndex < width) {
            for (int i = 0; i < mTerrain.size(); i++) {
                int times = mTerrain.get(i).mRepeatTimes;
                ArrayList<String> tr = mTerrain.get(i).mCandidateTerrain;
                while (times-- != 0) {
                    int index = mRnd.nextInt(tr.size());
//                    System.out.println(index);
                    String terrainStr = tr.get(index);
//                    System.out.println(terrainStr);
                    mEnemiesMaxCount = terrainStr.length();
                    for (int j = 0; j < terrainStr.length(); j++) {
                        switch (terrainStr.charAt(j))
                        {
                            case 'a':
                            {
//                                System.out.println("Before: " + mFloorHeight);
//                                BuildUpperGroundRecursively(baseIndex + mRnd.nextInt(sCloseLengthBase) + 1, 1);
//                                System.out.println("After: " + mFloorHeight);
                                baseIndex = BuildFlatGround(baseIndex, mRnd.nextInt(sMediumLengthBase));
                            } break;

                            case 'b':
                            {
                                baseIndex = BuildTerrainTypeB(baseIndex, mRnd.nextInt(sShortLengthBase) + 1);
                            } break;

                            case 'c':
                            {
                                baseIndex = BuildUpperGround(baseIndex, mRnd.nextInt(sLongLengthBase) + 2, ChangeFloorAround(mFloorHeight, 5),
                                                             MyConstants.ConvertCoordToIndex(3, 8), MyConstants.GRASSLAND_WALL_LEFT_MIDDLE, MyConstants.GRASSLAND_WALL_LEFT_UPPER,
                                                             MyConstants.GRASSLAND_WALL_MIDDLE_UPPER,
                                                             MyConstants.ConvertCoordToIndex(3, 9), MyConstants.GRASSLAND_WALL_RIGHT_MIDDLE, MyConstants.GRASSLAND_WALL_RIGHT_UPPER,
                                                             MyConstants.GRASSLAND_WALL_MIDDLE_MIDDLE, MyConstants.GRASSLAND_WALL_MIDDLE_MIDDLE, MyConstants.GRASSLAND_WALL_MIDDLE_MIDDLE);
                            } break;

                            case 'd':
                            {
                                baseIndex = BuildTerrainTypeD(baseIndex, 2, mRnd.nextInt(sLongLengthBase) + 2);
                            } break;
                        }
                    }
                    baseIndex = BuildInterval(baseIndex, MyConstants.GRASSLAND_WALL_MIDDLE_UPPER);
//                    baseIndex = BuildInterval(baseIndex, MyConstants.DUNGEON_WALL_MIDDLE_UPPER);
                }
            }
        }
        SettingUpFinishingPoint(2);
    }

    /*
    * -------------------------------
    * |                             |
    * |                             |
    * |                             |
    * --------------<-xOffsetToEnd->E
    * xOffsetToEnd
    * y position is found automatically
    * */
    private void SettingUpFinishingPoint(int xOffsetToEnd) {
        this.xExit = getWidth() - xOffsetToEnd;
        this.yExit = getHeight();
        for (int i = 0; i < height; i++) {
            if (getBlock(this.xExit, i) != 0) {
                this.yExit = i;
                break;
            }
        }
    }

    private int ChangeFloorAround(int currentHeight, int targetHeight) {
        int offset = mRnd.nextInt(3);
//        System.out.println(offset);
        if (currentHeight < targetHeight) {   // plus
//            if (currentHeight + offset < height) {
                currentHeight += offset;
//            } else {
//                currentHeight = height - 1;
//            }
        } else {
//            if (currentHeight - offset > 0) {
                currentHeight -= offset;
//            } else {
//                currentHeight = 1;
//            }
        }
        if (currentHeight >= height - 1) {
            return height - 2;
        }
        if (currentHeight <= 1) {
            return 2;
        }
        System.out.println(currentHeight);
        return currentHeight;
    }

//    private int ChangeFloorAroundOffset(int currentHeight, int offset) {
//        if (currentHeight + offset > 1 && currentHeight + offset < height - 1) {
//            return ChangeFloorAround(currentHeight, )
//        }
//        return ;
//    }

    private void FillAreaWith(int x, byte picIndex) {
        // fill the area
        for (int i = mFloorHeight - 1; i >= 0; i--) {
//            if (getBlock(x, getHeight() - i) == 0) {
                setBlock(x, getHeight() - i, picIndex);
//            }
        }
    }
    
    private int BuildInterval(int startIndex, byte intervalType) {
        setBlock(startIndex, getHeight() - mFloorHeight, intervalType);
        FillAreaWith(startIndex, MyConstants.ConvertCoordToIndex(1, 9));
        return startIndex + 1;
    }

    private void SetupPipes(int startIndex) {
        int pipePosX = startIndex;
        int pipeHeight = mRnd.nextInt(3) + 2;
        int pipeThickness = 2;//mRnd.nextInt(2) + 1;
        for (int i = pipePosX; i < pipePosX + pipeThickness; i++) {
            int j;
            for (j = mFloorHeight + 1; j < mFloorHeight + pipeHeight; j++) {
                setBlock(i, getHeight() - j, MyConstants.ConvertCoordToIndex(10 + i - pipePosX, 1));
            }
            setBlock(i, getHeight() - j, MyConstants.ConvertCoordToIndex(10 + i - pipePosX, 0));
            if (i == pipePosX) {
                if (mRnd.nextInt(3) == 0 && i > 10) {
                    setSpriteTemplate(i, getHeight() - j, new SpriteTemplate(SpriteTemplate.JUMP_FLOWER, false));
                }
            }
        }
    }

    private void SetupCoins(int startIndex, int len) {
        if (startIndex > width - 5) {
            return;
        }
        // setting up coins
        startIndex++;
        int bonusHeight = mRnd.nextInt(2) + 3;
        byte bonusType = MyConstants.ConvertCoordToIndex(2, 2);
        int blockType = mRnd.nextInt(4);
        if (blockType == 0) {
            bonusType = MyConstants.ConvertCoordToIndex((mRnd.nextBoolean() ? 1 : 5), 1);
        } else if (blockType == 1) {
            bonusType = MyConstants.ConvertCoordToIndex(3, 1);
        }
        for (int i = startIndex; i < startIndex + len; i++) {
            if (mRnd.nextInt(5) == 0 && bonusType == MyConstants.ConvertCoordToIndex(5, 1)) {
//            if (true) {
                setBlock(i, getHeight() - (mFloorHeight + bonusHeight), MyConstants.ConvertCoordToIndex(6, 1));
            } else {
                setBlock(i, getHeight() - (mFloorHeight + bonusHeight), bonusType);
            }
        }
    }

    private int BuildFlatGround(int startIndex, int len) {
//        int len = mRnd.nextInt(sMediumLengthBase);
        int tmp = startIndex + (len >> 2);
        if (len > 2 && getBlock(tmp, mFloorHeight) == 0) {
            SetupPipes(tmp);
        }
        for (int i = 0; i < len; i++) {
            setBlock(startIndex, getHeight() - mFloorHeight, MyConstants.GRASSLAND_WALL_MIDDLE_UPPER);
            FillAreaWith(startIndex, MyConstants.ConvertCoordToIndex(1, 9));
            startIndex++;
        }
        return startIndex;
    }

    // ATTN: ***This function is changing the mFloorHeight!!***
    /*
    * ---       ---
    * |1|       |5|
    * ---       ---
    * |*|       |*|
    * |2|       |6|
    * |*|       |*|
    * -------------
    * |3|***4***|7|
    * -------------
    * 1: cornerLeftUpper
    * 2: edgeLeft
    * 3: cornerLeftLower
    * 4: ground
    * 5: cornerRightUpper
    * 6: edgeRight
    * 7: cornerRightLower
    * */
    private int BuildLowerGround(int startIndex, int len, int finalHeight, boolean isPit, boolean hasPipes,
                                 byte cornerLeftUpper, byte edgeLeft, byte cornerLeftLower,
                                 byte ground,
                                 byte cornerRightUpper, byte edgeRight, byte cornerRightLower) {
        if (len <= 0) {
            return startIndex;
        }
        int lowerHeight = 0;
        if (!isPit) {
            lowerHeight = Math.min(mFloorHeight, finalHeight) - mRnd.nextInt(4) - 1;
            if (lowerHeight < 1) {
                lowerHeight = 1;
            }
//            System.out.println(mFloorHeight + " " + lowerHeight);
        }
        // setting up piece #1
        setBlock(startIndex, getHeight() - mFloorHeight, cornerLeftUpper);
        mFloorHeight--;
        // setting up piece #2
        int tmpHeight = Math.abs(mFloorHeight - lowerHeight);
        for (int i = 0; i < tmpHeight; i++) {
            setBlock(startIndex, getHeight() - mFloorHeight, edgeLeft);
            mFloorHeight--;
        }
        // setting up piece #3
        setBlock(startIndex, getHeight() - mFloorHeight, cornerLeftLower);
        // fill the area below #3
        FillAreaWith(startIndex, MyConstants.ConvertCoordToIndex(1, 9));
        startIndex++;
        // the pipes and coins
        if (hasPipes) {
            if (!isPit && len > 2) { //mRnd.nextInt(sShortLengthBase) + 1
                SetupPipes(startIndex + (len >> 2));
            } else if (!isPit && len > 2) {
                SetupCoins(startIndex, len - mRnd.nextInt(3) - 1);
            }
        }

        // setting up piece #4
        // setup the enemies
        int enemiesCnt = 0;
        boolean enemiesExist = mRnd.nextBoolean();
        for (int i = 0; i < len; i++) {
            setBlock(startIndex, getHeight() - mFloorHeight, ground);
            FillAreaWith(startIndex, MyConstants.ConvertCoordToIndex(1, 9));
            if (!hasPipes && enemiesExist) {
                if (startIndex > 10 && enemiesCnt < mEnemiesMaxCount && mRnd.nextInt(3) == 0) {
                    int tmp = mRnd.nextInt(16);
                    if (tmp == 0) {
                        setSpriteTemplate(startIndex, getHeight() - (mFloorHeight + 1), new SpriteTemplate(SpriteTemplate.ARMORED_TURTLE, false));
                    } else if (tmp < 5) {
                        setSpriteTemplate(startIndex, getHeight() - (mFloorHeight + 1), new SpriteTemplate(SpriteTemplate.RED_TURTLE, false));
                    } else if (tmp < 10) {
                        setSpriteTemplate(startIndex, getHeight() - (mFloorHeight + 1), new SpriteTemplate(SpriteTemplate.GREEN_TURTLE, false));
                    } else {
                        setSpriteTemplate(startIndex, getHeight() - (mFloorHeight + 1), new SpriteTemplate(SpriteTemplate.GOOMPA, false));
                    }
                    enemiesCnt++;
                }
            }
            startIndex++;
        }
        // setting up piece #7
        setBlock(startIndex, getHeight() - mFloorHeight, cornerRightLower);
        FillAreaWith(startIndex, MyConstants.ConvertCoordToIndex(1, 9));
        mFloorHeight++;
        // setting up piece #6
        tmpHeight = Math.abs(mFloorHeight - finalHeight);
        for (int i = 0; i < tmpHeight; i++) {
            setBlock(startIndex, getHeight() - mFloorHeight, edgeRight);
            mFloorHeight++;
        }
        // setting up piece #5
        setBlock(startIndex, getHeight() - mFloorHeight, cornerRightUpper);

        // return the prior x position
        return startIndex + 1;
    }
    // ATTN: ***This function is changing the mFloorHeight!!***
    /*
    * -------------
    * |3|***4***|7|
    * -------------
    * |*|       |*|
    * |2|       |6|
    * |*|       |*|
    * ---       ---
    * |1|       |5|
    * ---       ---
    * 1: cornerLeftLower
    * 2: edgeLeft
    * 3: cornerLeftUpper
    * 4: ground
    * 5: cornerRightLower
    * 6: edgeRight
    * 7: cornerRightUpper
    * */
    private int BuildUpperGround(int startIndex, int len, int finalHeight,
                                 byte cornerLeftLower, byte edgeLeft, byte cornerLeftUpper,
                                 byte ground,
                                 byte cornerRightLower, byte edgeRight, byte cornerRightUpper,
                                 byte fillLeftEdge, byte fillMiddle, byte fillRightEdge) {
        if (len <= 0) {
            return startIndex;
        }
//        System.out.println(finalHeight);
        int upperHeight = mFloorHeight + mRnd.nextInt(4);
        // setting up piece #1
        setBlock(startIndex, getHeight() - mFloorHeight, cornerLeftLower);
        FillAreaWith(startIndex, fillLeftEdge);
        mFloorHeight++;
        // setting up piece #2
        int tmpHeight = Math.abs(mFloorHeight - upperHeight);
        for (int i = 0; i < tmpHeight; i++) {
            setBlock(startIndex, getHeight() - mFloorHeight, edgeLeft);
            mFloorHeight++;
        }
        // setting up piece #3
        setBlock(startIndex, getHeight() - mFloorHeight, cornerLeftUpper);
        startIndex++;
        SetupCoins(startIndex, len - mRnd.nextInt(3) - 1);

        // setting up piece #4
        // as well as the enemies
        int enemiesCnt = 0;
        boolean enemiesExist = mRnd.nextBoolean();
        for (int i = 0; i < len; i++) {
            setBlock(startIndex, getHeight() - mFloorHeight, ground);
            FillAreaWith(startIndex, fillMiddle);
            if (enemiesExist) {
                if (startIndex > 10 && enemiesCnt < mEnemiesMaxCount && mRnd.nextInt(3) == 0) {
                    int tmp = mRnd.nextInt(16);
                    if (tmp == 0) {
                        setSpriteTemplate(startIndex, getHeight() - (mFloorHeight + 1), new SpriteTemplate(SpriteTemplate.ARMORED_TURTLE, false));
                    } else if (tmp < 5) {
                        setSpriteTemplate(startIndex, getHeight() - (mFloorHeight + 1), new SpriteTemplate(SpriteTemplate.RED_TURTLE, false));
                    } else if (tmp < 10) {
                        setSpriteTemplate(startIndex, getHeight() - (mFloorHeight + 1), new SpriteTemplate(SpriteTemplate.GREEN_TURTLE, false));
                    } else {
                        setSpriteTemplate(startIndex, getHeight() - (mFloorHeight + 1), new SpriteTemplate(SpriteTemplate.GOOMPA, false));
                    }
                    enemiesCnt++;
                }
            }
            startIndex++;
        }
        // setting up piece #7
        setBlock(startIndex, getHeight() - mFloorHeight, cornerRightUpper);
        mFloorHeight--;
        // setting up piece #6
        tmpHeight = Math.abs(mFloorHeight - finalHeight);
        for (int i = 0; i < tmpHeight; i++) {
            setBlock(startIndex, getHeight() - mFloorHeight, edgeRight);
            mFloorHeight--;
        }
        // setting up piece #5
        setBlock(startIndex, getHeight() - mFloorHeight, cornerRightLower);
        FillAreaWith(startIndex, fillRightEdge);

        return startIndex + 1;
    }
    
    private int BuildTerrainTypeB(int startIndex, int len) {
        int res = BuildLowerGround(startIndex, len, ChangeFloorAround(mFloorHeight, 5), (mRnd.nextInt() % 4 == 0), true,
                                   MyConstants.GRASSLAND_WALL_RIGHT_UPPER, MyConstants.GRASSLAND_WALL_RIGHT_MIDDLE, MyConstants.ConvertCoordToIndex(3, 9),
                                   MyConstants.GRASSLAND_WALL_MIDDLE_UPPER,
                                   MyConstants.GRASSLAND_WALL_LEFT_UPPER, MyConstants.GRASSLAND_WALL_LEFT_MIDDLE, MyConstants.ConvertCoordToIndex(3, 8));
        return res;
    }

    private int BuildTerrainTypeD(int startIndex, int depth, int len) {
        BuildUpperGroundRecursively(startIndex + mRnd.nextInt(sCloseLengthBase) + 1, 2);
        int finalIndex = BuildLowerGround(startIndex, len, mFloorHeight, (len > 5 ? false : (mRnd.nextInt() % 5 == 0)), false,
                                          MyConstants.GRASSLAND_WALL_RIGHT_UPPER, MyConstants.GRASSLAND_WALL_RIGHT_MIDDLE, MyConstants.ConvertCoordToIndex(3, 9),
                                          MyConstants.GRASSLAND_WALL_MIDDLE_UPPER,
                                          MyConstants.GRASSLAND_WALL_LEFT_UPPER, MyConstants.GRASSLAND_WALL_LEFT_MIDDLE, MyConstants.ConvertCoordToIndex(3, 8));

        return finalIndex;
    }

    private void BuildUpperGroundRecursively(int startIndex, int depth) {
        if (depth <= 0) {
            return;
        }
        int tmp = BuildUpperGround(startIndex + mRnd.nextInt(sCloseLengthBase), mRnd.nextInt(sCloseLengthBase), mFloorHeight,
                                   MyConstants.ConvertCoordToIndex(4, 9), MyConstants.ConvertCoordToIndex(4, 9), MyConstants.ConvertCoordToIndex(4, 8),
                                   MyConstants.ConvertCoordToIndex(5, 8),
                                   MyConstants.ConvertCoordToIndex(6, 9), MyConstants.ConvertCoordToIndex(6, 9), MyConstants.ConvertCoordToIndex(6, 8),
                                   MyConstants.ConvertCoordToIndex(4, 9), MyConstants.ConvertCoordToIndex(5, 9), MyConstants.ConvertCoordToIndex(6, 9));
        BuildUpperGroundRecursively(tmp + mRnd.nextInt(sCloseLengthBase) + 1, depth - 1);
    }
}