package competition.cig.lu_yufan.Status;

import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.engine.sprites.Sprite;
import ch.idsia.mario.environments.Environment;
import competition.cig.lu_yufan.TimingButton;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-13
 * Time: 下午2:29
 * To change this template use File | Settings | File Templates.
 */
public class ObstacleStatus extends StatusBase {
    private static ObstacleStatus ourInstance = new ObstacleStatus();

    public static ObstacleStatus getInstance() {
        return ourInstance;
    }

    // ------------- Variables -------------

    private int mPressJumpTime;
    private int mPressJumpDelay;
    private boolean mShoot;
    private int mShootDelay;

    // ------------- Constructor/Destructor -------------

    private ObstacleStatus() {
    }

    // ------------- Override Functions -------------

    @Override
    public boolean[] GetAction(byte[][] levelScene, byte[][] enemyScene, Environment env) {
        mButtons[Mario.KEY_RIGHT].StartImmediately(0xffff);
        mButtons[Mario.KEY_JUMP].Start(mPressJumpTime, mPressJumpDelay);

        if (mShoot) {
            mButtons[Mario.KEY_SPEED].SetRepeatedButton(1);
            mButtons[Mario.KEY_SPEED].Start(1, mShootDelay);
        }

//        OutputScene(enemyScene);
        if (EnemiesAround(levelScene, enemyScene, 1, 4)) {
//            OutputScene(enemyScene);
            mButtons[Mario.KEY_LEFT].StartImmediately(0xffff);
            mButtons[Mario.KEY_SPEED].SetRepeatedButton(1);
            mButtons[Mario.KEY_SPEED].Start(1, mShootDelay);
        } else {
            mButtons[Mario.KEY_LEFT].Release();
        }
        
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
        if (mRnd.nextInt() % 3 == 0 && levelScene[mMarioY][mMarioX + 2] != 0) {
            if (levelScene[mMarioY][mMarioX + 1] == 20) {
                // this is an angry enemy flower pot or parts of a cannon
                mShoot = true;
                mShootDelay = DecideTimePeriod(levelScene, false);
            } else {
                mShoot = false;
            }
            if (EnemiesAround(levelScene, enemyScene, 2, 4)) {
                mShoot = true;
                mShootDelay = DecideTimePeriod(levelScene, false);
            }

            mPressJumpDelay = 0;
            mPressJumpTime = DecideTimePeriod(levelScene, false);

            return true;
        }

        if (levelScene[mMarioY][mMarioX + 1] != 0) {
            if (levelScene[mMarioY][mMarioX + 1] == 20) {
                // this is an angry enemy flower pot or parts of a cannon
                mShoot = true;
                mShootDelay = DecideTimePeriod(levelScene, true);
            } else {
                mShoot = false;
            }
            if (EnemiesAround(levelScene, enemyScene, 1, 4)) {
//                OutputScene(enemyScene);
                mShoot = true;
                mShootDelay = DecideTimePeriod(levelScene, false);
            }

            mPressJumpDelay = 0;
            mPressJumpTime = DecideTimePeriod(levelScene, true);
            return true;
        }

        return false;
    }

    @Override
    public boolean IsLeavingThisStatus(byte[][] levelScene, byte[][] enemyScene, Environment env) {
        if (mButtons[Mario.KEY_JUMP].IsReleased() && env.isMarioOnGround()) {
            return true;
        }
        return false;
    }

    // ------------- Utility Functions -------------

    public int DecideTimePeriod(byte[][] levelScene, boolean closeDist) {
        if (levelScene[mMarioY - 4][mMarioX + 1] != 0) {
            return (closeDist ? 15 : 16);
        }
        if (levelScene[mMarioY - 3][mMarioX + 1] != 0) {
            return (closeDist ? 10 : 11);
        }
        if (levelScene[mMarioY - 2][mMarioX + 1] != 0) {
            return (closeDist ? 6 : 7);
        }
        if (levelScene[mMarioY - 1][mMarioX + 1] != 0) {
            return 3;
        }
        return 1;
    }

    public boolean EnemiesAround(byte[][] levelScene, byte[][] enemyScene, int offsetX, int offsetY) {
        for (int i = mMarioX - offsetX; i <= mMarioX + offsetX; i++) {
            for (int j = mMarioY - offsetY; j <= mMarioY + offsetY; j++) {
                if (j > 0 && enemyScene[j][i] == 9 && levelScene[j - 1][i] != 0) {
                    continue;
                }
                if (enemyScene[j][i] != 0 && enemyScene[j][i] != Sprite.KIND_FIREBALL) {
                    return true;
                }
            }
        }
        return false;
    }
}
