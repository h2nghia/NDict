package nghia.ndict.entities;

import java.util.ArrayList;
import java.util.List;

import nghia.ndict.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 1/13/13
 * Time: 11:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class FirstHash extends HashItem {
    private List<SecondHash> items;

    public FirstHash(String key) {
        this.setKey(key);
        items = new ArrayList<SecondHash>();
    }

    public List<SecondHash> getItems() {
        return items;
    }

    public void setItems(List<SecondHash> items) {
        this.items = items;
    }

    public int binarySearchSecondHash(String key, int start, int end) {
        if (end < start || Utils.isNullOrEmpty(key)) {
            return 0;
        }
        int middle = (end + start) / 2;
        String median = items.get(middle).getKey();
        int cmp = Utils.compareTwoStrings(key, median);
        if (cmp == 0) {
            return middle;
        } else if (cmp == 1) {
            return binarySearchSecondHash(key, middle + 1, end);
        } else {
            return binarySearchSecondHash(key, start, middle - 1);
        }
    }

    public int getLastHash2Index() {
        return getItems().size() - 1;
    }

    public int getLastHash3Index() {
        SecondHash secondHash = items.get(items.size() - 1);
        if (secondHash != null) {
            return secondHash.getItems().size() - 1;
        }
        return -1;
    }
}
