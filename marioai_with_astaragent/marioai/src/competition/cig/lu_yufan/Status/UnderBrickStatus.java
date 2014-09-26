package competition.cig.lu_yufan.Status;

import ch.idsia.mario.engine.sprites.Mario;
import ch.idsia.mario.environments.Environment;
import competition.cig.lu_yufan.TimingButton;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-13
 * Time: 下午10:28
 * To change this template use File | Settings | File Templates.
 */
public class UnderBrickStatus extends StatusBase {
    private static UnderBrickStatus ourInstance = new UnderBrickStatus();

    public static UnderBrickStatus getInstance() {
        return ourInstance;
    }

    // ------------- Variables -------------

    // first check the Y direction, then X
    private final int mCheckingOffsetX = 1;
    private final int mCheckingOffsetY = 3;
    private int mJumpPressedTime;

    // ------------- Constructor/Destructor -------------

    private UnderBrickStatus() {
    }

    // ------------- Override Functions -------------

    @Override
    public boolean[] GetAction(byte[][] levelScene, byte[][] enemyScene, Environment env) {
        mButtons[Mario.KEY_JUMP].StartImmediately(mJumpPressedTime);
        mButtons[Mario.KEY_RIGHT].StartImmediately(0xffff);

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
        boolean flag = false;
        for (int i = mMarioX; i <= mMarioX + mCheckingOffsetX; i++) {
            for (int j = mMarioY; j >= mMarioY - mCheckingOffsetY; j--) {
                if (levelScene[j][i] == -11 || levelScene[j][i] == 16) {
                    mJumpPressedTime = (mMarioY - j) * 2;
                    flag = true;
                    break;
                }
            }
        }
        return (flag && (mRnd.nextInt() % 3 == 0));
    }

    @Override
    public boolean IsLeavingThisStatus(byte[][] levelScene, byte[][] enemyScene, Environment env) {
        if (mButtons[Mario.KEY_JUMP].IsReleased() && env.isMarioOnGround()) {
            return true;
        }
        return false;
    }
}
