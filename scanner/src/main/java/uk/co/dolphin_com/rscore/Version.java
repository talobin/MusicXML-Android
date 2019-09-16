package uk.co.dolphin_com.rscore;

import java.text.NumberFormat;

public class Version {

    private final int hi;
    private final int lo;

    public String toString() {
        NumberFormat nf = NumberFormat.getIntegerInstance();
        nf.setMinimumIntegerDigits(2);
        return hi + "." + nf.format((long) lo);
    }

    private Version(int hi, int lo) {
        this.hi = hi;
        this.lo = lo;
    }
}
