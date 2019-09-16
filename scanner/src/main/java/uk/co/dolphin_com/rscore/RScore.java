package uk.co.dolphin_com.rscore;

import java.io.File;
import uk.co.dolphin_com.rscore.ex.RScoreException;

public class RScore {
    private final long nativePointer;

    public enum Orientation {
        ROT0, ROT90, ROT180, ROT270
    }

    public static native RScore Convert(int[] pixel,
                                        int bitmapWidth,
                                        int bitmapHeight,
                                        int width,
                                        Orientation orientation,
                                        ConvertCallback convertCallback,
                                        File midiFile,
                                        File xmlFile,
                                        ConvertOptions convertOptions) throws RScoreException;

    public static native Version getVersion();

    public native BarInfo getBarInfo(int i);

    public native int getNumBars();

    private RScore(long nativePointer) {
        this.nativePointer = nativePointer;
    }
}
