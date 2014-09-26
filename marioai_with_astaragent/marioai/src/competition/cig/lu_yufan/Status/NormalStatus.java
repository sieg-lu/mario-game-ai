package competition.cig.lu_yufan.Status;

import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import competition.cig.lu_yufan.TimingButton;

import java.awt.*;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-13
 * Time: 上午12:41
 * To change this template use File | Settings | File Templates.
 */
public class NormalStatus extends StatusBase {
    private static NormalStatus ourInstance = new NormalStatus();

    public static NormalStatus getInstance() {
        return ourInstance;
    }

    // ------------- Variables -------------
    
    private final int mGapTestRange = 5;

    // ------------- Constructor/Destructor -------------

    private NormalStatus() {
    }

    // ------------- Override Functions -------------

    @Override
    public boolean[] GetAction(byte[][] levelScene, byte[][] enemyScene, Environment env) {
//        mButtons[Mario.KEY_JUMP].SetRepeatedButton(1);
//        mButtons[Mario.KEY_JUMP].Start(5, 30);

        mButtons[Mario.KEY_RIGHT].StartImmediately(0xffff);
        if (env.isMarioOnGround()) {
            mButtons[Mario.KEY_SPEED].SetRepeatedButton(5);
            mButtons[Mario.KEY_SPEED].Start(5, 10 + mRnd.nextInt(10));
        }

        if (FallingTest(levelScene, 1) >= 1 && GapExistTest(levelScene)) {
//            OutputScene(levelScene);
            mButtons[Mario.KEY_SPEED].Release();
            mButtons[Mario.KEY_LEFT].StartImmediately(15);
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
//        mButtons[Mario.KEY_RIGHT].Start(10, 30);
    }

    @Override
    public void LeaveStatus() {
        ClearActionMark();
    }

    @Override
    public boolean IsChangedToThisStatus(byte[][] levelScene, byte[][] enemyScene, Environment env) {
        // default status, no need to shift others to here
        return false;
    }

    @Override
    public boolean IsLeavingThisStatus(byte[][] levelScene, byte[][] enemyScene, Environment env) {
        // default status, no need to leave
        return false;
    }

    // ------------- Utility Functions -------------

    private int FallingTest(byte[][] levelScene, int offsetX) {
//        return (levelScene[mMarioY][mMarioX + offsetX] == 0);
        int cnt = 0;
        for (int i = mMarioY; i < 22; i++) {
            if (levelScene[i][mMarioX + offsetX] == 0) {
                cnt++;
            } else {
                break;
            }
        }
        return cnt;
    }

    private boolean GapExistTest(byte[][] levelScene) {
        for (int i = mMarioX; i < mMarioX + mGapTestRange; i++) {
            boolean flag = false;
            for (int j = mMarioY; j < 22; j++) {
                if (levelScene[j][i] != 0) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                return true;
            }
        }
        return false;
    }
}
