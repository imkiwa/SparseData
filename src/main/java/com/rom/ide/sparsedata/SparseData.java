package com.rom.ide.sparsedata;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

/**
 * @author kiva
 */

public class SparseData {
    public static final int BLOCK_SIZE = 4096;
    public static final int BUFFER_SIZE = 1024;

    private String systemDatFile;
    private TransferInfo transferInfo;

    public SparseData(String systemDatFile, String transferListFile)
            throws IOException, IllegalStateException {
        this.systemDatFile = systemDatFile;
        this.transferInfo = TransferInfo.from(transferListFile);
    }

    public void convertToImage(String imageFile) throws IOException {
        RandomAccessFile datFile = new RandomAccessFile(systemDatFile, "r");
        RandomAccessFile imgFile = new RandomAccessFile(imageFile, "rw");

        int maxBlock = transferInfo.getMaxBlock();
        int length = maxBlock * BLOCK_SIZE;
        imgFile.setLength(length);

        byte[] emptyBuffer = new byte[BUFFER_SIZE];
        Arrays.fill(emptyBuffer, (byte) 0);

        imgFile.seek(0);
        for (int i = 0; i < maxBlock; ++i) {
            for (int k = 0; k < 4; ++k) {
                imgFile.write(emptyBuffer);
            }
        }
        imgFile.seek(0);

        for (TransferCommand command : transferInfo.commands) {
            /**
             * 我们只需要用到 new 里的数据来创建分区
             */
            if (!command.isNewCommand()) {
                continue;
            }

            int position = 0;
            while (position < command.blocks.length) {
                int start = command.blocks[position++];
                int end = command.blocks[position++];

                int blockCount = end - start;
                int startPosition = start * BLOCK_SIZE;

                imgFile.seek(startPosition);
                for (int i = 0; i < blockCount; ++i) {
                    for (int k = 0; k < 4; ++k) {
                        datFile.read(emptyBuffer);
                        imgFile.write(emptyBuffer);
                    }
                }
            }
        }

        datFile.close();
        imgFile.close();
    }
}
