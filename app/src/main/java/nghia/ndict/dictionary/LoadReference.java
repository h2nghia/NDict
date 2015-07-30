package nghia.ndict.dictionary;

import java.util.ArrayList;

import nghia.ndict.entities.ThirdHash;
import nghia.ndict.entities.Word;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 4/16/13
 * Time: 2:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class LoadReference {
    private ArrayList<Word> words = new ArrayList<Word>();
    private int startWordLoad;
    private int endWordLoad;
    private ThirdHash startThirdHash;
    private ThirdHash endThirdHash;


    public ArrayList<Word> getWords() {
        return words;
    }

    public void setWords(ArrayList<Word> words) {
        this.words = words;
    }

    public int getStartWordLoad() {
        return startWordLoad;
    }

    public void setStartWordLoad(int startWordLoad) {
        this.startWordLoad = startWordLoad;
    }

    public int getEndWordLoad() {
        return endWordLoad;
    }

    public void setEndWordLoad(int endWordLoad) {
        this.endWordLoad = endWordLoad;
    }

    public ThirdHash getStartThirdHash() {
        return startThirdHash;
    }

    public void setStartThirdHash(ThirdHash startThirdHash) {
        this.startThirdHash = startThirdHash;
    }

    public ThirdHash getEndThirdHash() {
        return endThirdHash;
    }

    public void setEndThirdHash(ThirdHash endThirdHash) {
        this.endThirdHash = endThirdHash;
    }
}
