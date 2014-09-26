package competition.cig.lu_yufan;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-13
 * Time: 上午12:27
 * To change this template use File | Settings | File Templates.
 */
public class TimingButton {
    // Call Start... to start pressing the button in a period of time
    // The button will be no longer out of control after calling Start functions
    // except after calling Release or Initialize again

    // ------------- Variables -------------

    private boolean mIsOn;
    private boolean mIsStart;
    private boolean mIsReleased;

    private int mTargetTimeCount;
    private int mDelayTimeCount;
    private int mInternalCounter;
    private int mIntermittentCount;

    private eButtonType mType;

//    public enum eTimePeriod {
//        eInfinite,              // 0xffff
//        eShorterTimePeriod,     // Immediate, 0
//        eShortTimePeriod,       // 1
//        eMediumTimePeriod,      // 3
//        eLongTimePeriod,        // 5
//        eLongerTimePeriod,      // 10
//    }

    /*
    *            |<-------------------mIsStart-------------------->
    *                       |<---mIsOn--->|
    *                                     |<----mIsReleased------->
    * eStandard: |--Delay-->|-----On----->|---------Off----------->
    * eRepeated: |--Delay-->|-----On----->|---Intermittent--->|
    *            ^---------------------<-------------------<--|
    * */
    public enum eButtonType {
        eStandard,
        eRepeated,
    }

    // ------------- Operation Functions -------------

//    public void SetRepeatedButton(eTimePeriod intermittentPeriod) {
//        SetRepeatedButton(TranslateTimePeriod(intermittentPeriod));
//    }

    public void SetRepeatedButton(int intermittent) {
        mType = eButtonType.eRepeated;
        mIntermittentCount = intermittent;
    }

    public void Initialize() {
        mIsOn = false;
        mIsStart = false;
        mIsReleased = false;
        mInternalCounter = 0;
        mTargetTimeCount = 0;
        mDelayTimeCount = 0;
        mType = eButtonType.eStandard;
    }

//    public void StartImmediately(eTimePeriod timePeriod) {
//        Start(TranslateTimePeriod(timePeriod), 0);
//    }

    public void StartImmediately(int timeCount) {
        Start(timeCount, 0);
    }

//    public void Start(eTimePeriod timePeriod, eTimePeriod timePeriodDelay) {
//        Start(TranslateTimePeriod(timePeriod), TranslateTimePeriod(timePeriodDelay));
//    }

//    public void Start(eTimePeriod timePeriod, int timeDelay) {
//        Start(TranslateTimePeriod(timePeriod), timeDelay);
//    }

    public void Start(int timeCount, int timeDelay) {
        if (mIsStart) {
            return;
        }
        if (timeCount < 0 || timeDelay < 0) {
            return;
        }
        mTargetTimeCount = timeCount;
        mDelayTimeCount = timeDelay;
        mIsStart = true;
    }

    public void Update() {
        if (mType == eButtonType.eStandard) {
            StandardUpdate();
        } else {
            RepeatedUpdate();
        }
    }

    private void RepeatedUpdate() {
        if (!mIsStart) {
            return;
        }

        if (mInternalCounter > mDelayTimeCount) {
            mIsOn = true;
        }

        if (mInternalCounter > mTargetTimeCount + mDelayTimeCount) {
            mIsOn = false;
        }
        if (mInternalCounter > mTargetTimeCount + mDelayTimeCount + mIntermittentCount) {
            // Repeat!
            mInternalCounter = 0;
            mDelayTimeCount = 0;
        }

        mInternalCounter++;
    }

    private void StandardUpdate() {
        if (!mIsStart) {
            return;
        }

        if (mInternalCounter > mDelayTimeCount) {
            mIsOn = true;
        }

        if (mInternalCounter > mTargetTimeCount + mDelayTimeCount) {
            // Release it
            mIsOn = false;
            mIsReleased = true;
            mInternalCounter = 0;
            mTargetTimeCount = 0;
            mDelayTimeCount = 0;
            return;
        }

        mInternalCounter++;
    }

    public void Release() {
        Initialize();
        mIsReleased = true;
    }
    
    public boolean IsReleased() {
        return mIsReleased;
    }

    public boolean IsButtonOn() {
        return mIsOn;
    }

    public boolean HasStarted() {
        return mIsStart;
    }
    
//    private int TranslateTimePeriod(eTimePeriod timePeriod) {
//        switch (timePeriod)
//        {
//            case eInfinite:
//                return 0xffff;
//            case eShorterTimePeriod:
//                return 0;
//            case eShortTimePeriod:
//                return 1;
//            case eMediumTimePeriod:
//                return 3;
//            case eLongTimePeriod:
//                return 5;
//            case eLongerTimePeriod:
//                return 10;
//        }
//        return -1;
//    }
}
