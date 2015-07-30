package nghia.ndict.entities;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 4/16/13
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class HashIndex extends BaseEntity {
    public int firstIndex = 0;
    public int secondIndex = 0;
    public int thirdIndex = 0;

    public HashIndex() {
    }

    public HashIndex(int firstIndex, int secondIndex, int thirdIndex) {
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
        this.thirdIndex = thirdIndex;
    }

    public boolean isFirstHash() {
        if (firstIndex == 0 && secondIndex == 0 && thirdIndex == 0) {
            return true;
        }
        return false;
    }
}
