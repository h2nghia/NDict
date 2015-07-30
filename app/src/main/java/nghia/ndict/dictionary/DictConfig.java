package nghia.ndict.dictionary;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 4/16/13
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public class DictConfig {
    private String dictName;
    private int totalWords;
    private int endWordIndex;

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public int getEndWordIndex() {
        return endWordIndex;
    }

    public void setEndWordIndex(int endWordIndex) {
        this.endWordIndex = endWordIndex;
    }

    public String getDictName() {
        return dictName;
    }

    public void setDictName(String dictName) {
        this.dictName = dictName;
    }
}
