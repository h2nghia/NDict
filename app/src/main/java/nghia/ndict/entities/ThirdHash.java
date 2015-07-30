package nghia.ndict.entities;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 1/13/13
 * Time: 10:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class ThirdHash extends HashItem {
    private int startIdx;
    private int endIdx;

    public ThirdHash(String key) {
        this.setKey(key);
    }

    public int getStartIdx() {
        return startIdx;
    }

    public void setStartIdx(int startIdx) {
        this.startIdx = startIdx;
    }

    public int getEndIdx() {
        return endIdx;
    }

    public void setEndIdx(int endIdx) {
        this.endIdx = endIdx;
    }
}
