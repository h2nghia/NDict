package nghia.ndict.dictionary;

import java.util.ArrayList;

import nghia.ndict.entities.Word;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 4/27/13
 * Time: 11:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class Decompressor {
    private int sizeOfBlock;
    private int headerSize;
    private ArrayList<Block> blocks;

    public int getSizeOfBlock() {
        return sizeOfBlock;
    }

    public void setSizeOfBlock(int sizeOfBlock) {
        this.sizeOfBlock = sizeOfBlock;
    }

    public int getBlockCount() {
        return blocks.size();
    }

    public Block getBlock(int index) {
        return blocks.get(index);
    }

    public void addBlock(Block b) {
        if (blocks == null) {
            blocks = new ArrayList<Block>();
        }
        blocks.add(b);
    }

    public void decompress(Word w) {
        int startBlock = w.getStartMeaningIndex() / sizeOfBlock;
        int numberOfBlock = (w.getEndMeaningIndex() - w.getStartMeaningIndex());
    }

    public int getHeaderSize() {
        return headerSize;
    }

    public void setHeaderSize(int headerSize) {
        this.headerSize = headerSize;
    }
}
