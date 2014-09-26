package dk.itu.mario.yufanlu_level;

import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.level.Level;

import java.util.Random;

public class AltLevel extends Level implements LevelInterface {
    private int mFoldingTimes;
    private int[] mFloorHeight;
    private final static int sBaseFloorHeight = 4;
    private int mHillsNum;
    private int mPitsNum;
    private Random mRnd = new Random();

    // Constructor: int, int, int -> AltLevels
    //     This function constructs a AltLevel class
    // Arguments:
    //     width is the width of floor
    //     height is the height of floor
    //     foldingTimes is the number of times of evolution
    // Return: void
    public AltLevel(int width, int height, int foldingTimes) {
        super(width, height);
        mFoldingTimes = foldingTimes;
        mFloorHeight = new int[width];
        mHillsNum = width >> 3;
        mPitsNum = 10;
        for (int i = 0; i < width; i++) {
            mFloorHeight[i] = sBaseFloorHeight;
        }
        Initialize();
    }

    // Initialization: num, num, num, num -> BinaryEvolution
    //     This function initializes the evolution process
    // Arguments:
    //     start and end mark the beginning and ending of recursion
    //     and the count of recursion won't be larger than lg(width)
    //     recursionTimes is the number of times of recursion
    //     currentHeight is the height of current floor
    // Return: void
    public void Initialize() {
        BinaryEvolution(0, width, mFoldingTimes, sBaseFloorHeight);
        for (int i = 0; i < mFloorHeight.length; i++) {
            System.out.printf("%d", mFloorHeight[i]);
        }
        System.out.println();
        CalibrateHeightsTooClose(0, 1);
        CalibrateHeightsTooHigh(0, 4);
        for (int i = 0; i < mFloorHeight.length; i++) {
            System.out.printf("%d", mFloorHeight[i]);
        }
        System.out.println();
        GenerateBaseMap();
        SettingUpFinishingPoint(2);
    }

    // FillAreaWith: int, int, byte -> FillAreaWith
    // to fill the area under the current patch (restrained by x and h) with picIndex
    private void FillAreaWith(int x, int h, byte picIndex) {
        // fill the area
        for (int i = h - 1; i >= 0; i--) {
            setBlock(x, getHeight() - i, picIndex);
        }
    }

    // BinaryEvolution: int, int. int, int -> BinaryEvolution
    //     This function divides the given terrain into random segments, assign height
    //     to each segment randomly, and execute recursion on each segment
    // Arguments:
    //     start and end mark the beginning and ending of recursion
    //     and the count of recursion won't be larger than lg(width)
    //     recursionTimes is the number of times of recursion
    //     currentHeight is the height of current floor
    // Return: void
    private void BinaryEvolution(int start, int end, int recursionTimes, int currentHeight) {
        if (recursionTimes <= 0) {
            return;
        }
        if (start - end >= -1) {
            return;
        }
        for (int i = start; i < end; i++) {
            mFloorHeight[i] = currentHeight;
        }

        int cutCount = mRnd.nextInt(3) + 2;
        int cutOffset = (end - start) / cutCount;
        if (cutOffset <= 1) {
            return;
        }
        System.out.println(cutCount);
        System.out.println(cutOffset);
        int oldPointer = start;
        int newPointer = start + cutOffset;
        while (newPointer < end) {
            System.out.println("R-Times: " + recursionTimes + "; Old: " + oldPointer + "; New: " + newPointer);
            RecursionAfterSettingNextWidth(oldPointer, newPointer, recursionTimes, currentHeight);
            oldPointer = newPointer;
            newPointer += cutOffset;
        }
        System.out.println("R-Times: " + recursionTimes + "; Old: " + oldPointer + "; New: " + newPointer);
        RecursionAfterSettingNextWidth(oldPointer, end, recursionTimes, currentHeight);
//        System.out.println("Old: " + oldPointer + "; New: " + newPointer);

//        int nextHeightOffset1 = currentHeight + mRnd.nextInt(6) - 3;
//        if (nextHeightOffset1 < 1) {
//            nextHeightOffset1 = 1;
//        } else if (nextHeightOffset1 >= width) {
//            nextHeightOffset1 = width - 1;
//        }
//        BinaryEvolution(start, mid, recursionTimes - 1, nextHeightOffset1);
//
//        int nextHeightOffset2 = currentHeight + mRnd.nextInt(6) - 3;
//        if (nextHeightOffset2 < 1) {
//            nextHeightOffset2 = 1;
//        } else if (nextHeightOffset2 >= width) {
//            nextHeightOffset2 = width - 1;
//        }
//        BinaryEvolution(mid, end, recursionTimes - 1, nextHeightOffset2);
    }

