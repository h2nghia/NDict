package nghia.ndict.dictionary;

import android.content.res.AssetManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.ArrayList;
import java.util.Vector;

import nghia.ndict.activity.DictionaryApplication;
import nghia.ndict.entities.FirstHash;
import nghia.ndict.entities.SecondHash;
import nghia.ndict.entities.ThirdHash;
import nghia.ndict.entities.Word;
import nghia.ndict.utils.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 3/20/13
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class DataHandler {
    private static final String TAG = DataHandler.class.getName();
    private static final int SIZE_OF_CHUNK = 1024 * 1024;
    private static DataHandler mInstance = null;
    private int lastWordIndex = 0;
    private DictConfig dictConfig;
    private String dictName;

    public DataHandler(String dictName) {
        try {
            this.dictName = dictName;
            loadDictConfig();
            Log.d("***", "Dictionary config is loaded");
        } catch (Exception e) {
            Log.d("***", "Unable to load dictionary config");
        }
    }

    public static DataHandler getInstance(String dictName) {
        if (mInstance == null) {
            mInstance = new DataHandler(dictName);
        }
        return mInstance;
    }

    private int readInt(InputStream is) throws IOException {
        byte[] buffer = new byte[4];
        is.read(buffer, 0, 4);
        return Utils.BytesToInt(buffer);
    }

    private String readString(InputStream is, int byteLen) throws IOException {
        byte[] buffer = new byte[byteLen];
        is.read(buffer, 0, byteLen);
        return new String(buffer);
    }

    public ArrayList<FirstHash> readHash() throws Exception {
        Utils.log("Start read hash");
        InputStream inputStream = DictionaryApplication.getContext().getResources().getAssets().open(dictName + "/" + dictName + ".hsh");
        ArrayList<FirstHash> hashList = new ArrayList<FirstHash>();
        try {
            int hashCount = readInt(inputStream);
            int characterByteSize;
            while (hashCount > 0) {
                characterByteSize = readInt(inputStream);
                String firstCharacter = readString(inputStream, characterByteSize);
                FirstHash firstHash = new FirstHash(firstCharacter);
                int numberOfSecondHash = readInt(inputStream);
                while (numberOfSecondHash > 0) {
                    characterByteSize = readInt(inputStream);
                    String secondCharacter = readString(inputStream, characterByteSize);
                    SecondHash secondHash = new SecondHash(secondCharacter);
                    int numberOfThirdHash = readInt(inputStream);
                    while (numberOfThirdHash > 0) {
                        characterByteSize = readInt(inputStream);
                        String thirdCharacter = readString(inputStream, characterByteSize);
                        ThirdHash thirdHash = new ThirdHash(thirdCharacter);
                        thirdHash.setStartIdx(readInt(inputStream));
                        thirdHash.setEndIdx(readInt(inputStream));
                        numberOfThirdHash--;
                        secondHash.getItems().add(thirdHash);
                    }
                    numberOfSecondHash--;
                    firstHash.getItems().add(secondHash);
                }
                hashCount--;
                hashList.add(firstHash);
            }
            Utils.log("ReadHash is done!");
        } catch (Exception ex) {
            Utils.log("Failed to readHash");
            throw new Exception("Failed to readHash", ex);
        }
        return hashList;

    }

    private void loadDictConfig() throws IOException {
        InputStream inputStream = DictionaryApplication.getContext().getResources().getAssets().open(dictName + "/" + dictName + ".conf");
        dictConfig = new DictConfig();
        if (inputStream != null) {
            byte[] buffer = new byte[4];
            inputStream.read(buffer);
            dictConfig.setTotalWords(Utils.BytesToInt(buffer));
            inputStream.read(buffer);
            dictConfig.setEndWordIndex(Utils.BytesToInt(buffer));
            inputStream.close();
        }
    }

    public ArrayList<Word> readWords(ThirdHash thirdHash, boolean saveLastWordIndex) throws IOException {
        ArrayList<Word> wordArrayList = new ArrayList<Word>();
        int sizeOfWordInByte;
        int s = thirdHash.getStartIdx();
        int e = thirdHash.getEndIdx();

        InputStream inputStream = DictionaryApplication.getContext().getResources().getAssets().open(dictName + "/" + dictName + ".wds");
        inputStream.skip(s);
        while (s < e) {
            sizeOfWordInByte = readInt(inputStream);
            Word word = new Word();
            word.setContent(readString(inputStream, sizeOfWordInByte));
            word.setStartMeaningIndex(readInt(inputStream));
            word.setEndMeaningIndex(readInt(inputStream));
            wordArrayList.add(word);
            s += 4 + sizeOfWordInByte + 4 + 4;
        }
        if (saveLastWordIndex) {
            lastWordIndex = e;
        }
        return wordArrayList;

    }

    public ArrayList<Word> readNextWords(int numberToLoad) throws IOException {
        ArrayList<Word> wordArrayList = new ArrayList<Word>();

        int sizeOfWordInByte;
        InputStream inputStream = DictionaryApplication.getContext().getResources().getAssets().open(dictName + "/" + dictName + ".wds");
        inputStream.skip(lastWordIndex);
        while (lastWordIndex < dictConfig.getEndWordIndex() && numberToLoad > 0) {
            sizeOfWordInByte = readInt(inputStream);
            Word word = new Word();
            word.setContent(readString(inputStream, sizeOfWordInByte));
            word.setStartMeaningIndex(readInt(inputStream));
            word.setEndMeaningIndex(readInt(inputStream));
            wordArrayList.add(word);
            numberToLoad--;
            lastWordIndex += 4 + sizeOfWordInByte + 4 + 4;
        }

        return wordArrayList;
    }

    public String readMeaning(Word w) throws IOException {
        int sizeToRead = w.getEndMeaningIndex() - w.getStartMeaningIndex();
        int startMeaning = w.getStartMeaningIndex();
        int startChunk = startMeaning / SIZE_OF_CHUNK;
        int endChunk = sizeToRead / SIZE_OF_CHUNK + startChunk;

        AssetManager assetManager = DictionaryApplication.getContext().getAssets();
        Vector<InputStream> inputStreams = new Vector<InputStream>();
        for (int i = startChunk + 1; i <= endChunk + 1; i++) {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(assetManager.open(dictName + "/" + dictName + "_" + i + ".dat"));
            inputStreams.add(bufferedInputStream);
        }

        SequenceInputStream sis = new SequenceInputStream(inputStreams.elements());

        sis.skip(startMeaning % SIZE_OF_CHUNK);
        byte[] buffer = new byte[sizeToRead];
        sis.read(buffer);
        String meaning = new String(buffer);
        Log.d(TAG, "Word meaning: " + meaning);
        return meaning;
    }

}
