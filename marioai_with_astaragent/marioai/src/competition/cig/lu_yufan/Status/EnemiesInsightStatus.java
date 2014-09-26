package competition.cig.lu_yufan.Status;

import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.engine.sprites.Sprite;
import ch.idsia.mario.environments.Environment;
import competition.cig.lu_yufan.TimingButton;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-13
 * Time: 下午8:44
 * To change this template use File | Settings | File Templates.
 */
public class EnemiesInsightStatus extends StatusBase {
    private static EnemiesInsightStatus ourInstance = new EnemiesInsightStatus();

    public static EnemiesInsightStatus getInstance() {
        return ourInstance;
    }

    // TODO: The EnemiesInsightStatus does not care about the KIND_SPIKY or KIND_ENEMY_FLOWER

    // ------------- Variables -------------

    private final int mStatusEntryRangeX = 7;
    private final int mStatusEntryRangePosY = 4;
    private final int mStatusEntryRangeNegY = 4;
    
    private final int mShootRangePosX = 3;
    private final int mShootRangePosY = 3;
    private final int mShootRangeNegY = 1;

    // ------------- Constructor/Destructor -------------

    private EnemiesInsightStatus() {
    }

    // ------------- Override Functions -------------

    @Override
    public boolean[] GetAction(byte[][] levelScene, byte[][] enemyScene, Environment env) {
        mButtons[Mario.KEY_RIGHT].StartImmediately(0xffff);

        /*
        *                  |-----|
        *                  | AREA 1:
        *                  | jump
        * -----------|-----| shoot fire
        *  AREA 3: speed   | v<--------EMERGENCY: jump + shoot
        * -----------|-----M***--|>
        *                  |
        *                  | AREA 2:
        *                  | shoot fire
        *                  |
        *                  |-----|
        * */
        if (CheckEnemiesInSpecifiedArea(enemyScene, 2, 0, 0, 0)) {
//            OutputScene(enemyScene);
            if (!Mario.fire || (Mario.fire && (mRnd.nextInt() % 3 == 0))) {
                mButtons[Mario.KEY_JUMP].StartImmediately(15);
            }
            mButtons[Mario.KEY_SPEED].SetRepeatedButton(1);
            mButtons[Mario.KEY_SPEED].StartImmediately(1);
        } else {
            if (CheckEnemiesInSpecifiedArea(enemyScene, 5, 0, 0, 4)) {
                if (CheckEnemyInTheAir(enemyScene, levelScene)) {
                    mButtons[Mario.KEY_DOWN].StartImmediately(5);
                } else {
                    mButtons[Mario.KEY_JUMP].Start(15, 3);
                }
                mButtons[Mario.KEY_SPEED].SetRepeatedButton(1);
                mButtons[Mario.KEY_SPEED].Start(1, 1);
            } else if (CheckEnemiesInSpecifiedArea(enemyScene, 5, 0, 4, 0, true)) {
                mButtons[Mario.KEY_SPEED].SetRepeatedButton(1);
                mButtons[Mario.KEY_SPEED].Start(1, 2);
            } else if (CheckEnemiesInSpecifiedArea(enemyScene, 0, 4, 0, 1)) {
                mButtons[Mario.KEY_SPEED].StartImmediately(0xffff);
            }
        }

//        if (CheckEnemiesInSpecifiedArea(enemyScene, 5, 0, 0, 4)) {
//            mButtons[Mario.KEY_JUMP].StartImmediately(15);
//            if (Mario.fire) {
//                if (mButtons[Mario.KEY_SPEED].HasStarted()) {
//                    mButtons[Mario.KEY_SPEED].Initialize();
//                }
//                mButtons[Mario.KEY_SPEED].SetRepeatedButton(1);
//                mButtons[Mario.KEY_SPEED].Start(1, 2);
//            } else {
//                if (mButtons[Mario.KEY_JUMP].HasStarted()) {
//                    mButtons[Mario.KEY_JUMP].Initialize();
//                }
//                mButtons[Mario.KEY_JUMP].Start(15, 0);
//            }
//        } else {
//            mButtons[Mario.KEY_JUMP].Release();
//        }
//
//        if (Mario.fire) {
//            if (CheckEnemiesInSpecifiedArea(enemyScene, 5, 0, 4, 1)) {
//                mButtons[Mario.KEY_LEFT].StartImmediately(12);
//                mButtons[Mario.KEY_SPEED].SetRepeatedButton(1);
//                mButtons[Mario.KEY_SPEED].Start(0xffff, 0);
////                System.out.println("Fire, Enemy insight.");
//            } else {
//                mButtons[Mario.KEY_SPEED].Release();
//            }
//        } else {
//            if (CheckEnemiesInSpecifiedArea(enemyScene, 4, 0, 4, 1)) {
//                mButtons[Mario.KEY_JUMP].SetRepeatedButton(1);
//                mButtons[Mario.KEY_JUMP].StartImmediately(7);
////                System.out.println("No fire, Enemy insight.");
//            } else {
//                mButtons[Mario.KEY_JUMP].Release();
//            }
//        }

        for (TimingButton t : mButtons) {
            t.Update();
        }
        FinalizeActions();
        return mAction;
    }

    @Override
    public void EnterStatus(boolean[] action, TimingButton[] buttons) {
        Initialize(action, buttons);
    }

    @Override
    public void LeaveStatus() {
        ClearActionMark();
    }

    @Override
    public boolean IsChangedToThisStatus(byte[][] levelScene, byte[][] enemyScene, Environment env) {
        return CheckEnemiesInSpecifiedArea(enemyScene, mStatusEntryRangeX, mStatusEntryRangeX, mStatusEntryRangePosY, mStatusEntryRangeNegY);
    }

    @Override
    public boolean IsLeavingThisStatus(byte[][] levelScene, byte[][] enemyScene, Environment env) {
        if (!CheckEnemiesInSpecifiedArea(enemyScene, mStatusEntryRangeX, mStatusEntryRangeX, mStatusEntryRangePosY, mStatusEntryRangeNegY)) {
            return true;
        }
        if (ObstacleStatus.getInstance().IsChangedToThisStatus(levelScene, enemyScene, env) ||
            GapStatus.getInstance().IsChangedToThisStatus(levelScene, enemyScene, env)) {
            return true;
        }
        return false;
    }

    // ------------- Utility Functions -------------

    private boolean CheckEnemyInTheAir(byte[][] enemyScene, byte[][] levelScene) {
//        if (CheckEnemiesInSpecifiedArea(enemyScene, 3, 3, -2, 5)) {
//
//        }
        for (int i = mMarioX + 3; i >= mMarioX - 3; i--) {
            for (int j = mMarioY - 2; j >= mMarioY - 5; j--) {
                if (IsEnemy(i, j, enemyScene)) {
                    for (int k = j + 1; k <= mMarioY; k++) {
                        if (levelScene[k][i] == 0) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
