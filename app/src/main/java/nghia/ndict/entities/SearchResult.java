package nghia.ndict.entities;

import java.util.ArrayList;

import nghia.ndict.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 4/18/13
 * Time: 3:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class SearchResult extends BaseEntity {
    private ArrayList<Word> wordsList;
    private String key;

    public int binarySearchWordIndex(int left, int right) {
        if (left > right || Utils.isNullOrEmpty(key)) {
            return 0;
        }
        int middle = (left + right) / 2;
        String word = wordsList.get(middle).getContent();
        String word2 = "";
        if (word.length() > key.length()) {
            word2 = new String(word.substring(0, key.length()));
        }
        int cmp = Utils.compareTwoStrings(key, word2);
        if (cmp == -1) {
            return binarySearchWordIndex(left, middle - 1);
        } else if (cmp == 0) {
            return middle;
        } else {
            return binarySearchWordIndex(middle + 1, right);
        }
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int count() {
        return wordsList == null ? 0 : wordsList.size();
    }

    public void appendWords(ArrayList<Word> words) {
        if (wordsList == null) {
            wordsList = new ArrayList<Word>();
        }
        wordsList.addAll(words);

    }

    public ArrayList<Word> getWordsList() {
        return wordsList;
    }

    public void setWordsList(ArrayList<Word> wordsList) {
        this.wordsList = wordsList;
    }

    public int getMatchIndex() {
        return wordsList == null ? 0 : binarySearchWordIndex(0, wordsList.size() - 1);
    }
}
