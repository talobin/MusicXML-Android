package uk.co.dolphin_com.rscore;

public class BarInfo {
    private static final int BEYOND_SCORES = 1;
    private static final int EOSCORE = 32;
    private static final int FIRST_OF_SYSTEM = 2;
    private static final int INVISIBLE = 4;
    private static final int YERROR = 16;
    public final int flags;
    public final BarlinePos leftBarline;
    public final int numBeats;
    public final BarlinePos rightBarline;
    public final int startBeat;

    public boolean beyondScores() {
        return (this.flags & 1) != 0;
    }

    public boolean isLeftmost() {
        return (this.flags & 2) != 0;
    }

    public boolean isInvisible() {
        return (this.flags & 4) != 0;
    }

    public boolean isYError() {
        return (this.flags & 16) != 0;
    }

    public boolean isBeyondLastInScore() {
        return (this.flags & 32) != 0;
    }

    BarInfo(BarlinePos leftBarline2, BarlinePos rightBarline2, int startBeat2, int numBeats2, int flags2) {
        this.leftBarline = leftBarline2;
        this.rightBarline = rightBarline2;
        this.startBeat = startBeat2;
        this.numBeats = numBeats2;
        this.flags = flags2;
    }
}
