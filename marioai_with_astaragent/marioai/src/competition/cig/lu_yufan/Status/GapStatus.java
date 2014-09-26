package competition.cig.lu_yufan.Status;

import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import competition.cig.lu_yufan.TimingButton;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-13
 * Time: 下午4:32
 * To change this template use File | Settings | File Templates.
 */
public class GapStatus extends StatusBase {
    private static GapStatus ourInstance = new GapStatus();

    public static GapStatus getInstance() {
        return ourInstance;
    }

    // ------------- Variables -------------
    
    private int mJumpPressedTime;
    private int mJumpPressedDelay;

    // ------------- Constructor/Destructor -------------

    private GapStatus() {
    }

    // ------------- Override Functions -------------

    @Override
    public boolean[] GetAction(byte[][] levelScene, byte[][] enemyScene, Environment env) {
        mButtons[Mario.KEY_JUMP].Start(mJumpPressedTime, mJumpPressedDelay);
        mButtons[Mario.KEY_RIGHT].StartImmediately(0xffff);
        mButtons[Mario.KEY_SPEED].StartImmediately(0xffff);

//        mButtons[Mario.KEY_JUMP].Start(mJumpPressedTime, 30);
//        mButtons[Mario.KEY_RIGHT].Start(0xffff, 30);
//        mButtons[Mario.KEY_SPEED].Start(0xffff, 30);

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
    }

    @Override
    public boolean IsChangedToThisStatus(byte[][] levelScene, byte[][] enemyScene, Environment env) {
//        if (GapExistTest(levelScene, 2)) {
//            int width = GapWidthTest(levelScene, 2);
//            mJumpPressedDelay = 1;
//            mJumpPressedTime = width + 1;
//            return true;
//        }

        if (GapExistTest(levelScene, 1)) {
            int width = GapWidthTest(levelScene, 1);
            mJumpPressedDelay = 0;
            mJumpPressedTime = width;
//            OutputScene(levelScene);
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

    private boolean GapExistTest(byte[][] levelScene, int offsetX) {
        for (int i = mMarioY; i < 22; i++) {
            if (levelScene[i][mMarioX + offsetX] != 0) {
                return false;
            }
        }
        return true;
    }
    
    private int GapWidthTest(byte[][] levelScene, int startOffsetX) {
        int cnt = 0;
        for (int i = mMarioX + startOffsetX; i < 22; i++) {
            boolean flag = false;
            for (int j = mMarioY; j < 22; j++) {
                if (levelScene[j][i] != 0) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                break;
            }
            cnt++;
        }
        return cnt;
    }

}
