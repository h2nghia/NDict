package nghia.ndict.dictionary;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.Deflater;
import java.util.zip.DeflaterOutputStream;

import nghia.ndict.utils.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: nghia
 * Date: 3/20/13
 * Time: 3:05 PM
 * To change this template use File | Settings | File Templates.
 */

public class Compressor {
    private List<Block> compressHeader;
    private int sizeOfBlock = Constants.DEFAULT_BLOCK_SIZE;
    private DeflaterOutputStream deflaterOutputStream;
    private ByteArrayOutputStream compressedOutputStream;

    public byte[] getCompressedData() {
        return compressedOutputStream.toByteArray();
    }

    public void compress(byte[] input) throws IOException {
        int offset = 0;
        compressedOutputStream = new ByteArrayOutputStream();
        if (deflaterOutputStream == null) {
            deflaterOutputStream = new DeflaterOutputStream(compressedOutputStream, new Deflater());
        }
        if (compressHeader == null) {
            compressHeader = new LinkedList<Block>();
        }
        int count = 0;
        while (count < input.length) {
            int bufferSize = sizeOfBlock;
            if (input.length - count < sizeOfBlock) {
                bufferSize = input.length - count;
            }

            deflaterOutputStream.write(input, count, bufferSize);
            count += bufferSize;
            //compressed data index
            compressHeader.add(new Block(offset, compressedOutputStream.size()));
            offset = compressedOutputStream.size();
        }
        compressedOutputStream.close();
    }

//    public byte[] getHeader() throws IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
//
//        //Total number of block
//        dataOutputStream.writeInt(compressHeader.size());
//        //Block size
//        dataOutputStream.writeInt(sizeOfBlock);
//        //block index data
//        for (Block block : compressHeader) {
//            dataOutputStream.writeInt(block.len);
//            dataOutputStream.writeInt(block.pos);
//        }
//        byteArrayOutputStream.close();
//        return byteArrayOutputStream.toByteArray();
//    }

}
