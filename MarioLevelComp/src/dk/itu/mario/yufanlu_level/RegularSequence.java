package dk.itu.mario.yufanlu_level;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: bilibili
 * Date: 13-10-29
 * Time: 上午11:55
 * To change this template use File | Settings | File Templates.
 */
public class RegularSequence {
    public class RegularNode {
        /*
        * Example:
        * mCandidateTerrain:
        *   -- "ab"
        *   -- "bcd"
        *   -- "bd"
        * mRepeatTimes:
        *   -- 5
        * This means to choose one of the terrain in the three candidates repeatedly
        * for 5 times
        * */
        public ArrayList<String> mCandidateTerrain = new ArrayList<String>();
        public int mRepeatTimes = 0;
        
        public void clear() {
            mCandidateTerrain.clear();
            mRepeatTimes = 0;
        }
    }

    private String mPattern;
    private Random mRnd = new Random();

    public ArrayList<RegularNode> getTerrain() {
        return mTerrain;
    }

    private ArrayList<RegularNode> mTerrain = new ArrayList<RegularNode>();

    /*
    * pat instruction:
    *   abcd:   terrain type
    *   (:      paralleling
    *       |:      or
    *       abcd:   terrain type
    *       ):
    *           *:  repeated (rand() % 5) times
    *           +:  repeated (rand() % 4 + 1) times
    * ATTN: ***nested parenthesis will be IGNORED***
    * ATTN: ***ONLY "abcd" four characters are allowed***
    * */
    public RegularSequence(String pat) {
        this.mPattern = pat;
    }

    public ArrayList<RegularNode> ParseExpression() {
        mTerrain.clear();
        for (int i = 0; i < mPattern.length(); i++) {
            switch (mPattern.charAt(i))
            {
                case '(':
                {
                    String str = "";
                    RegularNode current = new RegularNode();
                    for (int j = i + 1; j < mPattern.length(); j++) {
                        boolean overFlag = false;
                        int endingIndex = 0;
                        switch (mPattern.charAt(j))
                        {
                            case ')':
                            {
                                if (!str.equals("")) {
                                    current.mCandidateTerrain.add(str);
                                    str = "";
                                }
                                if (j + 1 < mPattern.length() && mPattern.charAt(j + 1) == '*') {
                                    current.mRepeatTimes = mRnd.nextInt(5);
                                    endingIndex = j + 1;
                                } else if (j + 1 < mPattern.length() && mPattern.charAt(j + 1) == '+') {
                                    current.mRepeatTimes = mRnd.nextInt(4) + 1;
                                    endingIndex = j + 1;
                                } else {
                                    current.mRepeatTimes = 1;
                                    endingIndex = j;
                                }
                                if (current.mCandidateTerrain.size() != 0) {
                                    mTerrain.add(current);
                                }
                                overFlag = true;
                            } break;

                            case '|':
                            {
                                if (!str.equals("")) {
                                    current.mCandidateTerrain.add(str);
                                    str = "";
                                }
                            } break;

                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            {
                                str += mPattern.charAt(j);
                            } break;
                        }
                        if (overFlag) {
                            i = endingIndex;
                            break;
                        }
                    }
                } break;

                case 'a':
                case 'b':
                case 'c':
                case 'd':
                {
                    RegularNode current = new RegularNode();
                    String tmp = "" + mPattern.charAt(i);
                    current.mRepeatTimes = 1;
                    current.mCandidateTerrain.add(tmp);
                    mTerrain.add(current);
                } break;

                default:
                    // do nothing
                    break;
            }
        }

        return mTerrain;
    }

    public void DebugOutput() {
        for (RegularNode rn : this.getTerrain()) {
            System.out.println("Times: " + rn.mRepeatTimes);
            for (String s : rn.mCandidateTerrain) {
                System.out.println("Candidate: " + s);
            }
        }
    }

    public static void main(String[] args) {
        RegularSequence rs;
//        RegularSequence rs = new RegularSequence("abcd(bd|cd)+a");
//        RegularSequence rs = new RegularSequence("(bddd|)+a");

        // Functional tests:
//        rs = new RegularSequence("(bddd|)+a");
//        rs = new RegularSequence("abcd(bd|cd)+ab");
//        rs = new RegularSequence("abcd(bd|cd)+(ab|addc)+");
        rs = new RegularSequence("aaaaa(ab)");
        // Edge & Error tests:
//        rs = new RegularSequence("");
//        rs = new RegularSequence("a");
//        rs = new RegularSequence("a()+");
//        rs = new RegularSequence("()");
//        rs = new RegularSequence("(ab)");
//        rs = new RegularSequence("a|b");
//        rs = new RegularSequence("(a|b");
//        rs = new RegularSequence("(a|b)");
//        rs = new RegularSequence("(a||b)");
//        rs = new RegularSequence("abc");
//        rs = new RegularSequence("*");
        // Stress tests:
//        rs = new RegularSequence("abcda(ab|a|bb|da|adadbada|adab|aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa)+");

        rs.ParseExpression();
        rs.DebugOutput();
        
        for (int i = 0; i < 10; i++) {
            Random rnd = new Random();
            System.out.println(rnd.nextInt(1));
        }
    }
}
