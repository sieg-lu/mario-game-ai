package competition.cig.lu_yufan;

import ch.idsia.ai.agents.Agent;
import ch.idsia.mario.environments.Environment;
import competition.cig.lu_yufan.Status.*;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-13
 * Time: 上午12:10
 * To change this template use File | Settings | File Templates.
 */
public class YufanLu_NarukamiAI implements Agent {

    // ------------- Variables -------------

    private String mName;
    private boolean[] mAction;
    private TimingButton[] mButtons;
    
    private StatusBase mCurrentStatus;

    // ------------- Constructor/Destructor -------------

    public YufanLu_NarukamiAI() {
        setName("Narukami_AI");
        reset();
    }

    // ------------- Override Functions -------------

    public boolean[] getAction(Environment observation) {
        byte[][] levelScene = observation.getLevelSceneObservationZ(1);
        byte[][] enemyScene = observation.getEnemiesObservationZ(1);

        // Status Switch Test
        /*
        * Priority: (Descending)
        *   -- GapStatus
        *   -- ObstacleStatus
        *   -- EnemyInsightStatus
        *   -- UnderBrickStatus
        *   -- NormalStatus
        * */
        if (mCurrentStatus == NormalStatus.getInstance() || mCurrentStatus.IsFinished()) {
            if (mCurrentStatus != GapStatus.getInstance() && GapStatus.getInstance().IsChangedToThisStatus(levelScene, enemyScene, observation)) {
                mCurrentStatus.LeaveStatus();
                mCurrentStatus = GapStatus.getInstance();
                mCurrentStatus.EnterStatus(mAction, mButtons);
//                StatusBase.OutputScene(levelScene);
                System.out.println("Change to GapStatus");
                return mAction;
            } else if (mCurrentStatus != ObstacleStatus.getInstance() && ObstacleStatus.getInstance().IsChangedToThisStatus(levelScene, enemyScene, observation)) {
                mCurrentStatus.LeaveStatus();
                mCurrentStatus = ObstacleStatus.getInstance();
                mCurrentStatus.EnterStatus(mAction, mButtons);
//                StatusBase.OutputScene(levelScene);
                System.out.println("Change to ObstacleStatus");
                return mAction;
            }  else if (mCurrentStatus != EnemiesInsightStatus.getInstance() && EnemiesInsightStatus.getInstance().IsChangedToThisStatus(levelScene, enemyScene, observation)) {
                mCurrentStatus.LeaveStatus();
                mCurrentStatus = EnemiesInsightStatus.getInstance();
                mCurrentStatus.EnterStatus(mAction, mButtons);
//                StatusBase.OutputScene(levelScene);
                System.out.println("Change to EnemiesInsightStatus");
                return mAction;
            } else if (mCurrentStatus != UnderBrickStatus.getInstance() && UnderBrickStatus.getInstance().IsChangedToThisStatus(levelScene, enemyScene, observation)) {
                mCurrentStatus.LeaveStatus();
                mCurrentStatus = UnderBrickStatus.getInstance();
                mCurrentStatus.EnterStatus(mAction, mButtons);
//                StatusBase.OutputScene(levelScene);
                System.out.println("Change to UnderBrickStatus");
                return mAction;
            }
        }

        // Status Exit Test
        if (mCurrentStatus.IsLeavingThisStatus(levelScene, enemyScene, observation) || mCurrentStatus.IsFinished()) {
            mCurrentStatus.LeaveStatus();
            mCurrentStatus = NormalStatus.getInstance();
            mCurrentStatus.EnterStatus(mAction, mButtons);
            System.out.println("Change to NormalStatus");
            return mAction;
        }

        return mCurrentStatus.GetAction(levelScene, enemyScene, observation);
    }

    public void reset() {
        mAction = new boolean[Environment.numberOfButtons];
        mButtons = new TimingButton[Environment.numberOfButtons];
        for (int i = 0; i < mButtons.length; i++) {
            mButtons[i] = new TimingButton();
        }
//        mCurrentStatus = EnemiesInsightStatus.getInstance();
        mCurrentStatus = NormalStatus.getInstance();
        mCurrentStatus.EnterStatus(mAction, mButtons);
    }

    public AGENT_TYPE getType() {
        return AGENT_TYPE.AI;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    // ------------- Utility Functions -------------

}