    // MUTUALLY RECURSIVE FUNCTIONS
    private void RecursionAfterSettingNextWidth(int start, int end, int recursionTimes, int currentHeight) {
        int nextHeightOffset = currentHeight + mRnd.nextInt(5) - 2;
        if (nextHeightOffset < 1) {
            nextHeightOffset = 1;
        } else if (nextHeightOffset >= width) {
            nextHeightOffset = width - 1;
        }
        BinaryEvolution(start, end, recursionTimes - 1, nextHeightOffset);
    }
    // CalibrateHeightTooClose: int, int -> CalibrateHeightTooClose
    //     This function calibrates those segments with heights less than tooCloseLen
    // Arguments:
    //     startIndex marks the begin point
    //     tooCL is the length of the segments that need to be calibrated
    // Return: void
    private void CalibrateHeightsTooClose(int startIndex, int tooCloseLen) {
        if (startIndex >= width) {
            return;
        }
        int cnt = 0;
        int oldStartIndex = startIndex;
        int thisHeight = mFloorHeight[startIndex];
        for (; startIndex < width; startIndex++) {
            if (mFloorHeight[startIndex] != thisHeight) {
                break;
            }
            cnt++;
        }
        if (cnt <= tooCloseLen) {
            for (int i = oldStartIndex; i < startIndex; i++) {
                mFloorHeight[i] = mFloorHeight[(startIndex >= width ? 0 : startIndex)];
            }
        }
        CalibrateHeightsTooClose(startIndex, tooCloseLen);
    }

    // CalibrateHeightsTooHigh: int, int -> CalibrateHeightsTooHigh
    //     This function calibrates the height of platform that follows to half of its current
    //     value if it is greater than tooHighPivot
    // Arguments:
    //     startIndex marks the begin point
    //     tooHighPivot is the max length of certain platform
    // Return: void
    private void CalibrateHeightsTooHigh(int startIndex, int tooHighPivot) {
        if (startIndex >= width) {
            return;
        }
        int nextIndex = (startIndex == width - 1 ? 0 : startIndex + 1);
        if (mFloorHeight[nextIndex] - mFloorHeight[startIndex] > tooHighPivot) {
            int mid = ((mFloorHeight[nextIndex] + mFloorHeight[startIndex]) >> 1);
            mFloorHeight[nextIndex] = mFloorHeight[startIndex] = mid;
            System.out.println("Find one");
        }
        CalibrateHeightsTooHigh(startIndex + 1, tooHighPivot);
    }

