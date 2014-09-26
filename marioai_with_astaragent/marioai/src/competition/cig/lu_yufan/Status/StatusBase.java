package competition.cig.lu_yufan.Status;

import ch.idsia.mario.engine.sprites.Sprite;
import ch.idsia.mario.environments.Environment;
import competition.cig.lu_yufan.TimingButton;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-13
 * Time: 上午12:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class StatusBase {

    // ------------- Variables -------------

    protected boolean[] mAction;
    protected TimingButton[] mButtons;
    private boolean mExitFlag = false;

    protected static int mMarioX = 11;
    protected static int mMarioY = 11;

    protected final Random mRnd = new Random();

    // ------------- Pure Virtual Functions -------------

    public abstract boolean[] GetAction(byte[][] levelScene, byte[][] enemyScene, Environment env);
    public abstract void EnterStatus(boolean[] action, TimingButton[] buttons);
    public abstract void LeaveStatus();
    public abstract boolean IsChangedToThisStatus(byte[][] levelScene, byte[][] enemyScene, Environment env);
    public abstract boolean IsLeavingThisStatus(byte[][] levelScene, byte[][] enemyScene, Environment env);

    // ------------- Base Utility Functions -------------

    protected void Initialize(boolean[] action, TimingButton[] buttons) {
        mAction = action;
        mButtons = buttons;
        mExitFlag = false;
        for (int i = 0; i < mAction.length; i++) {
            mAction[i] = false;
        }
        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i].Initialize();
        }
    }

    public boolean IsFinished() {
        return mExitFlag;
    }

    protected void DoExit() {
        mExitFlag = true;
    }

    protected void OutputAction() {
        for (boolean b : mAction) {
            System.out.print(b + " ");
        }
        System.out.println("");
    }

    public static void OutputScene(byte[][] scene) {
        for (int i = 0; i < scene.length; i++) {
            for (int j = 0; j < scene[i].length; j++) {
                if (i == mMarioX && j == mMarioY) {
                    System.out.printf("  M ");
                } else {
                    System.out.printf("%3d ", scene[i][j]);
                }
            }
            System.out.println("");
        }
    }

    protected void FinalizeActions() {
        for (int i = 0; i < mButtons.length; i++) {
            mAction[i] = mButtons[i].IsButtonOn();
        }
    }

    protected void ClearActionMark() {
        for (int i = 0; i < mAction.length; i++) {
            mAction[i] = false;
            mButtons[i].Initialize();
        }
    }

    protected boolean IsEnemy(int x, int y, byte[][] enemyScene) {
        return (enemyScene[y][x] != 0 && enemyScene[y][x] != Sprite.KIND_FIREBALL && enemyScene[y][x] != 9);
    }

    protected boolean IsAngryFlower(int x, int y, byte[][] enemyScene) {
        return (enemyScene[y][x] == 9);
    }

    /*
   * |<--------------^-------------->|
   * |<--------------|-------------->|
   * |<----------rangeNegY---------->|
   * |<--------------|-------------->|
   * |<--rangeNegX---M---rangePosX-->|
   * |<--------------|-------------->|
   * |<----------rangePosY---------->|
   * |<--------------|-------------->|
   * |<--------------v-------------->|
   * */
    protected boolean CheckEnemiesInSpecifiedArea(byte[][] enemyScene, int rangePosX, int rangeNegX, int rangePosY, int rangeNegY) {
        return CheckEnemiesInSpecifiedArea(enemyScene, rangePosX, rangeNegX, rangePosY, rangeNegY, false);
    }

    protected boolean CheckEnemiesInSpecifiedArea(byte[][] enemyScene, int rangePosX, int rangeNegX, int rangePosY, int rangeNegY, boolean considerAngryFlower) {
//        assert(rangePosX >= 0 && rangeNegX >= 0);
//        assert(rangePosY >= 0 && rangeNegY >= 0);
        for (int i = mMarioX + rangePosX; i >= mMarioX - rangeNegX; i--) {
            for (int j = mMarioY + rangePosY; j >= mMarioY - rangeNegY; j--) {
                if (considerAngryFlower) {
                    if (IsEnemy(i, j, enemyScene) || IsAngryFlower(i, j, enemyScene)) {
                        return true;
                    }
                } else {
                    if (IsEnemy(i, j, enemyScene)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}