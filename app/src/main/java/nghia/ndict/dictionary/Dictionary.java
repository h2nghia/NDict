package nghia.ndict.dictionary;


import java.io.IOException;
import java.util.ArrayList;

import nghia.ndict.entities.BaseEntity;
import nghia.ndict.entities.FirstHash;
import nghia.ndict.entities.HashIndex;
import nghia.ndict.entities.SearchResult;
import nghia.ndict.entities.SecondHash;
import nghia.ndict.entities.ThirdHash;
import nghia.ndict.entities.Word;
import nghia.ndict.utils.Constants;
import nghia.ndict.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 4/15/13
 * Time: 3:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class Dictionary extends BaseEntity {
    DataHandler dataHandler;
    private ArrayList<FirstHash> hashList;
    private LoadReference loadReference;
    private HashIndex currentHash;

    public ArrayList<FirstHash> getHashList() {
        return hashList;
    }

    public void setHashList(ArrayList<FirstHash> hashList) {
        this.hashList = hashList;
    }

    public void initDictionary(String dictName) throws Exception {
        loadReference = new LoadReference();
        dataHandler = new DataHandler(dictName);
        loadHash();
    }

    private void loadHash() throws Exception {
        hashList = dataHandler.readHash();
    }

    public ArrayList<Word> loadWords(HashIndex hIdx, boolean saveLastWordIndex) throws Exception {
        if (hashList == null || hashList.size() == 0) {
            Utils.log("Hash list is empty");
            throw new Exception("Hash list is empty");
        }
        ArrayList<Word> wordArrayList = null;
        try {
            ThirdHash thirdHash = hashList.get(hIdx.firstIndex).getItems().get(hIdx.secondIndex).getItems().get(hIdx.thirdIndex);
            if (thirdHash != null)
                wordArrayList = dataHandler.readWords(thirdHash, saveLastWordIndex);
        } catch (Exception ex) {
            Utils.log("Failed to load words");
            throw ex;
        }
        return wordArrayList;
    }

    public ArrayList<Word> loadUp(int numberToLoad) throws Exception {
        ArrayList<Word> wordArrayList = new ArrayList<Word>();
        HashIndex hIdx;
        do {
            hIdx = getPreviousHash();
            setCurrentHash(hIdx);
            wordArrayList.addAll(0, loadWords(hIdx, false));
        } while (!hIdx.isFirstHash() && wordArrayList.size() < numberToLoad);

        return wordArrayList;
    }

    public ArrayList<Word> loadDown() throws IOException {
        return dataHandler.readNextWords(Constants.DEFAULT_LIST_LOAD);
    }

    public SearchResult search(String text) throws Exception {
        HashIndex hIdx = findHashIndex(text);
        SearchResult sResult = new SearchResult();
        sResult.setKey(text);
        try {
            sResult.setWordsList(this.loadWords(hIdx, true));
            if (sResult.count() < Constants.DEFAULT_LIST_LOAD) {
                sResult.appendWords(dataHandler.readNextWords(Constants.DEFAULT_LIST_LOAD - sResult.count()));
            }
            while (sResult.count() < Constants.LIST_SIZE) {
                sResult.getWordsList().addAll(0, loadUp(Constants.LIST_SIZE - sResult.count()));
            }
        } catch (Exception ex) {
            Utils.log("Failed to search for text");
            throw ex;
        }

        return sResult;
    }

    public HashIndex findHashIndex(String text) throws Exception {
        int firstIndex;
        int secondIndex = 0;
        int thirdIndex = 0;
        if (Utils.isNullOrEmpty(text)) {
            HashIndex foundHash = new HashIndex(0, 0, 0);
            setCurrentHash(foundHash);
            return foundHash;
        }
        try {
            if (text.length() <= 1) {
                firstIndex = binarySearchFirstHash(text, 0, hashList.size() - 1);
            } else if (text.length() == 2) {
                firstIndex = binarySearchFirstHash(text.substring(0, 1), 0, hashList.size() - 1);
                secondIndex = hashList.get(firstIndex).binarySearchSecondHash(text.substring(1, 2), 0, hashList.get(firstIndex).getItems().size() - 1);
            } else {
                firstIndex = binarySearchFirstHash(text.substring(0, 1), 0, hashList.size() - 1);
                secondIndex = hashList.get(firstIndex).binarySearchSecondHash(text.substring(1, 2), 0, hashList.get(firstIndex).getItems().size() - 1);
                SecondHash secondHash = hashList.get(firstIndex).getItems().get(secondIndex);
                thirdIndex = secondHash.binarySearchThirdHash(text.substring(2, 3), 0, secondHash.getItems().size() - 1);
            }
        } catch (Exception e) {
            Utils.log("Failed to find hash index of word");
            throw e;
        }
        Utils.log("Hash Index: " + firstIndex + "--------" + secondIndex + "--------" + thirdIndex + "--------");
        HashIndex foundHash = new HashIndex(firstIndex, secondIndex, thirdIndex);
        setCurrentHash(foundHash);
        return foundHash;
    }

    private int binarySearchFirstHash(String key, int start, int end) {
        if (end < start || Utils.isNullOrEmpty(key)) {
            return 0;
        }
        int middle = (end + start) / 2;
        String median = hashList.get(middle).getKey();

        if (key.equalsIgnoreCase(median)) {
            return middle;
        } else if (Utils.compareTwoStrings(key, median) == -1) {
            return binarySearchFirstHash(key, start, middle - 1);
        } else {
            return binarySearchFirstHash(key, middle + 1, end);
        }
    }

    public HashIndex getPreviousHash() throws Exception {
        try {
            FirstHash firstHash = hashList.get(currentHash.firstIndex);
            if (currentHash.thirdIndex > 0) {
                return new HashIndex(currentHash.firstIndex, currentHash.secondIndex, currentHash.thirdIndex - 1);
            } else if (currentHash.secondIndex > 0) {
                return new HashIndex(currentHash.firstIndex, currentHash.secondIndex - 1, firstHash.getItems().get(currentHash.secondIndex - 1).getItems().size() - 1);
            } else if (currentHash.firstIndex > 0) {
                int newFirstIndex = currentHash.firstIndex - 1;
                FirstHash newFirstHash = hashList.get(newFirstIndex);
                return new HashIndex(currentHash.firstIndex - 1, newFirstHash.getLastHash2Index(), newFirstHash.getLastHash3Index());
            } else {
                return currentHash;
            }

        } catch (Exception e) {
            throw e;
        }
    }

    public HashIndex getCurrentHash() {
        return currentHash;
    }

    public void setCurrentHash(HashIndex foundHash) {
        currentHash = foundHash;
    }
}