    // SetupHills: int, int -> void
    //     This function uses recursion to generates hills randomly
    // Arguments:
    //     baseIndex is the starting point in X axis, which should be greater than 0
    //     cnt is the total number of hills that should be generated
    // Return: void
    private void SetupHills(int baseIndex, int cnt) {
        if (baseIndex >= width || cnt <= 0) {
            return;
        }
        System.out.println("base: " + baseIndex + "; cnt: " + cnt);
        System.out.println((width - baseIndex) >> 2);
        int startIndex = mRnd.nextInt(((width - baseIndex) >> 3) + 1) + baseIndex;
        if (startIndex >= width) {
            return;
        }
        int len = mRnd.nextInt(4) + 2;
        int currentHeight = mFloorHeight[startIndex];
        int upperHeight = currentHeight + mRnd.nextInt(3) + 2;
        // setting up piece #1
        setBlock(startIndex, getHeight() - currentHeight, MyConstants.ConvertCoordToIndex(4, 9));
        FillAreaWith(startIndex, currentHeight, MyConstants.ConvertCoordToIndex(4, 9));
        currentHeight++;
        // setting up piece #2
        int tmpHeight = Math.abs(currentHeight - upperHeight);
        for (int i = 0; i < tmpHeight; i++) {
            setBlock(startIndex, getHeight() - currentHeight, MyConstants.ConvertCoordToIndex(4, 9));
            currentHeight++;
        }
        // setting up piece #3
        setBlock(startIndex, getHeight() - currentHeight, MyConstants.ConvertCoordToIndex(4, 8));
        startIndex++;
        // setting up piece #4
        for (int i = 0; i < len; i++) {
            setBlock(startIndex, getHeight() - currentHeight, MyConstants.ConvertCoordToIndex(5, 8));
            FillAreaWith(startIndex, currentHeight, MyConstants.GRASSLAND_WALL_MIDDLE_MIDDLE);
            startIndex++;
            if (startIndex >= width) {
                return;
            }
        }
        // setting up piece #7
        setBlock(startIndex, getHeight() - currentHeight, MyConstants.ConvertCoordToIndex(6, 8));
        currentHeight--;
        // setting up piece #6
        tmpHeight = Math.abs(currentHeight - mFloorHeight[startIndex]);
        for (int i = 0; i < tmpHeight; i++) {
            setBlock(startIndex, getHeight() - currentHeight, MyConstants.ConvertCoordToIndex(6, 9));
            currentHeight--;
        }
        // setting up piece #5
        setBlock(startIndex, getHeight() - currentHeight, MyConstants.ConvertCoordToIndex(6, 9));
        FillAreaWith(startIndex, currentHeight, MyConstants.ConvertCoordToIndex(6, 9));

        SetupHills(startIndex, cnt - 1);
    }

    // SetupPits: int, int -> void
    //     This function uses recursion to generates pitfalls randomly
    // Arguments:
    //     baseIndex is the starting point in X axis, which should be greater than 0
    //     cnt is the total number of pitfalls that should be generated
    // Return: void
    private void SetupPits(int baseIndex, int cnt) {
        if (baseIndex >= width || cnt <= 0) {
            return;
        }
        int startIndex = mRnd.nextInt(mRnd.nextInt(3) + 1) + baseIndex;
        if (startIndex >= width) {
            return;
        }
        int len = startIndex + mRnd.nextInt(3) + 1;
        while (startIndex <= len) {
            mFloorHeight[startIndex] = 0;
            startIndex++;
        }
        SetupPits(startIndex + mRnd.nextInt(width / 10), cnt - 1);
    }

