package uk.co.dolphin_com.rscore;

public class ConvertOptions {
    public boolean exclSmallUppers;
    public boolean exclUpper;
    public String software;

    public ConvertOptions(String sw, boolean excludeUpperStaff, boolean excludeSmallUpperStaffs) {
        this.software = sw;
        this.exclUpper = excludeUpperStaff;
        this.exclSmallUppers = excludeSmallUpperStaffs;
    }
}
