package nghia.ndict.entities;

import java.util.ArrayList;
import java.util.List;

import nghia.ndict.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 1/13/13
 * Time: 11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class SecondHash extends HashItem {

    private List<ThirdHash> items;

    public SecondHash(String key) {
        this.setKey(key);
        items = new ArrayList<ThirdHash>();
    }

    public List<ThirdHash> getItems() {
        return items;
    }

    public void setItems(List<ThirdHash> items) {
        this.items = items;
    }

    public int binarySearchThirdHash(String key, int start, int end) {
        if (end < start || Utils.isNullOrEmpty(key)) {
            return 0;
        }
        int middle = (end + start) / 2;
        String median = items.get(middle).getKey();
        int cmp = Utils.compareTwoStrings(key, median);
        if (cmp == 0) {
            return middle;
        } else if (cmp == 1) {
            return binarySearchThirdHash(key, middle + 1, end);
        } else {
            return binarySearchThirdHash(key, start, middle - 1);
        }
    }

    int getLastHash3Index() {
        return items.size() - 1;
    }
}