    // SetupMisc: int, int -> void
    //     This function uses recursion to generates pitfalls randomly
    // Arguments:
    //     baseIndex is the starting point in X axis, which should be greater than 0
    //     pivot determines where to put pipes and coins
    // Return: void
    private void SetupMisc(int baseIndex, int pivot) {
        if (baseIndex >= width) {
            return;
        }
        int currentHeight = mFloorHeight[baseIndex];
        int counter = 0;
        int next = baseIndex + 1;
        for (int i = baseIndex; i < width; i++) {
            if (mFloorHeight[i] != currentHeight) {
                next = i + 1;
                break;
            }
            counter++;
        }

        if (counter >= pivot && mRnd.nextBoolean()) {
            int len = counter - (pivot >> 1);
            int bonusHeight = mRnd.nextInt(2) + 3;
            byte bonusType = MyConstants.ConvertCoordToIndex(2, 2);
            int blockType = mRnd.nextInt(4);
            if (blockType == 0) {
                bonusType = MyConstants.ConvertCoordToIndex((mRnd.nextBoolean() ? 1 : 5), 1);
            } else if (blockType == 1) {
                bonusType = MyConstants.ConvertCoordToIndex(3, 1);
            }
            for (int i = baseIndex; i < len + baseIndex; i++) {
                if (mRnd.nextInt(5) == 0 && bonusType == MyConstants.ConvertCoordToIndex(5, 1)) {
                    setBlock(i, getHeight() - (mFloorHeight[i] + bonusHeight), MyConstants.ConvertCoordToIndex(6, 1));
                } else {
                    setBlock(i, getHeight() - (mFloorHeight[i] + bonusHeight), bonusType);
                }
            }
        } else if (counter > 2) {
            int pipePosX = baseIndex;
            int pipeHeight = mRnd.nextInt(3) + 2;
            int pipeThickness = 2;//mRnd.nextInt(2) + 1;
            if (mFloorHeight[pipePosX] > 0) {
                for (int i = pipePosX; i < pipePosX + pipeThickness; i++) {
                    int j;
                    for (j = mFloorHeight[i] + 1; j < mFloorHeight[i] + pipeHeight; j++) {
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
        }
        System.out.println("base: " + baseIndex + "; next: " + next);
        SetupMisc(next, pivot);
    }

    private void SetupEnemies(int startIndex) {
        if (startIndex >= width) {
            return;
        }
        int x = startIndex, y = (mFloorHeight[startIndex] + 1);
        if (x > 0 && getBlock(x, getHeight() - y) == (byte)0) {
            int tmp = mRnd.nextInt(11);
            if (tmp == 0){
                setSpriteTemplate(x, getHeight() - y, new SpriteTemplate(SpriteTemplate.ARMORED_TURTLE, false));
            } else if (tmp < 5) {
                setSpriteTemplate(x, getHeight() - y, new SpriteTemplate(SpriteTemplate.RED_TURTLE, false));
            } else {
                setSpriteTemplate(x, getHeight() - y, new SpriteTemplate(SpriteTemplate.GREEN_TURTLE, false));
            }
        }
        SetupEnemies(startIndex + mRnd.nextInt(12));
    }
    
    private void GenerateBaseMap() {
        SetupHills(0, mHillsNum);
        SetupPits(10, mPitsNum);
        SetupMisc(0, 3);
        for (int i = 0; i < mFloorHeight.length - 1; i++) {
            if (mFloorHeight[i + 1] > mFloorHeight[i]) {
                setBlock(i, getHeight() - mFloorHeight[i], MyConstants.ConvertCoordToIndex(3, 8));
                FillAreaWith(i, mFloorHeight[i], MyConstants.GRASSLAND_WALL_MIDDLE_MIDDLE);
                for (int j = mFloorHeight[i] + 1; j < mFloorHeight[i + 1]; j++) {
                    setBlock(i, getHeight() - j, MyConstants.GRASSLAND_WALL_LEFT_MIDDLE);
                }
                setBlock(i, getHeight() - mFloorHeight[i + 1], MyConstants.GRASSLAND_WALL_LEFT_UPPER);
            } else if (mFloorHeight[i + 1] < mFloorHeight[i]) {
                setBlock(i, getHeight() - mFloorHeight[i], MyConstants.ConvertCoordToIndex(2, 8));
                for (int j = mFloorHeight[i] - 1; j > mFloorHeight[i + 1]; j--) {
                    setBlock(i, getHeight() - j, MyConstants.GRASSLAND_WALL_RIGHT_MIDDLE);
                }
                setBlock(i, getHeight() - mFloorHeight[i + 1], MyConstants.ConvertCoordToIndex(3, 9));
                FillAreaWith(i, mFloorHeight[i + 1], MyConstants.GRASSLAND_WALL_MIDDLE_MIDDLE);
            } else {
                setBlock(i, getHeight() - mFloorHeight[i], MyConstants.GRASSLAND_WALL_MIDDLE_UPPER);
                FillAreaWith(i, mFloorHeight[i], MyConstants.GRASSLAND_WALL_MIDDLE_MIDDLE);
            }
        }
        setBlock(mFloorHeight.length - 1, getHeight() - mFloorHeight[mFloorHeight.length - 1], MyConstants.GRASSLAND_WALL_MIDDLE_UPPER);
        SetupEnemies(10);
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

    public static void main(String[] args) {
        AltLevel al = new AltLevel(512, 16, 5);
    }
}
