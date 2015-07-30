package nghia.ndict.entities;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 1/13/13
 * Time: 11:30 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class HashItem extends BaseEntity {
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
